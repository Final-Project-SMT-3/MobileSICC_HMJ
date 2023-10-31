package com.example.sicc.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sicc.R;
import com.example.sicc.fragments.HomeFragment;
import com.example.sicc.fragments.ProgressFragment;
import com.example.sicc.fragments.SettingFragment;

public class MainActivity extends AppCompatActivity {
    private LinearLayout homeLayout, timelineLayout, saveLayout, settingLayout;
    private ImageView home, timeline, save, setting;
    private TextView txtHome, txtTimeline, txtSave, txtSetting;
    private int selectedTab = 1;
    private long backPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        homeLayout = findViewById(R.id.home_layout);
        timelineLayout = findViewById(R.id.timeline_layout);
        saveLayout = findViewById(R.id.bookmark_layout);
        settingLayout = findViewById(R.id.setting_layout);

        home = findViewById(R.id.icon_home);
        timeline = findViewById(R.id.icon_timeline);
        save = findViewById(R.id.icon_save);
        setting = findViewById(R.id.icon_setting);

        txtHome = findViewById(R.id.text_home);
        txtTimeline = findViewById(R.id.text_timeline);
        txtSave = findViewById(R.id.text_save);
        txtSetting = findViewById(R.id.text_setting);

        getSupportFragmentManager().beginTransaction().setReorderingAllowed(true)
                        .replace(R.id.fragment_container, HomeFragment.class, null)
                        .addToBackStack(null)
                        .commit();

        homeLayout.setOnClickListener(v-> {
            if (selectedTab != 1) {
                txtTimeline.setVisibility(View.GONE);
                txtSave.setVisibility(View.GONE);
                txtSetting.setVisibility(View.GONE);

                timelineLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                saveLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                settingLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                txtHome.setVisibility(View.VISIBLE);
                homeLayout.setBackgroundResource(R.drawable.round_hover);

                getSupportFragmentManager().beginTransaction().setReorderingAllowed(true)
                        .replace(R.id.fragment_container, HomeFragment.class, null)
                        .addToBackStack(null)
                        .commit();

                ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                scaleAnimation.setDuration(200);
                scaleAnimation.setFillAfter(true);
                homeLayout.startAnimation(scaleAnimation);

                selectedTab = 1;
            }
        });

        timelineLayout.setOnClickListener(v-> {
            if (selectedTab != 2) {
                txtHome.setVisibility(View.GONE);
                txtSave.setVisibility(View.GONE);
                txtSetting.setVisibility(View.GONE);

                homeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                saveLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                settingLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                txtTimeline.setVisibility(View.VISIBLE);
                timelineLayout.setBackgroundResource(R.drawable.round_hover);

                getSupportFragmentManager().beginTransaction().setReorderingAllowed(true)
                        .replace(R.id.fragment_container, ProgressFragment.class, null)
                        .addToBackStack(null)
                        .commit();

                ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                scaleAnimation.setDuration(200);
                scaleAnimation.setFillAfter(true);
                timelineLayout.startAnimation(scaleAnimation);

                selectedTab = 2;
            }
        });

        saveLayout.setOnClickListener(v-> {
            if (selectedTab != 3) {
                txtHome.setVisibility(View.GONE);
                txtTimeline.setVisibility(View.GONE);
                txtSetting.setVisibility(View.GONE);

                homeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                timelineLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                settingLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                txtSave.setVisibility(View.VISIBLE);
                saveLayout.setBackgroundResource(R.drawable.round_hover);

                ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                scaleAnimation.setDuration(200);
                scaleAnimation.setFillAfter(true);
                saveLayout.startAnimation(scaleAnimation);

                selectedTab = 3;
            }
        });

        settingLayout.setOnClickListener(v-> {
            if (selectedTab != 4) {
                txtHome.setVisibility(View.GONE);
                txtTimeline.setVisibility(View.GONE);
                txtSave.setVisibility(View.GONE);

                homeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                timelineLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                saveLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                txtSetting.setVisibility(View.VISIBLE);
                settingLayout.setBackgroundResource(R.drawable.round_hover);

                getSupportFragmentManager().beginTransaction().setReorderingAllowed(true)
                        .replace(R.id.fragment_container, SettingFragment.class, null)
                        .addToBackStack(null)
                        .commit();

                ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                scaleAnimation.setDuration(200);
                scaleAnimation.setFillAfter(true);
                settingLayout.startAnimation(scaleAnimation);

                selectedTab = 4;
            }
        });
    }

    public void setButtonActive(int tab) {
        switch (tab) {
            case 1:
                txtTimeline.setVisibility(View.GONE);
                txtSave.setVisibility(View.GONE);
                txtSetting.setVisibility(View.GONE);

                timelineLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                saveLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                settingLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                txtHome.setVisibility(View.VISIBLE);
                homeLayout.setBackgroundResource(R.drawable.round_hover);

                selectedTab = 1;
                break;

            case 2:
                txtHome.setVisibility(View.GONE);
                txtSave.setVisibility(View.GONE);
                txtSetting.setVisibility(View.GONE);

                homeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                saveLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                settingLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                txtTimeline.setVisibility(View.VISIBLE);
                timelineLayout.setBackgroundResource(R.drawable.round_hover);

                selectedTab = 2;
                break;

            case 3:
                txtHome.setVisibility(View.GONE);
                txtTimeline.setVisibility(View.GONE);
                txtSetting.setVisibility(View.GONE);

                homeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                timelineLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                settingLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                txtSave.setVisibility(View.VISIBLE);
                saveLayout.setBackgroundResource(R.drawable.round_hover);

                selectedTab = 3;
                break;

            case 4:
                txtHome.setVisibility(View.GONE);
                txtTimeline.setVisibility(View.GONE);
                txtSave.setVisibility(View.GONE);

                homeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                timelineLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                saveLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                txtSetting.setVisibility(View.VISIBLE);
                settingLayout.setBackgroundResource(R.drawable.round_hover);

                selectedTab = 4;
                break;

            default:
                Toast.makeText(MainActivity.this, "Mohon Maaf Tab Tidak DI Temukan", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();

        if (currentTime - backPressedTime > 3500) {
            Toast.makeText(this, "Tekan Sekali Lagi Untuk Keluar", Toast.LENGTH_SHORT).show();
            backPressedTime = currentTime;
        } else {
            super.onBackPressed();
            finish();
        }
    }
}