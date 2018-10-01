package com.example.user.how_about_a_cafe;

public class ReviewItem {
    private String review;
    private String rating;

    ReviewItem() {

    }

    ReviewItem(String review, String rating) {
        this.review = review;
        this.rating = rating;
    }

    public String getReview() {
        return this.review;
    }

    public String getRating() {
        return this.rating;
    }
}
