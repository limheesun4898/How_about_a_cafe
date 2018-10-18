package com.example.user.how_about_a_cafe;

import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.user.how_about_a_cafe.ExFragment.FifthFragment;
import com.example.user.how_about_a_cafe.ExFragment.FirstFragment;
import com.example.user.how_about_a_cafe.ExFragment.FourthFragment;
import com.example.user.how_about_a_cafe.ExFragment.LastFragment;
import com.example.user.how_about_a_cafe.ExFragment.SecondFragment;
import com.example.user.how_about_a_cafe.ExFragment.SixthFragment;
import com.example.user.how_about_a_cafe.ExFragment.ThirdFragment;

import static com.example.user.how_about_a_cafe.ExFragment.FirstFragment.first_lottie;
import static com.example.user.how_about_a_cafe.ExFragment.SecondFragment.second_lottie;
import static com.example.user.how_about_a_cafe.ExFragment.SixthFragment.a;
import static com.example.user.how_about_a_cafe.ExFragment.ThirdFragment.third_lottie;
import static com.example.user.how_about_a_cafe.ExFragment.FourthFragment.fourth_lottie;
import static com.example.user.how_about_a_cafe.ExFragment.FifthFragment.fifth_lottie;
import static com.example.user.how_about_a_cafe.ExFragment.SixthFragment.sixth_lottie;
import static com.example.user.how_about_a_cafe.ExFragment.LastFragment.last_lottie;

public class ViewPager extends AppCompatActivity {
    private android.support.v4.view.ViewPager vp;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);

        vp = (android.support.v4.view.ViewPager) findViewById(R.id.viewpager);
        fab = (FloatingActionButton) findViewById(R.id.first_next_fab);

        vp.setAdapter(new pagerAdapter(getSupportFragmentManager()));
        vp.setCurrentItem(0);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vp.setCurrentItem(getItem(+1));
                switch (vp.getCurrentItem()) {
                    case 1:
                        first_lottie.playAnimation();
                        break;
                    case 2:
                        second_lottie.playAnimation();
                        break;
                    case 3:
                        third_lottie.playAnimation();
                        break;
                    case 4:
                        fourth_lottie.playAnimation();
                        break;
                    case 5:
                        fifth_lottie.playAnimation();
                        break;
                    case 6:
                        Log.d("intintint", String.valueOf(a));
                        case7();
                        break;
                    case 7:
                        last_lottie.playAnimation();
                        break;

                }
            }
        });
    }
    private int getItem(int i) {
        return vp.getCurrentItem() + i;
    }

    private class pagerAdapter extends FragmentPagerAdapter {
        pagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new FirstFragment();
                case 1:
                    return new SecondFragment();
                case 2:
                    return new ThirdFragment();
                case 3:
                    return new FourthFragment();
                case 4:
                    return new FifthFragment();
                case 5:
                    return new SixthFragment();
                case 6:
                    return new LastFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 7;
        }
    }

    private void case7() {
        fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_black_24dp));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int infoFirst = 1;
                SharedPreferences a = getSharedPreferences("a", MODE_PRIVATE);
                SharedPreferences.Editor editor = a.edit();
                editor.putInt("First", infoFirst);
                editor.commit();
                finish();
            }
        });
    }
}
