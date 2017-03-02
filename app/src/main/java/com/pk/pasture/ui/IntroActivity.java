package com.pk.pasture.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.pk.pasture.R;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.MessageButtonBehaviour;
import agency.tango.materialintroscreen.SlideFragment;
import agency.tango.materialintroscreen.SlideFragmentBuilder;


public class IntroActivity extends MaterialIntroActivity {

    /**
     * Duration of wait
     **/
    private final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SlideFragmentBuilder mSlideFragmentBuilder = new SlideFragmentBuilder()
                .backgroundColor(R.color.colorPrimaryDark)
                .buttonsColor(R.color.colorPrimary);
        if(Build.VERSION.SDK_INT >= 23)
            mSlideFragmentBuilder
                    .possiblePermissions(new String[]{ Manifest.permission.ACCESS_COARSE_LOCATION})
                    .neededPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET});
        final SlideFragment mSlider = mSlideFragmentBuilder
                .title("Pasture Map")
                .description("Draw pastures on google map")
                .build();
        addSlide(mSlider, new MessageButtonBehaviour(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Thread loading = new Thread() {
                            public void run() {
                                try {
                                    sleep(SPLASH_DISPLAY_LENGTH);
                                    Intent main = new Intent(IntroActivity.this, MapActivity.class);
                                    startActivity(main);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                } finally {
                                    finish();
                                }
                            }
                        };

                        loading.start();
                    }
                }, "Go")
        );
    }
}
