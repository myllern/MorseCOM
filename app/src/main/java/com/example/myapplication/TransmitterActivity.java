package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TransmitterActivity extends AppCompatActivity {
    Button sendButton;
    EditText messageBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transmitter);
        sendButton = findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMessageInput();
            }
        });
        messageBox = (EditText) findViewById(R.id.transmitter_input_box);



    }

    public void getMessageInput(){
        if(messageBox.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "Enter the Data", Toast.LENGTH_SHORT).show();
        }

    }


}