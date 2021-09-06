package com.espol.aguapol.ui.alarmas;

import android.graphics.PorterDuff;
import android.os.Bundle;

import com.espol.aguapol.R;
import com.espol.aguapol.databinding.ActivityAlarmsBinding;

import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;


import com.espol.aguapol.ui.alarmas.main.SectionsPagerAdapter;


public class AlarmsActivity extends AppCompatActivity {

    private ActivityAlarmsBinding binding;
    TabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding = ActivityAlarmsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        setUpLayaout();


    }

    private void setUpLayaout() {

        tabs.getTabAt(0).setIcon(R.drawable.activas);
        tabs.getTabAt(1).setIcon(R.drawable.proceso);
        tabs.getTabAt(2).setIcon(R.drawable.solucionadas);

        tabs.getTabAt(0).getIcon().setColorFilter(getResources().getColor(R.color.gris), PorterDuff.Mode.SRC_IN);
        tabs.getTabAt(1).getIcon().setColorFilter(getResources().getColor(R.color.gris), PorterDuff.Mode.SRC_IN);
        tabs.getTabAt(2).getIcon().setColorFilter(getResources().getColor(R.color.gris), PorterDuff.Mode.SRC_IN);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}