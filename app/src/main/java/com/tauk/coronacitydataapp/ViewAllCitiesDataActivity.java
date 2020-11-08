package com.tauk.coronacitydataapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.os.Bundle;

public class ViewAllCitiesDataActivity extends AppCompatActivity {

    //declare variable for textview .........
    private TextView tvCitiesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_cities_data);

        //Initialize FirebaseApp.............
        FirebaseApp.initializeApp(this);
        //initialize the tvCitiesList ..........

        tvCitiesList = findViewById(R.id.tvCityDataList);

        //call the displayAllCities() method
        displayAllCities();


    }

    //Write the code to display all cities..........
    public void displayAllCities() {
        //get a reference to the database .....

        //get a reference to the child node under which you have all data ......

        //create a ChildEventListener and override all the methods in it .......

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference citiesReference = firebaseDatabase.getReference("/cities");

        StringBuilder stringBuilder = new StringBuilder();

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d("View all Cities", "onChildAdded:" + dataSnapshot.getKey());

                // A new car has been added, add it to the displayed list
                CoronaCityData coronaCityData = dataSnapshot.getValue(CoronaCityData.class);
                tvCitiesList.append(coronaCityData.toString());
                // ...
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("View all Cars", "onChildChanged:" + dataSnapshot.getKey());
                Toast.makeText(ViewAllCitiesDataActivity.this, dataSnapshot.getKey() + " changed!", Toast.LENGTH_SHORT).show();

                displayAllCities();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d("removed", "onChildRemoved:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.
                String commentKey = dataSnapshot.getKey();
                Toast.makeText(ViewAllCitiesDataActivity.this,
                        commentKey + " removed - RELOADING.........", Toast.LENGTH_SHORT).show();
                //reload and display again              Toast.LENGTH_SHORT).show();
                displayAllCities();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d("moved", "onChildMoved:" + dataSnapshot.getKey());

                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.
                //CoronaCityData coronaCityData= dataSnapshot.getValue(CoronaCityData.class);
                String commentKey = dataSnapshot.getKey();
                Toast.makeText(ViewAllCitiesDataActivity.this, commentKey + " moved", Toast.LENGTH_SHORT).show();
                // ...
                displayAllCities();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("cancelled", "postComments:onCancelled", databaseError.toException());
                Toast.makeText(ViewAllCitiesDataActivity.this, "Failed to load comments.",
                        Toast.LENGTH_SHORT).show();
            }
        };

        citiesReference.addChildEventListener(childEventListener); //handle CRUD event on /cars
        tvCitiesList.setText(stringBuilder.toString());

    }
}

