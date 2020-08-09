package com.shawinfosolutions.bankcustomerapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shawinfosolutions.bankcustomerapp.Adapter.AgentListAdapter;
import com.shawinfosolutions.bankcustomerapp.Model.BankAgents;
import com.shawinfosolutions.bankcustomerapp.R;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ListOfAgentsActivity extends AppCompatActivity {

    private ActionBar actionbar;
    private RecyclerView agent_list;
    // private Integer [] imagesofPersons = {R.drawable.acotrrrr,R.drawable.actorr,R.drawable.actorrrr};
    // private String [] nameofPersons = {"aaaa","bbbb","cccc"};
    private ArrayList<Integer> imagesofPersons = new ArrayList<>();
    private ArrayList<String> nameofPersons = new ArrayList<>();
    private ArrayList<String> Status = new ArrayList<>();
    private AgentListAdapter agentListAdapter;
    private DatabaseReference mDatabase;
    private String StoreName = "", AgentName = "", AgentPhoto = "", Experties = "", Brand = "", AgentLoginStatus = "", AgentToken = "", MobileNo = "";
    ArrayList<BankAgents> agentsList = new ArrayList<>();
    private int ArraySize;
    private String ID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_agents);
        agent_list = findViewById(R.id.agent_list);
        actionbar = getSupportActionBar();
        actionbar.setTitle("Available Bank Agents");

        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);
        imagesofPersons.add(R.drawable.acotrrrr);
        imagesofPersons.add(R.drawable.actorr);
        imagesofPersons.add(R.drawable.actorrrr);


        //  mDatabase = FirebaseDatabase.getInstance().getReference("https://console.firebase.google.com/project/agentfashionapp/Agents");
        // FirebaseDatabase database = FirebaseDatabase.getInstance();

        // Firebase myFirebaseRef = new Firebase("https://console.firebase.google.com/project/agentfashionapp/"); //<---- How to find this
        //  myFirebaseRef = myFirebaseRef.child("Agents");
        // mDatabase = FirebaseDatabase.getInstance().getReference("https://console.firebase.google.com/project/agentfashionapp-c2fc0/database/agentfashionapp-c2fc0/data/Agents");

        // Retrieve my other app.
        // FirebaseApp app = FirebaseApp.getInstance("agentfashionapp");
// Get the database for the other app.
        // FirebaseDatabase secondaryDatabase = FirebaseDatabase.getInstance(app);
        // mDatabase = secondaryDatabase.getReference().child("Agents");
        mDatabase = FirebaseDatabase.getInstance().getReference("BankAgents");

        //Firebase.setAndroidContext(getApplicationContext());

        // mDatabase = new Firebase("https://agentfashionapp-c2fc0.firebaseio.com/Agents");  // DataBase Profile Link

        agentsList.clear();

        agent_list.setLayoutManager(new LinearLayoutManager(this));

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                agentsList.clear();
                if (dataSnapshot.exists()) {

                    HashMap<String, Object> dataMap = (HashMap<String, Object>) dataSnapshot.getValue();

                    ArraySize = dataMap.size();
                    // Log.e("Size111",""+ArraySize);

                    for (String key : dataMap.keySet()) {

                        Object data = dataMap.get(key);

                        try {
                            HashMap<String, Object> userData = (HashMap<String, Object>) data;
                            //    private String agentLoginStatus,agentName,agentPhoto,agentToken,agentmobileNo,branchName,id;
                            BankAgents agent = new BankAgents((String) userData.get("id"), (String) userData.get("branchName"), (String) userData.get("agentName")
                                    , (String) userData.get("agentmobileNo"), (String) userData.get("agentPhoto"),
                                    (String) userData.get("agentLoginStatus"), (String) userData.get("agentToken"));
                            if (
                                    (agent.getId() != null && !agent.getId().isEmpty() && !agent.getId().equals("null")) &&
                                            (agent.getBranchName() != null && !agent.getBranchName().isEmpty() && !agent.getBranchName().equals("null")) &&
                                            (agent.getAgentName() != null && !agent.getAgentName().isEmpty() && !agent.getAgentName().equals("null")) &&
                                            (agent.getAgentmobileNo() != null && !agent.getAgentmobileNo().isEmpty() && !agent.getAgentmobileNo().equals("null")) &&
                                            (agent.getAgentPhoto() != null && !agent.getAgentPhoto().isEmpty() && !agent.getAgentPhoto().equals("null")) &&
                                            (agent.getAgentLoginStatus() != null && !agent.getAgentLoginStatus().isEmpty() && !agent.getAgentLoginStatus().equals("null")) &&
                                            (agent.getAgentToken() != null && !agent.getAgentToken().isEmpty() && !agent.getAgentToken().equals("null"))
                            ) {
                                ID = agent.getId();
                                StoreName = agent.getBranchName();
                                AgentName = agent.getAgentName();
                                MobileNo = agent.getAgentmobileNo();
                                AgentPhoto = agent.getAgentPhoto();
                                AgentLoginStatus = agent.getAgentLoginStatus();
                                AgentToken = agent.getAgentToken();


                                Log.e("StoreName", "" + StoreName);


                                Log.e("MobileNo==", "" + MobileNo);
                                Log.e("AgentPhoto==", "" + AgentPhoto);
                                Log.e("Experties==", "" + Experties);
                                Log.e("Brand=", "" + Brand);
                                Log.e("AgentLoginStatus==", "" + AgentLoginStatus);
                                Log.e("AgentToken==", "" + AgentToken);

                                BankAgents agentlist = new BankAgents();
                                agentlist.setId(ID);
                                agentlist.setBranchName(StoreName);
                                agentlist.setAgentName(AgentName);
                                agentlist.setAgentmobileNo(MobileNo);
                                agentlist.setAgentPhoto(AgentPhoto);
                                agentlist.setAgentLoginStatus(AgentLoginStatus);
                                agentlist.setAgentToken(AgentToken);
                                agentsList.add(agentlist);
                                Log.e("UserDataSize", "" + agentsList.size());
                                Log.e("AgentName==", "" + AgentName);

                            }

                        } catch (ClassCastException cce) {
                            cce.printStackTrace();
// If the object can’t be casted into HashMap, it means that it is of type String. 

                        }

                    }

                    agentListAdapter = new AgentListAdapter(ListOfAgentsActivity.this, agentsList);
                    agent_list.setAdapter(agentListAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will

        switch (item.getItemId()) {
            case android.R.id.home:

                Intent intent = new Intent(ListOfAgentsActivity.this, EKYCActivity.class);
                startActivity(intent);
                finish();

                //  finish();
                break;


        }
        return (super.onOptionsItemSelected(item));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ListOfAgentsActivity.this,EKYCActivity.class);
        startActivity(intent);
        finish();
    }
}
