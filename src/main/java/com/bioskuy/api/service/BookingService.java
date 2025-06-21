package com.bioskuy.api.service;

import java.util.ArrayList;
import java.util.List;

import com.bioskuy.api.entity.*;
import com.bioskuy.api.enums.BookingStatus;
import com.bioskuy.api.model.booking.BookingRequest;
import com.bioskuy.api.model.booking.BookingResponse;
import com.bioskuy.api.model.bookingSeats.BookingSeatResponse;
import com.bioskuy.api.model.customer.CustomerResponse;
import com.bioskuy.api.model.midtrans.SnapTransactionResult;
import com.bioskuy.api.model.movie.MovieResponse;
import com.bioskuy.api.model.payment.PaymentResponse;
import com.bioskuy.api.model.schedule.ScheduleResponse;
import com.bioskuy.api.model.theater.TheaterResponse;
import com.bioskuy.api.model.ticket.TicketResponse;
import com.bioskuy.api.repository.*;
import com.midtrans.httpclient.error.MidtransError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.bioskuy.api.enums.SeatStatus;

@Service
public class BookingService implements BookingServiceInterface{
    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final SeatRepository seatRepository;
    private final ScheduleRepository scheduleRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final SeatService seatService;
    private final PaymentService paymentService;

    @Autowired
    public BookingService(
            BookingRepository bookingRepository,
            CustomerRepository customerRepository,
            SeatRepository seatRepository,
            ScheduleRepository scheduleRepository,
            BookingSeatRepository bookingSeatRepository,
            SeatService seatService,
            PaymentService paymentService
    ) {
        this.bookingRepository = bookingRepository;
        this.customerRepository = customerRepository;
        this.seatRepository = seatRepository;
        this.scheduleRepository = scheduleRepository;
        this.bookingSeatRepository = bookingSeatRepository;
        this.seatService = seatService;
        this.paymentService = paymentService;
    }

    public BookingResponse getBookingbyId(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no such Booking"));

        return toBookingResponse(booking);
    }

    public List<BookingResponse> getAllBookings() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with email: " + email));

        List<Booking> bookings = bookingRepository.findAllByCustomerAndStatus(customer, BookingStatus.PAID);

        return bookings.stream()
                .map(this::toBookingResponse)
                .toList();
    }


    @Transactional
    public PaymentResponse createBooking(BookingRequest bookingRequest) throws MidtransError {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with email: " + email));

        Schedule schedule = scheduleRepository.findById(bookingRequest.getScheduleId())
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found with ID: " + bookingRequest.getScheduleId()));

        List<Seat> seats = seatRepository.findAllById(bookingRequest.getSeatIds());
        List<Seat> takenSeats = seats.stream()
                .filter(seat -> seat.getStatus() == SeatStatus.RESERVED)
                .toList();

        if (!takenSeats.isEmpty()) {
            throw new IllegalArgumentException("Some seats are already taken");
        }

        double totalPrice = schedule.getPrice() * seats.size();

        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setSchedule(schedule);
        booking.setAmount(totalPrice);
        booking.setStatus(BookingStatus.PENDING);
        booking = bookingRepository.save(booking);

        List<BookingSeat> bookingSeats = new ArrayList<>();
        for (Seat seat : seats) {
            BookingSeat bookingSeat = new BookingSeat();
            bookingSeat.setBooking(booking);
            bookingSeat.setSeat(seat);
            bookingSeats.add(bookingSeat);
        }
        bookingSeatRepository.saveAll(bookingSeats);

        booking.setBookingSeats(bookingSeats);

        seats.forEach(seat -> seat.setStatus(SeatStatus.RESERVED));
        seatRepository.saveAll(seats);

        SnapTransactionResult payment = this.paymentService.processPayment(booking);

        BookingResponse bookingResponse = toBookingResponse(booking);

        return PaymentResponse.builder()
                .booking(bookingResponse)
                .payment(payment)
                .build();
    }

    public BookingResponse cancelBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (booking.getStatus() == BookingStatus.PAID) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Booking is paid, can't cancel");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        booking = bookingRepository.save(booking);

        return toBookingResponse(booking);
    }

    public BookingResponse toBookingResponse(Booking booking) {
        CustomerResponse customerResponse = CustomerResponse.builder()
                .id(booking.getCustomer().getId())
                .name(booking.getCustomer().getName())
                .email(booking.getCustomer().getEmail())
                .build();

        MovieResponse movie = MovieResponse.builder()
                .id(booking.getSchedule().getMovie().getId())
                .title(booking.getSchedule().getMovie().getTitle())
                .genre(booking.getSchedule().getMovie().getGenre())
                .duration(booking.getSchedule().getMovie().getDuration())
                .rating(booking.getSchedule().getMovie().getRating())
                .image(booking.getSchedule().getMovie().getImage())
                .build();

        TheaterResponse theater = TheaterResponse.builder()
                .id(booking.getSchedule().getTheater().getId())
                .name(booking.getSchedule().getTheater().getName())
                .address(booking.getSchedule().getTheater().getAddress())
                .capacity(booking.getSchedule().getTheater().getCapacity())
                .build();

        ScheduleResponse scheduleResponse = ScheduleResponse.builder()
                .id(booking.getSchedule().getId())
                .movie(movie)
                .theater(theater)
                .startTime(booking.getSchedule().getStartTime())
                .endTime(booking.getSchedule().getEndTime())
                .price(booking.getSchedule().getPrice())
                .build();

        List<BookingSeatResponse> bookingSeatResponses = booking.getBookingSeats().stream()
                .map(bs -> BookingSeatResponse.builder()
                        .id(bs.getId())
                        .seat(seatService.toSeatResponse(bs.getSeat()))
                        .build())
                .toList();

        List<TicketResponse> ticketResponses = booking.getTickets() != null
                ? booking.getTickets().stream()
                .map(ticket -> TicketResponse.builder()
                        .id(ticket.getId())
                        .ticketNumber(ticket.getTicketNumber())
                        .build())
                .toList()
                : List.of();

        return BookingResponse.builder()
                .id(booking.getId())
                .amount(booking.getAmount())
                .customer(customerResponse)
                .schedule(scheduleResponse)
                .bookingSeats(bookingSeatResponses)
                .tickets(ticketResponses)
                .status(booking.getStatus())
                .build();
    }


}