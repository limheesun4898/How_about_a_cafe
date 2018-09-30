package com.example.user.how_about_a_cafe;

class ListItem {
    int image;
    String title;
    String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public ListItem(int image, String title, String url) {
        this.image = image;
        this.title = title;
        this.url = url;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
