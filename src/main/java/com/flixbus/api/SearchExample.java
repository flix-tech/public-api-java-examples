package com.flixbus.api;

import com.flixbus.api.domain.Trip;
import com.flixbus.api.domain.TripSearchRequest;
import com.flixbus.api.domain.TripSearchResponse;

import static com.flixbus.api.util.ExampleCodeHelper.*;

/**
 * Performs a trip search and lists trip details.
 */
public class SearchExample {

    public static void main(String[] args) {
        try {
            TripSearchRequest request   = getTripSearchRequest(STATION_ID_BERLIN, STATION_ID_MUNICH, 7);
            TripSearchResponse response = PublicApiClient.tripSearch(request);

            int foundTrips = response.getTrips().size();
            System.out.println(String.format("%d trips found.", foundTrips));

            for (Trip trip : response.getTrips()){
                System.out.println(trip.getDetails());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }
}
