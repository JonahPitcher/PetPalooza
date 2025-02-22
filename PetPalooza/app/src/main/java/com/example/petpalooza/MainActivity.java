package com.example.petpalooza;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_screen); // Link to start_screen.xml

        // Find the start button by its ID
        Button startButton = findViewById(R.id.startbutton);

        // Set up an onClickListener
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if pet data already exists in SharedPreferences
                Pet pet = loadPetInfo();
                if (pet != null) {
                    // If pet exists, go directly to GameplayActivity
                    Intent intent = new Intent(MainActivity.this, GameplayActivity.class);
                    intent.putExtra("PET_TYPE", pet.getType());
                    intent.putExtra("PET_NAME", pet.getName());
                    startActivity(intent);
                } else {
                    // If no pet exists, navigate to PetSelectActivity
                    Intent intent = new Intent(MainActivity.this, PetSelectActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    // Method to load the pet info from SharedPreferences
    private Pet loadPetInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences("PetInfo", MODE_PRIVATE);
        String petData = sharedPreferences.getString("PET_DATA", null);

        if (petData == null) {
            return null;  // No saved pet data
        }

        try {
            // Decode the Base64 string back to byte array
            byte[] petBytes = Base64.decode(petData, Base64.DEFAULT);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(petBytes);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Pet pet = (Pet) objectInputStream.readObject();
            objectInputStream.close();
            return pet;
        } catch (Exception e) {
            e.printStackTrace();
            return null;  // Failed to load pet data
        }
    }

}
