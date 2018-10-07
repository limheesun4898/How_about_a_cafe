package com.example.user.how_about_a_cafe;

public class ReviewItem {
    private String review;
    private String rating;
    private String url;
    private String formatDate;
    private boolean isimage;
    private String profile_image;
    private String name;
    private String menu;
    private String cafe_name;
    private String selection;

    public ReviewItem() {

    }

    public ReviewItem(String review, String rating, String url, String formatDate, String name, String profile_image, String menu, String cafe_name, String selection, boolean isimage) {
        this.review = review;
        this.rating = rating;
        this.url = url;
        this.formatDate = formatDate;
        this.name = name;
        this.profile_image = profile_image;
        this.menu = menu;
        this.cafe_name = cafe_name;
        this.selection = selection;
        this.isimage = isimage;
    }

    public ReviewItem(String review, String rating, String formatDate, String name, String profile_image, String menu, String cafe_name, String selection, boolean isimage) {
        this.review = review;
        this.rating = rating;
        this.formatDate = formatDate;
        this.name = name;
        this.profile_image = profile_image;
        this.menu = menu;
        this.cafe_name = cafe_name;
        this.selection = selection;
        this.isimage = isimage;
    }

    public String getReview() {
        return this.review;
    }

    public String getRating() {
        return this.rating;
    }

    public String getUrl() {
        return this.url;
    }

    public String getFormatDate() {
        return this.formatDate;
    }

    public boolean isIsimage() {
        return this.isimage;
    }

    public String getName() {
        return this.name;
    }

    public String getCafe_name() {
        return this.cafe_name;
    }

    public String getSelection() {
        return this.selection;
    }

    public String getMenu() {
        return this.menu;
    }

    public String getProfile_image() {
        return this.profile_image;
    }
}
