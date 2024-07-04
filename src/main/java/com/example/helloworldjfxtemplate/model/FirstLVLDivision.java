package com.example.helloworldjfxtemplate.model;

import java.time.LocalDateTime;

public class FirstLVLDivision {
    private int divisionID;
    private String divisionName;
    private int countryID;

    public FirstLVLDivision(int divisionID, String divisionName) {
        this.divisionID = divisionID;
        this.divisionName = divisionName;
    }

    public FirstLVLDivision(int divisionID, String divisionName, int countryId, LocalDateTime createDate,
                            String createdBy, LocalDateTime lastUpdated, String lastUpdatedBy) {
        this.divisionID = divisionID;
        this.divisionName = divisionName;
        this.countryID = countryID;
    }

    public int getDivisionID() {
        return divisionID;
    }

    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    public int getCountryID() {
        return countryID;
    }

    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    @Override
    public String toString() {
        return divisionName;
    }
}