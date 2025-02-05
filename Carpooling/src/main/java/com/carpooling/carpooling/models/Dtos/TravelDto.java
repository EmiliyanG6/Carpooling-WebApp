package com.carpooling.carpooling.models.Dtos;

import com.carpooling.carpooling.models.Travel;

import java.time.LocalDateTime;

public class TravelDto {

    private String startingPoint;
    private String endingPoint;
    private LocalDateTime departureTime;
    private int freeSpots;
    private String comment;

    public TravelDto(Travel travel) {
        this.startingPoint = travel.getStartPoint();
        this.endingPoint = travel.getEndPoint();
        this.departureTime = travel.getDepartureTime();
        this.freeSpots = travel.getFreeSpots();
        this.comment = travel.getComment();
    }
    public TravelDto() {

    }

    public String getStartingPoint() {
        return startingPoint;
    }

    public void setStartingPoint(String startingPoint) {
        this.startingPoint = startingPoint;
    }

    public String getEndingPoint() {
        return endingPoint;
    }

    public void setEndingPoint(String endingPoint) {
        this.endingPoint = endingPoint;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public int getFreeSpots() {
        return freeSpots;
    }

    public void setFreeSpots(int freeSpots) {
        this.freeSpots = freeSpots;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
