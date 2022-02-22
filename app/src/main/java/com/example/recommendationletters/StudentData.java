package com.example.recommendationletters;

public class StudentData {
    String full_name, registration_nr;
    float total_average;

    public StudentData(){};

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getRegistration_nr() {
        return registration_nr;
    }

    public void setRegistration_nr(String registration_nr) {
        this.registration_nr = registration_nr;
    }

    public float getTotal_average() {
        return total_average;
    }

    public void setTotal_average(float total_average) {
        this.total_average = total_average;
    }
}
