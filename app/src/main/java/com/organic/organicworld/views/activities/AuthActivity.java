package com.organic.organicworld.views.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import com.organic.organicworld.databinding.ActivityAuthBinding;
import com.organic.organicworld.utils.UserPref;

import java.util.Objects;

public class AuthActivity extends AppCompatActivity {

    ActivityAuthBinding binding;
    UserPref pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = new UserPref(this);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setStatusBarColor(Color.WHITE);
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.continueBtn.setOnClickListener(v -> {
            binding.authView.setVisibility(View.GONE);
            binding.nameFillUp.setVisibility(View.VISIBLE);
        });

        binding.signUpBtn.setOnClickListener(v -> {
            binding.loader.setVisibility(View.VISIBLE);
            binding.spin.setIndeterminate(true);
            final String _name = Objects.requireNonNull(binding.nameEditText.getText()).toString();
            Handler handler = new Handler(Looper.getMainLooper());
            Runnable runnable = () -> saveName(_name);
            handler.postDelayed(runnable, 1000);
        });
    }

    private void saveName(String name) {
        boolean res = pref.saveName(name);
        if (res) {
            Intent intent = new Intent(AuthActivity.this, HomeActivity.class);
            intent.putExtra("userName", name);
            startActivity(intent);
        } else {
            binding.loader.setVisibility(View.GONE);
            binding.nameEditText.setError("Kindly provide your name");
        }
    }

    @Override
    protected void onStop() {
        binding.loader.setVisibility(View.GONE);
        super.onStop();
        this.finish();
    }
}