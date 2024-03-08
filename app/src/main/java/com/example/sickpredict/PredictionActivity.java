package com.example.sickpredict;

import androidx.annotation.Nullable;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.sickpredict.user.DoctorListActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class PredictionActivity extends AppCompat {

    private EditText editSymptoms;
    private Button predict;
    private TextView result;
    private ImageButton microphone;
    private static final String TAG = "PredictionActivity";

    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;

    private String url =  "https://sick-predict-app.onrender.com/predict";

    ArrayList<String> symptoms_array = new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction);

        editSymptoms = findViewById(R.id.symptoms);

        predict = findViewById(R.id.predict_button);

        microphone = findViewById(R.id.imageButton_microphone);

        result = findViewById(R.id.result);

        microphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speak();
            }
        });

        // Inside your predict.setOnClickListener() method:

        predict.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(PredictionActivity.this, DoctorListActivity.class);
                startActivity(intent);
            }



        });

    }

    private void speak() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi Speak Your Symptoms");

        try{
            startActivityForResult(intent,REQUEST_CODE_SPEECH_INPUT);
        }
        catch (Exception e){
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
                    editSymptoms.setText(result.get(0));
                }
                break;
            }
        }
    }


    protected void Prediction() {

                String symp = editSymptoms.getText().toString().trim();
                if (symp.isEmpty()) {
                    Toast.makeText(PredictionActivity.this, "Please enter symptoms", Toast.LENGTH_LONG).show();
                    return; // Exit early if symptoms are empty
                }

                // Add the symptom to the array list
                symptoms_array.add(symp);

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