package com.shawinfosolutions.bankcustomerapp.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shawinfosolutions.bankcustomerapp.Model.CustomerDetails;
import com.shawinfosolutions.bankcustomerapp.R;
import com.shawinfosolutions.bankcustomerapp.VerhoeffAlgorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class EKYCActivity extends AppCompatActivity {
    private EditText firstname, lastname, email, aadharNo, mobile;
    private Button submitBtn;
    private ActionBar actionbar;
    private DatabaseReference mDatabase;
    private ArrayList<String> aadharList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ekyc_activity);
        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        email = findViewById(R.id.email);
        aadharNo = findViewById(R.id.aadharNo);
        mobile = findViewById(R.id.mobile);
        submitBtn = findViewById(R.id.submitBtn);
        actionbar = getSupportActionBar();
        actionbar.setTitle("Start Your E-KYC");
        aadharList = new ArrayList<>();
        aadharList.clear();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);
        mDatabase = FirebaseDatabase.getInstance().getReference("CustomerDetails");

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            /// Log.e("key", "keyval" + dataSnapshot.getKey());
                            if (dataSnapshot.exists()) {

                                HashMap<String, Object> dataMap = (HashMap<String, Object>) dataSnapshot.getValue();

                                for (String key : dataMap.keySet()) {

                                    Object data = dataMap.get(key);

                                    try {
                                        HashMap<String, Object> userData = (HashMap<String, Object>) data;
                                        // aadharNo,firstname,id,lastname,mailId,mobilenumber;
                                        CustomerDetails tokenList = new CustomerDetails(
                                                (String) userData.get("id"), (String) userData.get("firstname"), (String) userData.get("lastname"),
                                                (String) userData.get("mailId"), (String) userData.get("aadharNo"), (String) userData.get("mobilenumber"));
                                        if (
                                                (tokenList.getId() != null && !tokenList.getId().isEmpty() && !tokenList.getId().equals("null")) &&
                                                        (tokenList.getFirstname() != null && !tokenList.getFirstname().isEmpty() && !tokenList.getFirstname().equals("null")) &&
                                                        (tokenList.getLastname() != null && !tokenList.getLastname().isEmpty() && !tokenList.getLastname().equals("null")) &&
                                                        (tokenList.getMailId() != null && !tokenList.getMailId().isEmpty() && !tokenList.getMailId().equals("null")) &&
                                                        (tokenList.getMobilenumber() != null && !tokenList.getMobilenumber().isEmpty() && !tokenList.getMobilenumber().equals("null")) &&
                                                        (tokenList.getAadharNo() != null && !tokenList.getAadharNo().isEmpty() && !tokenList.getAadharNo().equals("null"))
                                        ) {
                                            aadharList.add(tokenList.getAadharNo());
                                            // MobileNoVal=custList.get()

                                        }


                                    } catch (ClassCastException cce) {
                                        cce.printStackTrace();

                                    }

                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });



                        if (ValidateData()) {
                        SharedPreferences prefs = getSharedPreferences("Fashion", MODE_PRIVATE);
                        String UserID = prefs.getString("UserID", "");

                        CustomerDetails tokenn = new CustomerDetails(UserID, firstname.getText().toString(), lastname.getText().toString(),
                                email.getText().toString(), aadharNo.getText().toString(), mobile.getText().toString());
                        // tokenn.setId(user.getUid());
                        //  tokenn.setToken(token);
                        // databaseref.child(id).setValue(tokenn);
                        mDatabase.child(UserID).setValue(tokenn);


                        Intent intent=new Intent(EKYCActivity.this,ListOfAgentsActivity.class);
                        startActivity(intent);


                }
            }
        });

    }

    private boolean ValidateData() {

        //  firstname,lastname,email,aadharNo,mobile;
        if (firstname.getText().toString().equalsIgnoreCase("")) {
            firstname.setError("Please enter firstname");
            return false;
        }  if (lastname.getText().toString().equalsIgnoreCase("")) {
            lastname.setError("Please enter lastname");
            return false;
        }  if (email.getText().toString().equalsIgnoreCase("")) {
            email.setError("Please enter E-mail Id");
            return false;
        } else if (!isValid(email)) {
            email.setError("Invalid Email Address");
            return false;
        }  if (aadharNo.getText().toString().equalsIgnoreCase("")) {
            aadharNo.setError("Please enter Aadhar Card number");
            return false;
        } else if (!validateAadharNumber(aadharNo.getText().toString())) {
            aadharNo.setError("Invalid Aadhar Card number");
            return false;
        } else if (aadharList.contains(aadharNo.getText().toString())) {
            aadharNo.setError("This Aadhar Card number is already exist");
            return false;
          //  Toast.makeText(getApplicationContext(), "This Aadhar number is already exist", Toast.LENGTH_LONG).show();
        }  if (mobile.getText().toString().equalsIgnoreCase("")) {
            mobile.setError("Please enter mobile number");
            return false;
        } else if (mobile.getText().toString().length() < 10) {
            mobile.setError("Please Enter valid  Mobile Number.");
            return false;
        } else if (!(mobile.getText().toString().startsWith("7") || mobile.getText().toString().startsWith("8") || mobile.getText().toString().startsWith("9"))) {
            mobile.setError("Please Enter valid  Mobile Number.");
            return false;
        }

        return true;
    }

    public static boolean validateAadharNumber(String aadharNumber) {
        Pattern aadharPattern = Pattern.compile("\\d{12}");
        boolean isValidAadhar = aadharPattern.matcher(aadharNumber).matches();
        if (isValidAadhar) {
            isValidAadhar = VerhoeffAlgorithm.validateVerhoeff(aadharNumber);
        }
        return isValidAadhar;
    }


    private boolean isValid(EditText edt_mail) {


        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (edt_mail == null)
            return false;
        return pat.matcher(edt_mail.getText().toString()).matches();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will

        switch (item.getItemId()) {
            case android.R.id.home:

                Intent intent = new Intent(EKYCActivity.this, StoreLocationActivity.class);
                startActivity(intent);
                finish();

                //  finish();
                break;

        }
        return (super.onOptionsItemSelected(item));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EKYCActivity.this, StoreLocationActivity.class);
        startActivity(intent);
        finish();
    }
}
