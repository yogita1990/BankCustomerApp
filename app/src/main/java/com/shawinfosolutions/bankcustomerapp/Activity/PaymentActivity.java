package com.shawinfosolutions.bankcustomerapp.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.shawinfosolutions.bankcustomerapp.R;
import com.squareup.picasso.Picasso;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class  PaymentActivity extends AppCompatActivity {
private Button OkBtn;
private ImageView CustomerTokenTxt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.final_pay);
        OkBtn = findViewById(R.id.OkBtn);
        CustomerTokenTxt = findViewById(R.id.CustomerTokenTxt);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("Verification");

        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);
        if (getIntent().getExtras() != null) {


            //Bundle extras = getIntent().getExtras();
            String key2 = getIntent().getExtras().getString("key2");
            Picasso.with(PaymentActivity.this).load(key2).into(CustomerTokenTxt);


        }
        OkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(PaymentActivity.this);
                dialog.setContentView(R.layout.custom_online);
                Button okBtn = (Button) dialog.findViewById(R.id.okBtn);
                // if button is clicked, close the custom dialog
                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent=new Intent(PaymentActivity.this,CustomerMainActivity.class);
                        startActivity(intent);

                        //  Toast.makeText(getApplicationContext(),"Dismissed..!!",Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will

        switch (item.getItemId()) {
            case android.R.id.home:

                Intent intent=new Intent(PaymentActivity.this,CustomerMainActivity.class);
                startActivity(intent);
                finish();

                //  finish();
                break;


        }
        return (super.onOptionsItemSelected(item));
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(PaymentActivity.this,CustomerMainActivity.class);
        startActivity(intent);
        finish();
    }
}
