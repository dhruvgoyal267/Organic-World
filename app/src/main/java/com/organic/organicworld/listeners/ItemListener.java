package com.organic.organicworld.listeners;

import android.content.Intent;
import android.net.Uri;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import com.google.android.material.navigation.NavigationView;
import com.organic.organicworld.BuildConfig;
import com.organic.organicworld.R;
import com.organic.organicworld.databinding.ActivityHomeBinding;
import com.organic.organicworld.databinding.CustomUserMessageToUsBinding;
import com.organic.organicworld.utils.UtilityFunctions;
import com.organic.organicworld.viewmodels.ProductViewModel;
import com.organic.organicworld.views.fragments.ListItemsFragment;

public class ItemListener implements NavigationView.OnNavigationItemSelectedListener {

    ActivityHomeBinding binding;
    AppCompatActivity activity;
    ProductViewModel model;

    public ItemListener(ActivityHomeBinding binding, AppCompatActivity activity, ProductViewModel model) {
        this.binding = binding;
        this.activity = activity;
        this.model = model;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        if (item.getItemId() == R.id.showAll) {
            binding.appBar.toolbar.setTitle(R.string.all_categories);
            ListItemsFragment fragment = new ListItemsFragment();
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment, "Categories")
                    .addToBackStack("Home")
                    .commit();
        } else if (item.getItemId() == R.id.share) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Organic World");
            String shareMessage = "No need to search organic products for long hours on Internet.\n\n\nJust download the Organic World now and get access to the healthier alternatives of all the products that you use.\n\n\n\n";
            shareMessage += "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            activity.startActivity(Intent.createChooser(shareIntent, "Choose one"));
        } else if (item.getItemId() == R.id.rateApp) {
            Intent intent = null;
            try {
                intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market:?//details?id=${context?.packageName}"));
            } catch (Exception e) {
                intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=${context?.packageName}"));
            } finally {
                if (intent != null)
                    activity.startActivity(intent);
            }

        } else if (item.getItemId() == R.id.feedback) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            View view = activity.getLayoutInflater().inflate(R.layout.custom_user_message_to_us, null);
            builder.setView(view);
            AlertDialog alertDialog = builder.create();
            CustomUserMessageToUsBinding messageBinding = CustomUserMessageToUsBinding.bind(view);
            messageBinding.submitBtn.setOnClickListener(v -> {
                String message = messageBinding.userIssue.getText().toString();
                String email = messageBinding.userEmail.getText().toString();
                model.feedback(message, email).observe(activity, aBoolean -> {
                    if (aBoolean != null) {
                        if (aBoolean) {
                            alertDialog.dismiss();
                            UtilityFunctions.showToast(activity, "Thanks for telling us what you want.");
                        } else {
                            UtilityFunctions.showToast(activity, "Message and email both should not be empty");
                            messageBinding.userIssue.setError("");
                            messageBinding.userEmail.setError("");
                        }
                    }
                });
            });
            alertDialog.show();
            messageBinding.closeBtn.setOnClickListener(v -> alertDialog.dismiss());
        } else if (item.getItemId() == R.id.suggestProduct) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            View view = activity.getLayoutInflater().inflate(R.layout.custom_suggest_product, null);
            builder.setView(view);
            AlertDialog alertDialog = builder.create();
            com.organic.organicworld.databinding.CustomSuggestProductBinding suggestProductBinding = com.organic.organicworld.databinding.CustomSuggestProductBinding.bind(view);
            suggestProductBinding.submitBtn.setOnClickListener(v -> {
                String productName = suggestProductBinding.productName.getText().toString();
                model.suggest(productName).observe(activity, aBoolean -> {
                    if (aBoolean != null) {
                        if (aBoolean) {
                            alertDialog.dismiss();
                            UtilityFunctions.showToast(activity, "Thanks for suggesting product we will add it soon.");
                        } else
                            suggestProductBinding.productName.setError("Kindly! put the name of the product");
                    }
                });
            });
            alertDialog.show();
            suggestProductBinding.closeBtn.setOnClickListener(v -> alertDialog.dismiss());
        }
        return true;
    }
}
