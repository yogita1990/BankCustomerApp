package com.shawinfosolutions.bankcustomerapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shawinfosolutions.bankcustomerapp.Model.Customer;
import com.shawinfosolutions.bankcustomerapp.R;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class CustomerMainActivity extends AppCompatActivity {
    private GridView gridView;
    private LinearLayout mumbaiLayout, naviMumbaiLay;
    String[] city = {
            "Mumbai",
            "Navi Mumbai",
            "Thane",
            "Palghar",
            "Vijaywada",
            "Pune",
            "Ahmedabad",
            "Surat",
            "Vadod ara",
            "Delhi"

    };
    int[] imageId = {
            R.drawable.mumbai,
            R.drawable.navimumbai,
            R.drawable.thane,
            R.drawable.palghar,
            R.drawable.vijaywada,
            R.drawable.pune,
            R.drawable.ahamadabad,
            R.drawable.surat,
            R.drawable.vadodra,
            R.drawable.delhi

    };
    private ActionBar actionbar;
    private TextInputEditText pincode;
    private DatabaseReference mDatabase;
    private String MobileNo = "";
    private String loginStatus = "";
    private String UserID,UserLoggedInStatus;
    private String UserLoggedIn;
    private ArrayList<String> loginStatusList,UserIDList,mobileList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        actionbar = getSupportActionBar();
        actionbar.setTitle("Select Your City");

        //actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);
        mumbaiLayout = findViewById(R.id.mumbaiLayout);
        naviMumbaiLay = findViewById(R.id.naviMumbaiLay);
        pincode = findViewById(R.id.pincode);
        SharedPreferences prefs = getSharedPreferences("Fashion", MODE_PRIVATE);
        String IsLoggedIn = prefs.getString("IsLoggedIn", "");//"No name defined" is the de
        UserID = prefs.getString("UserID", "");
        MobileNo = prefs.getString("MobileNo", "");
        UserLoggedIn = prefs.getString("UserLoggedIn", "");
        UserLoggedInStatus=prefs.getString("UserLoggedInStatus", "");
        Log.e("UserLoggedIn","UserLoggedIn="+UserLoggedIn);
        loginStatusList=new ArrayList<>();
        loginStatusList.clear();
        UserIDList=new ArrayList<>();
        UserIDList.clear();
        mobileList=new ArrayList<>();
        mobileList.clear();
        // mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference("Customers");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    HashMap<String, Object> dataMap = (HashMap<String, Object>) dataSnapshot.getValue();

                    for (String key : dataMap.keySet()) {

                        Object data = dataMap.get(key);

                        try {
                            HashMap<String, Object> userData = (HashMap<String, Object>) data;

                            Customer mUser = new Customer((String) userData.get("id"), (String) userData.get("mobileNo"), (String) userData.get("loginStatus"));
                            if (
                                    (mUser.getId() != null && !mUser.getId().isEmpty() && !mUser.getId().equals("null")) &&
                                            (mUser.getLoginStatus() != null && !mUser.getLoginStatus().isEmpty() && !mUser.getLoginStatus().equals("null")) &&
                                            (mUser.getMobileNo() != null && !mUser.getMobileNo().isEmpty() &&
                                                    !mUser.getMobileNo().equals("null"))) {
                                // UserID= mUser.getId();
                                MobileNo = mUser.getMobileNo();
                                loginStatus = mUser.getLoginStatus();
                                UserID= mUser.getId();
                                loginStatusList.add(mUser.getLoginStatus());
                                UserIDList.add(UserID);
                                mobileList.add(MobileNo);



                            }
//addTextToView(mUser.getName() + " - " + Integer.toString(mUser.getAge()));

                        } catch (ClassCastException cce) {
                            cce.printStackTrace();
// If the object can’t be casted into HashMap, it means that it is of type String. 

                        }

                    }

                    Log.e("mobile", "" + MobileNo);

                    Log.e("UserID", "" + UserID);
                    Log.e("loginStatus", "" + loginStatus);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        pincode.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                //  adapter.getFilter().filter(cs);

//                Log.e("Size",""+cs);
//                Log.e("Size1",""+arg1);
//                Log.e("Size2",""+arg2);
//                Log.e("Size3",""+arg3);
                String SizeOfString = String.valueOf(arg1);
                if (SizeOfString.equalsIgnoreCase("5")) {
                    Intent intent = new Intent(CustomerMainActivity.this, StoreLocationActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                //  Toast.makeText(getApplicationContext(),"before text change",Toast.LENGTH_LONG).show();
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                //  Toast.makeText(getApplicationContext(),"after text change",Toast.LENGTH_LONG).show();
            }
        });
        mumbaiLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CustomerMainActivity.this, StoreLocationActivity.class);
                intent.putExtra("selectedVal", "Mumbai");
                startActivity(intent);
                finish();
            }
        });
        naviMumbaiLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(CustomerMainActivity.this, StoreLocationActivity.class);
//                intent.putExtra("selectedVal", "Navi Mumbai");
//                startActivity(intent);
//                finish();



            }


        });
//        gridView=findViewById(R.id.gridView);
//        CityGridAdapter adapter = new CityGridAdapter(MainActivity.this, city, imageId);
//        gridView.setAdapter(adapter);
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//
//                Toast.makeText(MainActivity.this, "You Clicked at " +city[+ position], Toast.LENGTH_SHORT).show();
//
//            }
//        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will

        switch (item.getItemId()) {
            case android.R.id.home:

                Intent intent = new Intent(CustomerMainActivity.this, SplashScreenActivity.class);
                startActivity(intent);
                finish();

                //  finish();
                break;

            case R.id.logout:
                FirebaseAuth.getInstance().signOut();

                SharedPreferences sharedpreferences = getSharedPreferences("Fashion", Context.MODE_PRIVATE);

                MobileNo=mobileList.get(mobileList.indexOf(UserLoggedIn));
                UserID=UserIDList.get(mobileList.indexOf(UserLoggedIn));
                String LoginStatus=loginStatusList.get(mobileList.indexOf(UserLoggedIn));



                Log.e("Mobilno","Mob"+MobileNo);
                Log.e("UserID","UserID"+UserID);
                Log.e("LoginStatus","LoginStatus"+LoginStatus);

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("IsLoggedIn", "0");
                editor.commit();
                editor.apply();
                Customer cust = new Customer(UserID, MobileNo, "false");


                mDatabase.child(UserID).setValue(cust);

                Intent intent1 = new Intent(CustomerMainActivity.this, SplashScreenActivity.class);
                startActivity(intent1);
                finish();


                break;


        }
        return (super.onOptionsItemSelected(item));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CustomerMainActivity.this, SplashScreenActivity.class);
        startActivity(intent);
        finish();
    }
}
