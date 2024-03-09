package com.example.sickpredict;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PredictionActivity extends AppCompat {

    private static final String TAG = "PredictionActivity";
    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    ArrayList<String> symptoms_array = new ArrayList<>();
    private ChipGroup chipGroup;
    private AutoCompleteTextView autoCompleteTextView;
    private List<String> symptomsList;
    private ArrayAdapter<String> adapter;
    private Button predict;
    private TextView result;
    private ImageButton microphone;
    private String url = "https://ml-2-jpsk.onrender.com/predict";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction);


        predict = findViewById(R.id.predict_button);

        microphone = findViewById(R.id.imageButton_microphone);

        result = findViewById(R.id.result);

        chipGroup = findViewById(R.id.chipGroup);
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);

        // Initialize symptoms list
        symptomsList = new ArrayList<>();
        // Add your symptoms to the list
        symptomsList.add("eye_irritation");
        symptomsList.add("running_nose");
        symptomsList.add("stuffy_nose");
        symptomsList.add("watery_eyes");
        symptomsList.add("sneezing");
        symptomsList.add("itchy_nose");
        symptomsList.add("itchy_throat");
        symptomsList.add("inflamed_throat");
        symptomsList.add("watery_stools");
        symptomsList.add("frequent_bowel_movements");
        symptomsList.add("abdomen_pain");
        symptomsList.add("nausea");
        symptomsList.add("bloating");
        symptomsList.add("bloody_stools");
        symptomsList.add("fever");
        symptomsList.add("headachae");
        symptomsList.add("more_intense_pain");
        symptomsList.add("fatigue");
        symptomsList.add("dry_cough");
        symptomsList.add("sore_throat");
        symptomsList.add("cough");
        symptomsList.add("vomiting");
        symptomsList.add("heartburn");
        symptomsList.add("indigestion");
        symptomsList.add("change_in_apetite");
        symptomsList.add("anemia");
        symptomsList.add("rashes");
        symptomsList.add("pain_behind_eyes");
        symptomsList.add("pain_in_joints");
        symptomsList.add("feeling_of_discomfort");
        symptomsList.add("low energy");
        symptomsList.add("cough_with_mucus");
        symptomsList.add("greenish_yellow_bloody_mucus");
        symptomsList.add("shortness_of_breath");
        symptomsList.add("chills");
        symptomsList.add("sweating");
        symptomsList.add("shallow_breathing");
        symptomsList.add("chest_pain");
        // Add more symptoms as needed

        // Initialize and set adapter for AutoCompleteTextView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, symptomsList);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setThreshold(1); // Show suggestions after 1 character is typed

        // Set listener for adding chips when a symptom is selected
        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            String symptom = adapter.getItem(position);
            addChip(symptom);
            autoCompleteTextView.setText("");
        });

        microphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speak();
            }
        });

        // Inside your predict.setOnClickListener() method:

        predict.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Prediction();
//                Intent intent = new Intent(PredictionActivity.this, DoctorListActivity.class);
//                startActivity(intent);
            }


        });

    }

    private void speak() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi Speak Your Symptoms");

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (Exception e) {
            Toast.makeText(PredictionActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    //get text array from voice intent
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    //set to inputbox
                }
                break;
            }
        }
    }

    private void addChip(String text) {
        Chip chip = new Chip(this);
        chip.setText(text);
        symptoms_array.add(text); // Add the symptom to the array list
        chip.setCloseIconVisible(true);
        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chipGroup.removeView(chip);
                symptoms_array.remove(text); // Remove the symptom from the array list
            }
        });
        chipGroup.addView(chip);
    }


    protected void Prediction() {

        if (symptoms_array.isEmpty()) {
            Toast.makeText(PredictionActivity.this, "Please enter symptoms", Toast.LENGTH_LONG).show();
            return; // Exit early if symptoms are empty
        }


        // Convert symptoms array to JSON array

        // Create JSON object with the symptoms array
        JSONObject jsonBody = new JSONObject();
        try {
            JSONArray jsonArray = new JSONArray(symptoms_array);
            // Put the JSONArray into the jsonBody
            jsonBody.put("symptoms", jsonArray);


        } catch (JSONException e) {
            e.printStackTrace();
            return; // Exit early if there's an error creating the JSON object
        }

        // Hit the API
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String data = response.getString("disease");
                            result.setText(data);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(PredictionActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(PredictionActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the RequestQueue
        RequestQueue queue = Volley.newRequestQueue(PredictionActivity.this);
        queue.add(jsonObjectRequest);

    }
}