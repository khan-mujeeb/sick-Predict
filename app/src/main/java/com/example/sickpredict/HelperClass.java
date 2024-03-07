package com.example.sickpredict;

public class HelperClass {

    String fullname;
    String email;
    String dob;
    String gender;
    String mobile;

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public HelperClass(String fullname, String email, String dob, String gender, String mobile) {
        this.fullname = fullname;
        this.email = email;
        this.dob = dob;
        this.gender = gender;
        this.mobile = mobile;
    }

    public HelperClass(String fullname, String mobile, String gender, String dob) {
        this.fullname = fullname;
        this.mobile = mobile;
        this.gender = gender;
        this.dob = dob;

    }

    public HelperClass() {
    }
}
