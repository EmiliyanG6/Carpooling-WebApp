package com.carpooling.carpooling.models.Dtos;

import com.carpooling.carpooling.enums.TravelStatus;
import com.carpooling.carpooling.models.Travel;

import java.time.LocalDateTime;

public class TravelDto {

    private long id;
    private String startingPoint;
    private String endingPoint;
    private LocalDateTime departureTime;
    private int freeSpots;
    private String comment;
    private TravelStatus status;

    public TravelDto(Travel travel) {
        this.startingPoint = travel.getStartPoint();
        this.endingPoint = travel.getEndPoint();
        this.departureTime = travel.getDepartureTime();
        this.freeSpots = travel.getFreeSpots();
        this.comment = travel.getComment();
        this.status = travel.getStatus();
    }
    public TravelDto() {

    }

    public TravelStatus getStatus() {
        return status;
    }

    public void setStatus(TravelStatus status) {
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
