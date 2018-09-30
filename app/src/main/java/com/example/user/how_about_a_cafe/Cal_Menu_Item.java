package com.example.user.how_about_a_cafe;

public class Cal_Menu_Item {
    private String menu_name;
    private boolean temper;
    private String size;
    private String price;
    private String cnt;

    public Cal_Menu_Item() {

    }

    public Cal_Menu_Item(String menu_name, boolean temper, String size, String price, String cnt) {
        this.menu_name = menu_name;
        this.temper = temper;
        this.size = size;
        this.price = price;
        this.cnt = cnt;
    }


    public String getMenu_name() {
        return this.menu_name;
    }

    public String getPrice() {
        return this.price;
    }

    public String getSize() {
        return this.size;
    }

    public boolean getTemper() {
        return this.temper;
    }

    public String getCnt() {
        return this.cnt;
    }
}
