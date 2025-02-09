package com.carpooling.carpooling.models.Dtos;

public class FeedbackDto {
    private int rating;
    private String comment;
    private String giverUsername;

    public FeedbackDto() {

    }
    public FeedbackDto(int rating, String comment, String giverUsername) {
        this.rating = rating;
        this.comment = comment;
        this.giverUsername = giverUsername;
    }

    public String getGiverUsername() {
        return giverUsername;
    }

    public void setGiverUsername(String giverUsername) {
        this.giverUsername = giverUsername;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
