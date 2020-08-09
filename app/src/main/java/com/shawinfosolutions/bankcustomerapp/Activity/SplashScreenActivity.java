package com.shawinfosolutions.bankcustomerapp.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.shawinfosolutions.bankcustomerapp.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;
    private String IsLoggedIn = "", IsLoggedInSales = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);



//        try {
//          //  if (KeyManager.getSharedPreferenceBoolean(SplashActivity.this, "isLoggedIn", false)) {
//                if (getIntent().hasExtra("pushnotification")) {
//                    Intent intent = new Intent(this, PaymentActivity.class);
//                    startActivity(intent);
//                    finish();
//                } else {
//                    CheckLogin();
//                }
//           // } else {
////                Intent i = new Intent(SplashActivity.this, LoginActivity.class);
////                startActivity(i);
////                finish();
//          //  }
//
//        } catch (Exception e) {
//            CheckLogin();
//            e.printStackTrace();
//        }


        SharedPreferences prefs = getSharedPreferences("Fashion", MODE_PRIVATE);
        IsLoggedIn = prefs.getString("IsLoggedIn", "");
        IsLoggedInSales = prefs.getString("IsLoggedInSales", "");
        Log.e("IsLoggedIn", "" + IsLoggedIn);
        Log.e("IsLoggedInSales", "" + IsLoggedInSales);
//        FirebaseDatabase.getInstance().getReference().child("Tokens").
//                child(UserTB.getText().toString().trim()).child("token").
//                addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        String usertoken=dataSnapshot.getValue(String.class);
//                        sendNotifications(usertoken, CaseMap.Title.getText().toString().trim(),Message.getText().toString().trim());
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity

                if (IsLoggedIn.equalsIgnoreCase("1")) {
                    Intent intent = new Intent(SplashScreenActivity.this, CustomerMainActivity.class);
                    startActivity(intent);
                    finish();

                } /*else if (IsLoggedInSales.equalsIgnoreCase("1")) {
                    Intent intent = new Intent(SplashScreenActivity.this, OnlineCustomersListActivity.class);
                    startActivity(intent);
                    finish();
                }*/ else {
                    Intent i = new Intent(SplashScreenActivity.this, LoginAsCustomerActivity.class);
                    startActivity(i);
                    finish();

                }
                // close this activity
            }
        }, SPLASH_TIME_OUT);
    }

}
