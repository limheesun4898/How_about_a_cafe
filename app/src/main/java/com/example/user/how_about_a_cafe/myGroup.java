package com.example.user.how_about_a_cafe;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.RequestBuilder;

import java.util.ArrayList;

public class myGroup {
    public ArrayList<String> child;
    public ArrayList<String> childPrice;
    public ArrayList<Uri> childImage;
    public String groupName;
    myGroup(String name){
        groupName = name;
        child = new ArrayList<String>();
        childPrice = new ArrayList<String>();
        childImage = new ArrayList<Uri>();
    }

}
