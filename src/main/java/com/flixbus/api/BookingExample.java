package com.flixbus.api;

import com.flixbus.api.domain.*;

import static com.flixbus.api.util.ExampleCodeHelper.*;

/**
 * Creates a reservation for 1 Adult on a return trip.
 *
 * For constants and helper functions please see
 * the {@link com.flixbus.api.util.ExampleCodeHelper}.
 */
public class BookingExample {

    public static void main(String[] args) {
        try {
            // first we need to be authenticated
            AuthenticationRequest  authRequest  = new AuthenticationRequest(INTEGRATION_EMAIL, INTEGRATION_TOKEN);
            AuthenticationResponse authResponse = PublicApiClient.authenticate(authRequest);

            // create a reservation (with the outward trip)
            ReservationResponse reservationResponse = createReservation(authResponse);

            // add the return trip to a reservation (we could add even more trips to the reservation)
            reservationResponse = addTripToReservation(authResponse, reservationResponse);

            // add passenger details to the reservation
            addPassengerDetailsToReservation(reservationResponse);

            //TODO list payment methods
            //TODO start payment
            //TODO commit payment
            //TODO retrieve ticket data
        } catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * In order to create a reservation we need to provide
     * exactly 1 trip. If we need more trips on our
     * reservation we must add it later.
     */
    private static ReservationResponse createReservation(AuthenticationResponse authResponse) throws Exception {
        // we need a search response, to pick a TripItem to be booked
        TripSearchRequest  searchRequest  = getTripSearchRequest(STATION_ID_BERLIN, STATION_ID_MUNICH, 7);
        TripSearchResponse searchResponse = PublicApiClient.tripSearch(searchRequest);

        // we create the reservation
        TripItem tripItemToBook = searchResponse.getTrip(0).getTripItem(0);
        ReservationRequest  reservationRequest  = getReservationRequest(tripItemToBook, authResponse);

        return PublicApiClient.createReservation(reservationRequest);
    }

    /**
     * After having created a reservation we can add trips.
     */
    private static ReservationResponse addTripToReservation(AuthenticationResponse authResponse,
                                                            ReservationResponse reservationResponse) throws Exception {
        // we add a (return) trip to the reservation
        TripSearchRequest searchRequest   = getTripSearchRequest(STATION_ID_MUNICH, STATION_ID_BERLIN, 9);
        TripSearchResponse searchResponse = PublicApiClient.tripSearch(searchRequest);

        TripItem tripItemToBook                = searchResponse.getTrip(0).getTripItem(1);
        ReservationRequest reservationRequest  = getReservationRequest(tripItemToBook, authResponse);
        reservationRequest.setReservation(reservationResponse.getReservation());

        return PublicApiClient.createReservation(reservationRequest);
    }

    /**
     * After having created a reservation we can add
     * passenger details.
     *
     * Attention: If you have more than 1 trip and more than
     * 1 passenger on your reservation it can happen that the
     * number of passengers differs from trip to trip.
     * E.g. on the first trip could be only 1 passenger and
     * on the trip back there could be 2 passengers.
     */
    private static void addPassengerDetailsToReservation(ReservationResponse reservationResponse) throws Exception {
        // first we need to get a passenger details object
        PassengerDetailsRequest passengerDetailsRequest   = getPassengerDetailsRequest(reservationResponse.getReservation());
        PassengerDetailsResponse passengerDetailsResponse = PublicApiClient.getPassengerDetails(passengerDetailsRequest);

        // now we can add passenger details
        AddPassengerDetailsRequest addPassengerDetailsRequest =
                getAddPassengerDetailsRequest(passengerDetailsResponse, reservationResponse.getReservation());

        PublicApiClient.addPassengerDetails(addPassengerDetailsRequest);
    }
}
