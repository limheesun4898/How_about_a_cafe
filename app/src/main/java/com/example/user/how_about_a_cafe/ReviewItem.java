package com.example.user.how_about_a_cafe;

public class ReviewItem {
    private String review;
    private String rating;
    private String url;
    private boolean isimage;

    ReviewItem() {

    }

    ReviewItem(String review, String rating, boolean isimage) {
        this.review = review;
        this.rating = rating;
        this.isimage = isimage;
    }

    ReviewItem(String review, String rating, String url, boolean isimage) {
        this.review = review;
        this.rating = rating;
        this.url = url;
        this.isimage = isimage;
    }

    public String getReview() {
        return this.review;
    }

    public String getRating() {
        return this.rating;
    }

    public String getUrl() {return this.url;}

    public boolean isIsimage() {return this.isimage;}
}
