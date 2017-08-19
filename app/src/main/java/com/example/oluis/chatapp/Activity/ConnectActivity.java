package com.example.oluis.chatapp.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.oluis.chatapp.R;

public class ConnectActivity extends AppCompatActivity {

    private EditText txtIp;
    private EditText txtPort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
    }

    public void btnConnectOnClick(View v){
        Intent i = new Intent(this, MainActivity.class);

        txtIp = (EditText) findViewById(R.id.txtIp);
        txtPort = (EditText) findViewById(R.id.txtPort);

        i.putExtra("IP", txtPort.getText());
        i.putExtra("PORT", txtPort.getText());

        startActivity(i);
        finish();
    }
}
