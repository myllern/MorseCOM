package com.example.morescom;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class MainActivity extends AppCompatActivity {
    Button receiverButton;
    Button transmitterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        transmitterButton = findViewById(R.id.transmitter_button);
        receiverButton = findViewById(R.id.receiver_button);
        receiverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openReceiverActivity();
            }
        });
        transmitterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTransmitterActivity();
            }
        });



    }

    public void openTransmitterActivity() {
        Intent intent = new Intent(this, TransmitterActivity.class);
        startActivity(intent);
    }
    public void openReceiverActivity() {
        Intent intent = new Intent(this, ReceiverActivity.class);
        startActivity(intent);
    }
}
