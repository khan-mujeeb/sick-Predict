package com.example.sickpredict.api;

import java.util.List;

public class utils {
    public static class PredictionRequest {
        private List<String> symptoms;

        public PredictionRequest(List<String> symptoms) {
            this.symptoms = symptoms;
        }
    }

    public class PredictionResponse {
        private String prediction;
        private String Accuracy;

        public String getPrediction() {
            return prediction;
        }

        public String getAccuracy() {
            return Accuracy;
        }
    }

    public static class MedicineRequest {
        private String disease;
        private int age;
        private String gender;

        public MedicineRequest(String disease, int age, String gender) {
            this.disease = disease;
            this.age = age;
            this.gender = gender;
        }
    }

    public class MedicineResponse {
        private List<String> predicted_drugs;

        public List<String> getPredicted_drugs() {
            return predicted_drugs;
        }
    }

}
