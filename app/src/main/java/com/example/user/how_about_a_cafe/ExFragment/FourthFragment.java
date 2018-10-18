package com.example.user.how_about_a_cafe.ExFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;
import com.example.user.how_about_a_cafe.R;

public class FourthFragment extends android.support.v4.app.Fragment {
    public static LottieAnimationView fourth_lottie;

    public FourthFragment () {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.activity_fourth_fragment, container, false);
        fourth_lottie = view.findViewById(R.id.fourth_lottie);
        fourth_lottie.playAnimation();
        fourth_lottie.loop(true);

        return view;
    }
}
