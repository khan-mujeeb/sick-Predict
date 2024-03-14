package com.example.sickpredict;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
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
    private String url = "https://ml1-9y6p.onrender.com/predict";









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
        symptomsList.add(getString(R.string.eye_irritation));
        symptomsList.add(getString(R.string.running_nose));
        symptomsList.add(getString(R.string.stuffy_nose));
        symptomsList.add(getString(R.string.watery_eyes));
        symptomsList.add(getString(R.string.sneezing));
        symptomsList.add(getString(R.string.itchy_nose));
        symptomsList.add(getString(R.string.itchy_throat));
        symptomsList.add(getString(R.string.inflamed_throat));
        symptomsList.add(getString(R.string.watery_stools));
        symptomsList.add(getString(R.string.frequent_bowel_movements));
        symptomsList.add(getString(R.string.abdomen_pain));
        symptomsList.add(getString(R.string.nausea));
        symptomsList.add(getString(R.string.bloating));
        symptomsList.add(getString(R.string.bloody_stools));
        symptomsList.add(getString(R.string.fever));
        symptomsList.add(getString(R.string.headachae));
        symptomsList.add(getString(R.string.more_intense_pain));
        symptomsList.add(getString(R.string.fatigue));
        symptomsList.add(getString(R.string.dry_cough));
        symptomsList.add(getString(R.string.sore_throat));
        symptomsList.add(getString(R.string.cough));
        symptomsList.add(getString(R.string.vomiting));
        symptomsList.add(getString(R.string.heartburn));
        symptomsList.add(getString(R.string.indigestion));
        symptomsList.add(getString(R.string.change_in_apetite));
        symptomsList.add(getString(R.string.anemia));
        symptomsList.add(getString(R.string.rashes));
        symptomsList.add(getString(R.string.pain_behind_eyes));
        symptomsList.add(getString(R.string.pain_in_joints));
        symptomsList.add(getString(R.string.feeling_of_discomfort));
        symptomsList.add(getString(R.string.low_energy));
        symptomsList.add(getString(R.string.cough_with_mucus));
        symptomsList.add(getString(R.string.greenish_yellow_bloody_mucus));
        symptomsList.add(getString(R.string.shortness_of_breath));
        symptomsList.add(getString(R.string.chills));
        symptomsList.add(getString(R.string.sweating));
        symptomsList.add(getString(R.string.shallow_breathing));
        symptomsList.add(getString(R.string.chest_pain));
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
                            String data = response.getString("prediction");
                            String prob = response.getString("Accuracy");
                            result.setText("Prediction: " + data + ", Accuracy: " + prob);
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