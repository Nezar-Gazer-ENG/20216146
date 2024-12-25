package com.sdaproject.api20216146.service;

import com.sdaproject.api20216146.model.Booking;
import com.sdaproject.api20216146.model.HotelRoom;
import com.sdaproject.api20216146.model.Event;
import com.sdaproject.api20216146.model.User;
import com.sdaproject.api20216146.repository.BookingRepository;
import com.sdaproject.api20216146.repository.HotelRoomRepository;
import com.sdaproject.api20216146.repository.EventRepository;
import com.sdaproject.api20216146.repository.UserRepository;
import com.sdaproject.api20216146.util.BookingMessageTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Service
public class BookingService {

    private static final Logger logger = Logger.getLogger(BookingService.class.getName());
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HotelRoomRepository hotelRoomRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private NotificationService notificationService;

    public Booking createBooking(Booking booking) {
        validateBooking(booking);

        User user = userRepository.findById(booking.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found."));
        booking.setUser(user);
        booking.setBookingDate(new Date());

        String notificationMessage;
        if (isHotelBooking(booking)) {
            handleHotelBooking(booking);
            notificationMessage = BookingMessageTemplate.getHotelBookingMessage(
                    booking.getHotelRoom().getName(),
                    booking.getCheckInDate().toString()
            );
        } else if (isEventBooking(booking)) {
            handleEventBooking(booking);
            notificationMessage = BookingMessageTemplate.getEventBookingMessage(
                    booking.getEvent().getName(),
                    booking.getEvent().getEventDate().toString()
            );
        } else {
            throw new RuntimeException("Booking must be associated with either a hotel room or an event.");
        }

        Booking savedBooking = bookingRepository.save(booking);
        logger.info("Booking created successfully with ID: " + savedBooking.getId());

        logger.info("Scheduling notification: " + notificationMessage);
        scheduleNotification(notificationMessage);

        return savedBooking;
    }

    private void validateBooking(Booking booking) {
        if (booking.getUser() == null || booking.getUser().getId() == null) {
            throw new RuntimeException("Booking must have a valid user.");
        }
        if (isHotelBooking(booking) && isEventBooking(booking)) {
            throw new RuntimeException("A booking cannot be associated with both a hotel room and an event.");
        }
    }

    private void scheduleNotification(String message) {
        scheduler.schedule(() -> {
            try {
                notificationService.sendNotification("/topic/default", message);
                logger.info("Notification sent after 3 seconds: " + message);
            } catch (Exception e) {
                logger.severe("Failed to send delayed notification: " + e.getMessage());
            }
        }, 3, TimeUnit.SECONDS);
    }

    public Booking getBookingById(Long id) {
        Optional<Booking> optionalBooking = bookingRepository.findById(id);
        return optionalBooking.orElseThrow(() -> new RuntimeException("Booking not found with ID: " + id));
    }

    public List<Booking> getBookingsByUserId(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    public List<Booking> getHotelBookingsForUser(Long userId) {
        return bookingRepository.findByUserIdAndHotelRoomIsNotNull(userId);
    }

    public List<Booking> getEventBookingsForUser(Long userId) {
        return bookingRepository.findByUserIdAndEventIsNotNull(userId);
    }

    private boolean isHotelBooking(Booking booking) {
        return booking.getHotelRoom() != null && booking.getHotelRoom().getId() != null;
    }

    private boolean isEventBooking(Booking booking) {
        return booking.getEvent() != null && booking.getEvent().getId() != null;
    }

    private void handleHotelBooking(Booking booking) {
        HotelRoom hotelRoom = hotelRoomRepository.findById(booking.getHotelRoom().getId())
                .orElseThrow(() -> new RuntimeException("Hotel room not found."));

        if (!isRoomAvailable(hotelRoom, booking.getCheckInDate(), booking.getCheckOutDate())) {
            throw new RuntimeException("Hotel room is not available for the selected dates.");
        }

        booking.setHotelRoom(hotelRoom);
        booking.setTotalAmount(calculateHotelBookingAmount(hotelRoom, booking.getCheckInDate(), booking.getCheckOutDate()));
    }

    private void handleEventBooking(Booking booking) {
        Event event = eventRepository.findById(booking.getEvent().getId())
                .orElseThrow(() -> new RuntimeException("Event not found."));

        if (!isEventAvailable(event, booking.getQuantity())) {
            throw new RuntimeException("Not enough seats available. Available: " + event.getSeatsAvailable());
        }

        event.setSeatsAvailable(event.getSeatsAvailable() - booking.getQuantity());
        eventRepository.save(event);
        booking.setEvent(event);
        booking.setTotalAmount(calculateEventBookingAmount(event, booking.getQuantity()));
    }

    private boolean isRoomAvailable(HotelRoom hotelRoom, Date checkInDate, Date checkOutDate) {
        return bookingRepository.findByHotelRoom(hotelRoom).stream()
                .noneMatch(booking -> !(checkOutDate.before(booking.getCheckInDate()) || checkInDate.after(booking.getCheckOutDate())));
    }

    private boolean isEventAvailable(Event event, int quantity) {
        return event.getSeatsAvailable() >= quantity;
    }

    private double calculateHotelBookingAmount(HotelRoom hotelRoom, Date checkInDate, Date checkOutDate) {
        long durationInMillis = checkOutDate.getTime() - checkInDate.getTime();
        long days = TimeUnit.DAYS.convert(durationInMillis, TimeUnit.MILLISECONDS);
        return hotelRoom.getPricePerNight() * (days > 0 ? days : 1);
    }

    private double calculateEventBookingAmount(Event event, int quantity) {
        return event.getTicketPrice() * quantity;
    }
}
