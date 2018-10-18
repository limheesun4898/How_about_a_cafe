package com.example.user.how_about_a_cafe.ExFragment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;
import com.example.user.how_about_a_cafe.R;

public class SecondFragment extends android.support.v4.app.Fragment {
    public static LottieAnimationView second_lottie;

    public SecondFragment () {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.activity_second_fragment, container, false);
        second_lottie = view.findViewById(R.id.second_lottie);
        second_lottie.playAnimation();
        second_lottie.loop(true);

        return view;
    }
}
