package com.organic.organicworld.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.organic.organicworld.databinding.ActivityAuthBinding;
import com.organic.organicworld.utils.UserPref;

import java.util.Objects;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AuthActivity extends AppCompatActivity {

    ActivityAuthBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserPref pref = new UserPref(this);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setStatusBarColor(Color.WHITE);
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.continueBtn.setOnClickListener(v -> {
            binding.authView.setVisibility(View.GONE);
            binding.nameFillUp.setVisibility(View.VISIBLE);
        });

        binding.signUpBtn.setOnClickListener(v -> {
            String _name = "";
            _name = Objects.requireNonNull(binding.nameEditText.getText()).toString();
            pref.saveName(_name);
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("userName", _name);
            startActivity(intent);
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.finish();
    }
}