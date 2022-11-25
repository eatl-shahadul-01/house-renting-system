package bd.com.squarehealth.corelibrary.services;

import bd.com.squarehealth.corelibrary.dtos.BookingDto;

/**
 * Inter-service Communication Service.
 * This class is used to call APIs from external services.
 */
public interface ISCService {

    BookingDto placeBookingRequest(BookingDto bookingData, String authorization) throws Exception;
}
