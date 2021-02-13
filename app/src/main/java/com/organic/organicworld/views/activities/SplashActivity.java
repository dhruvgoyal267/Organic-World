package com.organic.organicworld.views.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.auth.api.Auth;
import com.organic.organicworld.R;
import com.organic.organicworld.databinding.ActivitySplashBinding;
import com.organic.organicworld.utils.UserPref;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class SplashActivity extends AppCompatActivity {

    ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setStatusBarColor(Color.WHITE);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Handler handler = new Handler(Looper.getMainLooper());
        Runnable runnable = this::isNameSaved;
        handler.postDelayed(runnable, 2000);
    }

    private void isNameSaved() {
        UserPref pref = new UserPref(this);
        Flowable<String> name = pref.getUserName();
        name.toObservable().subscribeOn(Schedulers.io())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull String s) {
                        Intent intent;
                        if (!s.isEmpty()) {
                            intent = new Intent(SplashActivity.this, HomeActivity.class);
                            intent.putExtra("userName", s);
                        } else {
                            intent = new Intent(SplashActivity.this, AuthActivity.class);
                        }
                        startActivity(intent);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Intent intent;
                        intent = new Intent(SplashActivity.this, AuthActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }

    @Override
    protected void onStop() {
        super.onStop();
        this.finish();
    }
}