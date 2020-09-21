package com.example.companyapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class ContactAddressActivity extends AppCompatActivity {

    String location = "DEF";

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_address);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("messages");
        final DatabaseReference locationRef = database.getReference("company/location");
        final ContactAddressActivity supportFormActivity = this;

        locationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                if(value == null) {
                    return;
                }
                supportFormActivity.setLocation(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("{}", "Failed to read value.", error.toException());
            }
        });

        Button historyBtn = (Button) findViewById(R.id.location_btn);
        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a Uri from an intent string. Use the result to create an Intent.
                if(!location.equals("DEF")) {
                    Uri gmmIntentUri = Uri.parse(location);

                    // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    // Make the Intent explicit by setting the Google Maps package
                    mapIntent.setPackage("com.google.android.apps.maps");

                    // Attempt to start an activity that can handle the Intent
                    startActivity(mapIntent);
                } else {
                    Toast.makeText(supportFormActivity, "Location Not Available", Toast.LENGTH_SHORT).show();
                }

            }
        });

        Button sendBtn = (Button) findViewById(R.id.send_btn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView name = findViewById(R.id.contact_name);
                TextView email = findViewById(R.id.contact_email);
                TextView phone = findViewById(R.id.contact_phone);
                TextView message = findViewById(R.id.contact_message);

                DatabaseReference childNode = myRef.child(UUID.randomUUID().toString());
                childNode.child("name").setValue(name.getText().toString());
                childNode.child("email").setValue(email.getText().toString());
                childNode.child("phone").setValue(phone.getText().toString());
                childNode.child("message").setValue(message.getText().toString());
                Toast.makeText(supportFormActivity, "Message Sent!", Toast.LENGTH_LONG).show();
                name.setText("");
                email.setText("");
                phone.setText("");
                message.setText("");
            }
        });
    }
}
