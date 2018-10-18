package com.example.user.how_about_a_cafe.ExFragment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;
import com.example.user.how_about_a_cafe.R;

public class LastFragment extends android.support.v4.app.Fragment {
    public static LottieAnimationView last_lottie;

    public LastFragment () {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.activity_last_fragment, container, false);
        last_lottie = view.findViewById(R.id.last_lottie);
        last_lottie.playAnimation();
        last_lottie.setImageAssetsFolder("images/");
        last_lottie.loop(true);

        return view;
    }
}
