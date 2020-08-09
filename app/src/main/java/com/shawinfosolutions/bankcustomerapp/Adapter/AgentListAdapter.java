package com.shawinfosolutions.bankcustomerapp.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.shawinfosolutions.bankcustomerapp.Model.BankAgents;
import com.shawinfosolutions.bankcustomerapp.Constant;
import com.shawinfosolutions.bankcustomerapp.Activity.ListOfAgentsActivity;
import com.shawinfosolutions.bankcustomerapp.MySingleton;
import com.shawinfosolutions.bankcustomerapp.R;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.MODE_PRIVATE;
import static com.facebook.react.bridge.UiThreadUtil.runOnUiThread;

public class AgentListAdapter extends RecyclerView.Adapter<AgentListAdapter.ViewHolder> {

    //private List<ColorData> mData;
    private LayoutInflater mInflater;
    private Context mContext;
    //    private ArrayList<Integer> imagesofPersons;
//    private ArrayList<String> nameofPersons;
//    private ArrayList<String> Status;
    private AlertDialog alertDialog;

    private String NOTIFICATION_TITLE;
    private String NOTIFICATION_MESSAGE;
    private String TOPIC;
    final String TAG = "NOTIFICATION TAG";
    private String tokenVal;
    ArrayList<BankAgents> agentsList;
    private String mobileNo;
    private String UserLoggedIn;
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;


    public AgentListAdapter(Context context, ArrayList<BankAgents> agentsList) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.agentsList = agentsList;
    }


    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.agent_item, parent, false);
        URL serverURL;

        try {
            serverURL = new URL("https://meet.jit.si");//test
            //serverURL = new URL("https://meet.ezycom.co.in/");
            //https://meet.ezycom.co.in/
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid server URL!");
        }
        JitsiMeetConferenceOptions defaultOptions
                = new JitsiMeetConferenceOptions.Builder()
                .setServerURL(serverURL)
                .setWelcomePageEnabled(false)
                .build();
        JitsiMeet.setDefaultConferenceOptions(defaultOptions);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        //  String animal = mData.get(position);
        byte[] encodeByte;
        encodeByte = Base64.decode(agentsList.get(position).getAgentPhoto(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        holder.PersonImg.setImageBitmap(bitmap); // From android.graphics.Color
        holder.NameTxt.setText("Name :" + agentsList.get(position).getAgentName());
        holder.addressTxt.setText("Mobile No :" + agentsList.get(position).getAgentmobileNo());
        Log.e("Position", "" + position);

        // Log.e("Nameeeee", "" + agentsList.get(position).getAgentName());

        if (agentsList.get(position).getAgentLoginStatus().equalsIgnoreCase("true")) {
            holder.statusVal.setBackgroundResource(R.drawable.green_dot); // From android.graphics.Color

        } else {
            holder.statusVal.setBackgroundResource(R.drawable.active_dot); // From android.graphics.Color

        }
        SharedPreferences prefs = mContext.getSharedPreferences("Fashion", MODE_PRIVATE);
        UserLoggedIn = prefs.getString("UserLoggedIn", "");//
        Log.e("UserLoggedIn", "" + UserLoggedIn);
        holder.salesAgentLayout.setTag(R.string.tag1, agentsList.get(position).getAgentToken());
        ;
        holder.salesAgentLayout.setTag(R.string.tag2, agentsList.get(position).getAgentmobileNo());
        ;

        holder.salesAgentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tokenVal = view.getTag(R.string.tag1).toString();
                mobileNo = view.getTag(R.string.tag2).toString();

                if (agentsList.get(position).getAgentLoginStatus().equalsIgnoreCase("true")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    ViewGroup viewGroup = view.findViewById(android.R.id.content);
                    View dialogView = LayoutInflater.from(mContext).inflate(R.layout.custom_dialog_for_call_sales, viewGroup, false);
                    String text = String.valueOf(java.util.UUID.randomUUID());
                    Log.e("GUID", "" + text);
                    Log.e("tokenVal===", "" + tokenVal);
                    // SharedPreferences prefs = mContext.getSharedPreferences("Fashion", MODE_PRIVATE);
                    //   tokenVal = prefs.getString("tokenVal", "");
                    Button joinBtn = dialogView.findViewById(R.id.joinBtn);
                    EditText editText = dialogView.findViewById(R.id.conferenceName);
                    joinBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            sharedpreferences = mContext.getSharedPreferences("Fashion", Context.MODE_PRIVATE);
                            editor = sharedpreferences.edit();
                            editor.putString("AgentMobileNo",mobileNo);
                            editor.apply();
                            editor.commit();
                            NOTIFICATION_TITLE = "New Customer Calling";
                            NOTIFICATION_MESSAGE = text;

                            TOPIC = "/topics/" + mobileNo; //topic has to match what the receiver subscribed to

                            JSONObject notification = new JSONObject();
                            JSONObject notifcationBody = new JSONObject();
                            try {
                                notifcationBody.put("title", NOTIFICATION_TITLE);
                                notifcationBody.put("message", NOTIFICATION_MESSAGE);
                                notifcationBody.put("key1", "agentList");
                                notifcationBody.put("key2", UserLoggedIn);

                                notification.put("to", TOPIC);
                                notification.put("data", notifcationBody);
                                Log.e(TAG, "notification: " + notification);
                                Log.e(TAG, "key2: " + UserLoggedIn);

                            } catch (JSONException e) {
                                Log.e(TAG, "onCreate: " + e.getMessage());
                            }
                            sendNotifications(notification);


                            runOnUiThread(new Runnable() {
                                public void run() {
                                    // UI code goes here


////////----------------To start Meeting------------------

                                    if (text.length() > 0) {
                                        // Build options object for joining the conference. The SDK will merge the default
                                        // one we set earlier and this one when joining.
                                        JitsiMeetConferenceOptions options
                                                = new JitsiMeetConferenceOptions.Builder()
                                                .setRoom(text)
                                                .build();
                                        // Launch the new activity with the given options. The launch() method takes care
                                        // of creating the required Intent and passing the options.
                                        JitsiMeetActivity.launch((ListOfAgentsActivity) mContext, options);
                                        alertDialog.dismiss();


                                    }

                                }
                            });
//                            FirebaseDatabase.getInstance().getReference().child("Agents").
//                                    child("agentToken").child(tokenVal).
//                                    addListenerForSingleValueEvent(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                            String usertoken = dataSnapshot.getValue(String.class);
//                                            Log.e("token", "usertoken" + FirebaseDatabase.getInstance().getReference().child("Agents").child("agentToken").child(tokenVal));
//                                            Log.e("tokenn", "usertoken1" + FirebaseDatabase.getInstance().getReference().child("Tokens").child(tokenVal));
//                                            Log.e("usertoken", "usertoken2" + usertoken);
//                                            Log.e("dataSnapshot", "usertoken3" + dataSnapshot.getValue());
//////----------------To start Meeting------------------
//
//                                            if (text.length() > 0) {
//                                                // Build options object for joining the conference. The SDK will merge the default
//                                                // one we set earlier and this one when joining.
//                                                JitsiMeetConferenceOptions options
//                                                        = new JitsiMeetConferenceOptions.Builder()
//                                                        .setRoom(text)
//                                                        .build();
//                                                // Launch the new activity with the given options. The launch() method takes care
//                                                // of creating the required Intent and passing the options.
//                                                JitsiMeetActivity.launch((ListOfAgentsActivity) mContext, options);
//                                                alertDialog.dismiss();
//
//
//                                            }
//
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                        }
//                                    });
                            // }
                        }

                    });

                    builder.setView(dialogView);
                    alertDialog = builder.create();
                    alertDialog.show();

                }
            }
        });

    }

    private void sendNotifications(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Constant.FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mContext, "Request error", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onErrorResponse: Didn't work");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", Constant.serverKey);
                params.put("Content-Type", Constant.contentType);
                return params;
            }
        };
        MySingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);
    }


    // total number of items
    @Override
    public int getItemCount() {
        return agentsList.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView PersonImg;
        TextView NameTxt, addressTxt;
        private LinearLayout statusVal;
        private LinearLayout salesAgentLayout;

        ViewHolder(View itemView) {
            super(itemView);
            PersonImg = itemView.findViewById(R.id.PersonImg);
            NameTxt = itemView.findViewById(R.id.NameTxt);
            addressTxt = itemView.findViewById(R.id.addressTxt);
            statusVal = itemView.findViewById(R.id.statusVal);
            salesAgentLayout = itemView.findViewById(R.id.salesAgentLayout);
        }
    }

//    private void sendNotification(JSONObject notification) {
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Constant.FCM_API, notification,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.i(TAG, "onResponse: " + response.toString());
//                        //edtTitle.setText("");
//                        //edtMessage.setText("");
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(mContext, "Request error", Toast.LENGTH_LONG).show();
//                        Log.i(TAG, "onErrorResponse: Didn't work");
//                    }
//                }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("Authorization", Constant.serverKey);
//                params.put("Content-Type", Constant.contentType);
//                return params;
//            }
//        };
//        MySingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);
//    }


}


