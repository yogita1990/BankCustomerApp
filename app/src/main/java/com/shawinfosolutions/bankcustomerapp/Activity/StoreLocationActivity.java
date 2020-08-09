package com.shawinfosolutions.bankcustomerapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.shawinfosolutions.bankcustomerapp.R;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class StoreLocationActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener{
    String[] cities = { "Mumbai", "Navi Mumbai", "Thane", "Palghar", "Vijaywada","Pune","Delhi","Surat"};

    private ActionBar actionbar;
    Spinner citySpin;
    private String spinVal;
    private LinearLayout firstLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_loc);
        citySpin=findViewById(R.id.citySpin);
        actionbar = getSupportActionBar();
        actionbar.setTitle("Nearest Branch");

        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);
        citySpin.setOnItemSelectedListener(this);
        firstLayout=findViewById(R.id.firstLayout);
        firstLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(StoreLocationActivity.this, EKYCActivity.class);
                startActivity(intent1);
                finish();
            }
        });
        final Intent intent = getIntent();
        spinVal = intent.getStringExtra("selectedVal");
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,cities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        citySpin.setAdapter(adapter);
        if (spinVal != null) {
            int spinnerPosition = adapter.getPosition(spinVal);
            citySpin.setSelection(spinnerPosition);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will

        switch (item.getItemId()) {
            case android.R.id.home:

                Intent intent=new Intent(StoreLocationActivity.this, CustomerMainActivity.class);
                startActivity(intent);
                finish();

                //  finish();
                break;

        }
        return (super.onOptionsItemSelected(item));
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(StoreLocationActivity.this, CustomerMainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        // Toast.makeText(getApplicationContext(),cities[i] , Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
