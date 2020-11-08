package com.tauk.coronacitydataapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddRemoveCoronaDataActivity extends AppCompatActivity {

    //declare variables as needed for edit texts, tvStatus .....
    private EditText etCity;
    private EditText etCases;
    private EditText etRecoveries;

    private TextView tvStatus;

    //declare variables for DatabaseReference ...............
    private DatabaseReference databaseReference;
    private DatabaseReference newCityReference;

    //declare variables for FirebaseAuth ............ (need this for logout)
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_remove_corona_data);

        //initialize FirebaseApp....
        FirebaseApp.initializeApp(this);

        //initialize FirebaseAuth.....
        firebaseAuth = FirebaseAuth.getInstance();

        //initialize ****ALL**** the EditTexts ..........
        etCity = findViewById(R.id.etCity);
        etCases = findViewById(R.id.etCases);
        etRecoveries = findViewById(R.id.etRecoveries);

        //initialize TextView - tvStatus ..........
        tvStatus = findViewById(R.id.tvStatus);
    }

    //Add the code to add a new city and its related data ........
    public void doAddCityData(View view) {
        //write the code to add a new city and its related data ........
        //add the data under child node "cities" with key as the city.......
        //note the cases and recoveries are integers

        //get all the data from the edit texts............
        String city = etCity.getText().toString();
        int cases = Integer.parseInt(etCases.getText().toString());
        int recoveries = Integer.parseInt(etRecoveries.getText().toString());

        //make an object using the model class that you have written
        final CoronaCityData coronaCityData = new CoronaCityData(city, cases, recoveries);

        //Add the node with the city name

        //get a reference to the Firebase database
        databaseReference = FirebaseDatabase.getInstance().getReference();

        if(databaseReference != null)
            Toast.makeText(this, databaseReference.toString(), Toast.LENGTH_LONG).show();

        //then add the data under that new node.
        //get a reference to the Firebase database
        //get the reference to the regNumber under /cars node
        newCityReference = databaseReference.child("/cities").child(city);

        //add the new city corona data
        newCityReference.setValue(coronaCityData) //adds the data under carRegNo child node
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        Toast.makeText(AddRemoveCoronaDataActivity.this, " data was added!!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        Toast.makeText(AddRemoveCoronaDataActivity.this, " db error!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //Search for city by name and display its cases and recoveries in the edit texts
    public void doSearchCity(View view) {
        final String searchCity = etCity.getText().toString();

        if (searchCity.isEmpty()) {
            Toast.makeText(AddRemoveCoronaDataActivity.this,  " City is empty!", Toast.LENGTH_LONG).show();
            etCity.setFocusable(true);
            tvStatus.setText("City is empty!");
            return;
        }

        //make a reference to the search city
        //get a reference to the Firebase database
        //STEP 1 - get the reference to your firebase database
        databaseReference = FirebaseDatabase.getInstance().getReference();

        //STEP 2 - Make a reference to the  city to update
        final DatabaseReference searchCityRef = databaseReference.child("/cities").child(searchCity);

        //see https://firebase.google.com/docs/database/android/read-and-write?authuser=0

        //STEP 3 - create a ValueEventListener
        ValueEventListener cityQueryListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) { //data from firebase into DataSnapshot
                if (dataSnapshot.exists()) { //if snapshot exists it means a city exists at searchCityRef

                    CoronaCityData coronaCityData = dataSnapshot.getValue(CoronaCityData.class);
                    etCases.setText(coronaCityData.cases + "");
                    etRecoveries.setText(coronaCityData.recoveries + "");

                    tvStatus.setText(searchCity + " found!");
                }
                else {
                    Toast.makeText(AddRemoveCoronaDataActivity.this, searchCity + " NOT found!", Toast.LENGTH_SHORT).show();
                    etCity.setFocusable(true);
                    tvStatus.setText(searchCity + " NOT found!");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting City failed, log a message
                Log.w("SEARCH", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };

        //STEP 4 - add the value event lister to your car reference
        searchCityRef.addListenerForSingleValueEvent(cityQueryListener);
    }

    //Update City data by its name
    public void doUpdateCityData(View view) {
        //first do a search - if city is found the update if the datasnapshot exists
        //code is similar but not the differences and similarity to add
        final String updateCity = etCity.getText().toString();
        //make a reference to the search city
        //get a reference to the Firebase database
        //STEP 1 - get the reference to your firebase database
        databaseReference = FirebaseDatabase.getInstance().getReference();

        //STEP 2 - Make a reference to the update city
        final DatabaseReference updateCityRef = databaseReference.child("/cities").child(updateCity);

        //see https://firebase.google.com/docs/database/android/read-and-write?authuser=0

        //STEP 3 - create a ValueEventListener
        ValueEventListener cityQueryListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) { //data from firebase into DataSnapshot
                if (dataSnapshot.exists()) { //if snapshot exists it means a city exists at updateCityRef
                    // Make a CoronaCityData object and fill it with values of the editTexts

                    int cases = Integer.parseInt(etCases.getText().toString());
                    int recoveries = Integer.parseInt(etRecoveries.getText().toString());

                    CoronaCityData updateCityData = new CoronaCityData(updateCity, cases, recoveries);

                    // The the city update using setValue()
                    updateCityRef.setValue(updateCityData)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // update was successful!
                                    tvStatus.setText(updateCity + " updated!");
                                    Toast.makeText(AddRemoveCoronaDataActivity.this, " data was Updated!!", Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // update failed
                                    Toast.makeText(AddRemoveCoronaDataActivity.this, " db error!", Toast.LENGTH_LONG).show();
                                }
                            });
                }
                else {
                    Toast.makeText(AddRemoveCoronaDataActivity.this, updateCity + " NOT found!", Toast.LENGTH_LONG).show();
                    etCases.setText("");
                    etRecoveries.setText("");
                    tvStatus.setText(updateCity + " NOT found!");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting City failed, log a message
                Log.w("Update -> SEARCH", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };

        //STEP 4 - add the value event lister to your car reference
        updateCityRef.addListenerForSingleValueEvent(cityQueryListener);
    }

    //Delete a city by its name ..........
    public void doDeleteCityData(View view) {
        //write the code to delete a city by its name ..........
        final String city = etCity.getText().toString();

        //check if the databaseReference is null - initialize it if null
        if (databaseReference == null)
            databaseReference = FirebaseDatabase.getInstance().getReference();

        //get a reference to the Firebase database until the child node to delete
        final DatabaseReference cityReference = databaseReference.child("/cities").child(city);

        //need the listener as we need to check if the city being deleted exists or not
        ValueEventListener removeCityListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) { //data from firebase into DataSnapshot
                if (dataSnapshot.exists()) { //if snapshot exists it means a city exists at updateCityRef
                    //city exits - go ahead and delete

                    //remove the city at the reference
                    cityReference.removeValue()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Write was successful!
                                    tvStatus.setText(city + " data was removed!");
                                    Toast.makeText(AddRemoveCoronaDataActivity.this, city + " data was removed!!", Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Write failed
                                    Toast.makeText(AddRemoveCoronaDataActivity.this, " remove error!", Toast.LENGTH_LONG).show();
                                }
                            });
                } else {
                    // City to delete does not exist - show messages!
                    tvStatus.setText(city + " does NOT exist!");
                    Toast.makeText(AddRemoveCoronaDataActivity.this, city + " does NOT exist!!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting City failed, log a message
                Log.w("DELETE -> SEARCH", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };

        //to the cityReference add the removeCityListener
        cityReference.addListenerForSingleValueEvent(removeCityListener);
    }

    //write the code to move to the ViewAllCitiesDataActivity ..........
    public void doViewAllCitiesData(View view) {
        //write the code to move to the ViewAllCitiesDataActivity ..........
        Intent intent = new Intent(this, ViewAllCitiesDataActivity.class);
        startActivity(intent);
    }


    //Write the code to logout the current user and close the current activity
    public void doLogout(View view) {
        //Write the code to logout the current user and close the current activity
        firebaseAuth.signOut();
        finish();
    }

}
