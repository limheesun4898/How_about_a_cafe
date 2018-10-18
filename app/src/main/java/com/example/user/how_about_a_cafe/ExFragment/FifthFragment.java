package com.example.user.how_about_a_cafe.ExFragment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;
import com.example.user.how_about_a_cafe.R;

public class FifthFragment extends android.support.v4.app.Fragment {
    public static LottieAnimationView fifth_lottie;

    public FifthFragment () {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.activity_fifth_fragment, container, false);
        fifth_lottie = view.findViewById(R.id.fifth_lottie);
        fifth_lottie.playAnimation();
        fifth_lottie.loop(true);

        return view;
    }
}
