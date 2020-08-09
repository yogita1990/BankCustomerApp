package com.shawinfosolutions.bankcustomerapp.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.shawinfosolutions.bankcustomerapp.Model.Customer;
import com.shawinfosolutions.bankcustomerapp.Model.CustomerTokens;
import com.shawinfosolutions.bankcustomerapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginAsCustomerActivity extends AppCompatActivity {
    private Button submitBtn, ContinueBtn, SIgnInBtn;
    private String TAG = "LoginAsCust";
    private String mVerificationId = "";
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private FirebaseAuth mAuth;
    private EditText editTextMobile, edtVerificatonCode;
    private SharedPreferences sharedpreferences;
    private ProgressDialog dialog;
    private String loading_message = "Please wait...";
    public static final String OTP_REGEX = "[0-9]{1,6}";
    private static final int REQUEST_ALL_PERMISSION = 200;
    private SharedPreferences.Editor editor;
    private DatabaseReference mDatabase, mDatabaseAgnt;
    private String token = "";
    private DatabaseReference databaseref;
    private String TokenVal = "", MobileNoVal = "111";
    private ArrayList<String> custList,IDList,LoginStatusList;
    private String Id;
//    private String[] permissions = {
//
//            Manifest.permission.RECEIVE_SMS,
//            Manifest.permission.READ_SMS,
//            Manifest.permission.SEND_SMS};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        sharedpreferences = getSharedPreferences("Fashion", Context.MODE_PRIVATE);
        custList = new ArrayList<>();
        custList.clear();
        IDList=new ArrayList<>();
        IDList.clear();
        LoginStatusList=new ArrayList<>();
        LoginStatusList.clear();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            ActivityCompat.requestPermissions(this, permissions, REQUEST_ALL_PERMISSION);
//        } else {
//
//        }
        // mDatabase = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        editTextMobile = findViewById(R.id.editTextMobile);
        edtVerificatonCode = findViewById(R.id.edtVerificatonCode);
        submitBtn = findViewById(R.id.submitBtn);
        ContinueBtn = findViewById(R.id.ContinueBtn);
        SIgnInBtn = findViewById(R.id.SIgnInBtn);
        SIgnInBtn.setVisibility(View.GONE);
        submitBtn.setVisibility(View.GONE);
        edtVerificatonCode.setVisibility(View.GONE);
        editor = sharedpreferences.edit();

        databaseref = FirebaseDatabase.getInstance().getReference("CustomerTokens");
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( LoginAsCustomerActivity.this,
                new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        String newToken = instanceIdResult.getToken();
                        Log.e("newToken",newToken);

                    }
                });

        databaseref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                /// Log.e("key", "keyval" + dataSnapshot.getKey());
                if (dataSnapshot.exists()) {

                    HashMap<String, Object> dataMap = (HashMap<String, Object>) dataSnapshot.getValue();

                    for (String key : dataMap.keySet()) {

                        Object data = dataMap.get(key);

                        try {
                            HashMap<String, Object> userData = (HashMap<String, Object>) data;

                            CustomerTokens tokenList = new CustomerTokens(
                                    (String) userData.get("id"),(String) userData.get("mobileNo"), (String) userData.get("token"));
                            if (
                                    (tokenList.getId() != null && !tokenList.getId().isEmpty() && !tokenList.getId().equals("null")) &&
                                            (tokenList.getMobileNo() != null && !tokenList.getMobileNo().isEmpty() && !tokenList.getMobileNo().equals("null")) &&
                                            (tokenList.getToken() != null && !tokenList.getToken().isEmpty() && !tokenList.getToken().equals("null"))
                            ) {
                                Id = tokenList.getId();
                                IDList.add(Id);
                                MobileNoVal = tokenList.getMobileNo();
                                TokenVal = tokenList.getToken();
                                // Token token = new Token();

                                // token.setMobileNo(MobileNoVal);
                                // token.setToken(TokenVal);
                                custList.add(MobileNoVal);
                                Log.e("IDDDD", "" + Id);
                                Log.e("MobileNoVal111", "" + MobileNoVal);
                                // MobileNoVal=custList.get()

                            }


                        } catch (ClassCastException cce) {
                            cce.printStackTrace();

                        }

                    }
                    Log.e("mobilelistSize", "Size" + custList.size());
                    Log.e("mobilelist", "Mobile==" + MobileNoVal);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        ContinueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                String mobile = editTextMobile.getText().toString().trim();
                edtVerificatonCode.setVisibility(View.VISIBLE);

                if (mobile.isEmpty() || mobile.length() < 10) {
                    editTextMobile.setError("Enter a valid mobile");
                    editTextMobile.requestFocus();
                    return;
                }
                FirebaseMessaging.getInstance().subscribeToTopic("/topics/"+mobile);
//Log.e("IDS","IDLIST"+IDList.get(custList.indexOf(editTextMobile.getText().toString())));
                editor.putString("UserLoggedIn", mobile);
                editor.commit();
                editor.apply();
                if (!(IDList.isEmpty())) {

                    if (custList.contains(editTextMobile.getText().toString())) {
                        Id = IDList.get(custList.indexOf(editTextMobile.getText().toString()));
                        String UserLoggedIn = sharedpreferences.getString("UserLoggedIn", "");//"No name defined" is the de
                        Log.e("Id", "Id===" + Id);

                        Log.e("listllll", "" + custList.size());

                        Log.e("MobileNoVal", "" + MobileNoVal);
                        //                            if (mobilelist.contains(editTextMobile.getText().toString())) {
                        edtVerificatonCode.setVisibility(View.GONE);
                        Customer cust = new Customer(Id, mobile, "true");

                        mDatabase = FirebaseDatabase.getInstance().getReference("Customers");

                        mDatabase.child(Id).setValue(cust);
                        Intent intent = new Intent(LoginAsCustomerActivity.this, CustomerMainActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        AsyncCallWS asyncetask = new AsyncCallWS(LoginAsCustomerActivity.this, "Please wait...", mobile);
                        asyncetask.execute();
                    }

                    // sendVerificationCode(mobile);

                }else {
                    AsyncCallWS asyncetask = new AsyncCallWS(LoginAsCustomerActivity.this, "Please wait...", mobile);
                    asyncetask.execute();
                }
            }
        });


        SIgnInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = edtVerificatonCode.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    edtVerificatonCode.setError("Enter valid code");
                    edtVerificatonCode.requestFocus();
                    return;
                }

                //verifying the code entered manually
                verifyVerificationCode(code);
            }
        });
    }


    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncCallWS extends AsyncTask<Void, Void, Void> {
        Context context;
        String str;
        String mobile;

        public AsyncCallWS(Context activity, String str, String mobile) {
            this.context = activity;
            this.str = str;
            this.mobile = mobile;

            // dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(context);
            if (dialog != null && !loading_message.equalsIgnoreCase("")) {
                dialog.setMessage(loading_message);
                dialog.setCancelable(true);
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.setProgress(0);
                dialog.setMax(100);

                dialog.show();
            }

        }


        @Override
        protected Void doInBackground(Void... voids) {
            sendVerificationCode(mobile);
            return null;
        }

        @Override

        protected void onPostExecute(Void aVoid) {
            try {
                super.onPostExecute(aVoid);
                dialog.dismiss();
                SIgnInBtn.setVisibility(View.VISIBLE);
                ContinueBtn.setVisibility(View.GONE);
//                SmsReceiver.bindListener(new SmsListener() {
//                    @Override
//                    public void messageReceived(String messageText) {
//
//                        //From the received text string you may do string operations to get the required OTP
//                        //It depends on your SMS format
//                        Log.e("Message",messageText);
//                        Toast.makeText(LoginAsCustomerActivity.this,"Message: "+messageText,Toast.LENGTH_LONG).show();
//
//                        // If your OTP is six digits number, you may use the below code
//
//                        Pattern pattern = Pattern.compile(OTP_REGEX);
//                        Matcher matcher = pattern.matcher(messageText);
//                        String otp = null;
//                        while (matcher.find())
//                        {
//                            otp = matcher.group();
//                        }
//                        edtVerificatonCode.setText(otp);
//                        Toast.makeText(LoginAsCustomerActivity.this,"OTP: "+ otp ,Toast.LENGTH_LONG).show();
//
//                    }
//                });
            } catch (Exception ex) {
                ex.printStackTrace();
                Log.e("Error", ex.getMessage());
                Toast.makeText(LoginAsCustomerActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();

            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will

        switch (item.getItemId()) {
            case android.R.id.home:

                Intent intent = new Intent(LoginAsCustomerActivity.this, SplashScreenActivity.class);
                startActivity(intent);
                finish();

                //  finish();
                break;


        }
        return (super.onOptionsItemSelected(item));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LoginAsCustomerActivity.this, SplashScreenActivity.class);
        startActivity(intent);
        finish();
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(LoginAsCustomerActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mDatabase = FirebaseDatabase.getInstance().getReference("Customers");

                            Id = mDatabase.push().getKey();

                            Customer cust = new Customer(Id,editTextMobile.getText().toString(), "true");
                            // tokenn.setId(user.getUid());
                            //  tokenn.setToken(token);
                            // databaseref.child(id).setValue(tokenn);

                            //mDatabase.child("Customers").setValue(cust);
                            mDatabase.child(Id).setValue(cust);


                            mDatabaseAgnt = FirebaseDatabase.getInstance().getReference("CustomerTokens");
                            editor.putString("IsLoggedIn", "1");
                            editor.putString("UserID", Id);
                            editor.putString("MobileNo", editTextMobile.getText().toString());
                            editor.commit();
                            editor.apply();

                            //   onAuthSuccesss(task.getResult().getUser());

                            FirebaseInstanceId.getInstance().getInstanceId()
                                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                            if (!task.isSuccessful()) {
                                                Log.w("TAG", "getInstanceId failed", task.getException());
                                                return;
                                            }

                                            // Get new Instance ID token
                                            token = task.getResult().getToken().toString();
                                            Log.e("Tokenmsg", "TokenVal" + token);
                                            editor.putString("tokenVal", token);
                                            editor.commit();
                                            //  id = databaseref.push().getKey().toString();
                                            Log.e("IDVAL==", "" + Id);
                                            Log.e("token==", "" + token);
                                            Log.e("Mobie==", "" + editTextMobile.getText().toString());
                                            //  FirebaseDatabase.getInstance().reference.child("users/${user.uid}/tokens").child(token).setValue(true)
                                            // mDatabase = FirebaseDatabase.getInstance().getReference("CustomerTokens");

                                            CustomerTokens tokenn = new CustomerTokens(Id, editTextMobile.getText().toString(),token);
                                            // tokenn.setId(user.getUid());
                                            //  tokenn.setToken(token);
                                            // databaseref.child(id).setValue(tokenn);
                                            mDatabaseAgnt.child(Id).setValue(tokenn);
                                            // mDatabase.child("Tokens").setValue(tokenn);
                                            // Toast.makeText(LoginAsCustomerActivity.this, "Artist added", Toast.LENGTH_LONG).show();
                                            //test


                                        }
                                    });
                            Intent intent = new Intent(LoginAsCustomerActivity.this, CustomerMainActivity.class);
                            startActivity(intent);
                            finish();

                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }
                            Log.e("Error", "Error" + message);
                            Toast.makeText(LoginAsCustomerActivity.this, message, Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }


    @Override
    public void onStart() {
        super.onStart();

        // Add value event listener to the post
        // [START post_value_event_listener]
    }


    //the callback to detect the verifi acation status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();
            Log.e("SMSCODE", "" + code);
            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                edtVerificatonCode.setText(code);
                //verifying the code
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(LoginAsCustomerActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("Error111", "Error111" + e.getMessage());

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case REQUEST_ALL_PERMISSION:
//
//        }
    // }

}

