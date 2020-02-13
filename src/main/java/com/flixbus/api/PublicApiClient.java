package com.flixbus.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flixbus.api.domain.*;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static com.flixbus.api.util.ExampleCodeHelper.INTEGRATION_SERVER;
import static com.flixbus.api.util.ExampleCodeHelper.INTEGRATION_TOKEN;

/**
 * Reflects (some of) the endpoints documented on
 * our Developer Portal:
 *
 * https://developer.flix.tech/
 */
public class PublicApiClient {

    static final int HTTP_STATUS_OK = 200;

    static String apiToken   = INTEGRATION_TOKEN;
    static String server     = INTEGRATION_SERVER;

    static HttpClient client = HttpClient.newHttpClient();

    static ObjectMapper mapper = new ObjectMapper();

    /**
     * Authenticates a user and returns a session token
     * which is required on subsequent requests.
     *
     * https://developer.flix.tech/#post_partner_authenticate
     */
    public static AuthenticationResponse authenticate(AuthenticationRequest request) throws Exception {
        Map<Object, Object> data = new HashMap<>();
        data.put("email"   , request.getEmail());
        data.put("password", request.getPassword());

        String json = httpPostRequest("/public/v1/partner/authenticate.json", data);

        AuthenticationResponse response = mapper.readValue(json, AuthenticationResponse.class);
        return response;
    }

    /**
     * Get available trips between cities with all the stations
     * combinations possible belonging to each city.
     *
     * https://developer.flix.tech/#get_trip_search
     */
    public static TripSearchResponse tripSearch(TripSearchRequest request) throws Exception {
        StringBuilder builder = new StringBuilder("/public/v1/trip/search.json")
                .append("?search_by="     ).append(request.getSearch_by())
                .append("&from="          ).append(request.getFrom().getId())
                .append("&to="            ).append(request.getTo().getId())
                .append("&departure_date=").append(request.getDeparture_date())
                .append("&adult="         ).append(request.getAdult())
                .append("&children="      ).append(request.getChildren())
                .append("&bikes="         ).append(request.getBikes())
                .append("&currency="      ).append(request.getCurrency());

        String json = httpGetRequest(builder.toString());

        TripSearchResponse response = mapper.readValue(json, TripSearchResponse.class);
        return response;
    }

    /**
     * Creates a reservation and returns its details
     * like {@link CartItem}s.
     *
     * https://developer.flix.tech/#put_reservation_items
     */
    public static ReservationResponse createReservation(ReservationRequest request) throws Exception {
        Map<Object, Object> data = new HashMap<>();
        data.put("trip_uid", request.getTrip_uid());
        data.put("currency", request.getCurrency());
        data.put("adult"   , request.getAdult());

        if (request.hasReservation()){
            data.put("reservation",       request.getReservation().getId());
            data.put("reservation_token", request.getReservation().getToken());
        }

        Map<String,String> additionalHeaders = new HashMap<>();
        additionalHeaders.put("X-API-Session", request.getSessionToken());

        String json = httpPutRequest("/public/v1/reservation/items.json", data, additionalHeaders);

        ReservationResponse response = mapper.readValue(json, ReservationResponse.class);
        return response;
    }

    /**
     * Returns the passenger details which are needed
     * in order to add passengers to a reservation.
     *
     * https://developer.flix.tech/#get_passengers
     */
    public static PassengerDetailsResponse getPassengerDetails(PassengerDetailsRequest request) throws Exception {
        String path = String.format("/public/v1/reservations/%s/passengers.json", request.getId());
        StringBuilder builder = new StringBuilder(path)
                .append("?reservation_token=").append(request.getReservation_token());

        String json = httpGetRequest(builder.toString());

        PassengerDetailsResponse response = mapper.readValue(json, PassengerDetailsResponse.class);
        return response;
    }

    /**
     * Adds passengers to a reservation.
     *
     * https://developer.flix.tech/#put_passenger
     */
    public static AddPassengerDetailsResponse addPassengerDetails(AddPassengerDetailsRequest request) throws Exception {
        String path = String.format("/public/v1/reservations/%s/passengers.json", request.getId());

        Map<Object, Object> data = new HashMap<>();
        data.put("reservation_token", request.getPassengerRequest().getReservation_token());

        int i=0;
        for (Passenger passenger : request.getPassengerRequest().getPassengers()) {
            data.put("passengers["+i+"][firstname]"            , passenger.getFirstname());
            data.put("passengers["+i+"][lastname]"             , passenger.getLastname());
            data.put("passengers["+i+"][phone]"                , passenger.getPhone());
            data.put("passengers["+i+"][birthdate]"            , passenger.getBirthdate());
            data.put("passengers["+i+"][type]"                 , passenger.getType());
            data.put("passengers["+i+"][reference_id]"         , passenger.getReference_id());
            data.put("passengers["+i+"][parental_permission]"  , passenger.isParental_permission());

            i++;
        }

        String json = httpPutRequest(path, data, new HashMap<>());

        AddPassengerDetailsResponse response = mapper.readValue(json, AddPassengerDetailsResponse.class);
        return response;
    }

    private static String httpGetRequest(String pathAndParams) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .GET()
                .uri(URI.create(server + pathAndParams))
                .headers("Accept"              , "application/json")
                .headers("Accept-Enconding"    , "gzip, identity")
                .headers("Accept-Language"     , "en")
                .headers("User-Agent"          , "public-api-example-client")
                .headers("Content-Type"        , "application/x-www-form-urlencoded")
                .headers("X-API-Authentication", apiToken)
                .build();

        return request(request);
    }

    private static String httpPostRequest(String path, Map<Object, Object> payload) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .POST(getUrlEncodedFormData(payload))
                .uri(URI.create(server + path))
                .headers("Accept"              , "application/json")
                .headers("Accept-Enconding"    , "gzip, identity")
                .headers("Accept-Language"     , "en")
                .headers("User-Agent"          , "public-api-example-client")
                .headers("Content-Type"        , "application/x-www-form-urlencoded")
                .headers("X-API-Authentication", apiToken)
                .build();

        return request(request);
    }

    private static String httpPutRequest(String path, Map<Object, Object> payload, Map<String,
            String> additionalHeaders) throws Exception {

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .PUT(getUrlEncodedFormData(payload))
                .uri(URI.create(server + path))
                .headers("Accept"              , "application/json")
                .headers("Accept-Enconding"    , "gzip, identity")
                .headers("Accept-Language"     , "en")
                .headers("User-Agent"          , "public-api-example-client")
                .headers("Content-Type"        , "application/x-www-form-urlencoded")
                .headers("X-API-Authentication", apiToken);

        for (String header : additionalHeaders.keySet()){
            requestBuilder.header(header, additionalHeaders.get(header));
        }

        HttpRequest request = requestBuilder.build();

        return request(request);
    }

    private static String request(HttpRequest request) throws Exception {
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        int responseStatusCode = response.statusCode();
        if (responseStatusCode != HTTP_STATUS_OK){
            throw new Exception(String.format("Request to Public API failed (Status: %d, Message: %s).",
                    responseStatusCode, response.body()));
        }

        return response.body();
    }

    public static HttpRequest.BodyPublisher getUrlEncodedFormData(Map<Object, Object> data) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }

            builder.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
        }

        return HttpRequest.BodyPublishers.ofString(builder.toString());
    }
}
