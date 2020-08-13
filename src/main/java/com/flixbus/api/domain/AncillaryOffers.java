package com.flixbus.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AncillaryOffers {

    private AncillaryOfferCollection luggage_additional;
    private AncillaryOfferCollection seat_reservation;

    public AncillaryOfferCollection getLuggage_additional() {
        return luggage_additional;
    }

    public void setLuggage_additional(AncillaryOfferCollection luggage_additional) {
        this.luggage_additional = luggage_additional;
    }

    public AncillaryOfferCollection getSeat_reservation() {
        return seat_reservation;
    }

    public void setSeat_reservation(AncillaryOfferCollection seat_reservation) {
        this.seat_reservation = seat_reservation;
    }
}
