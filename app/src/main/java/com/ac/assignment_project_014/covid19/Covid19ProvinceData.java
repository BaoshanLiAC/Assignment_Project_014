package com.ac.assignment_project_014.covid19;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Covid19ProvinceData implements Serializable {
    private String name;
    private int caseNumber;

    public Covid19ProvinceData(){}

    public Covid19ProvinceData(String name, int caseNumber){
        setName(name);
        setCaseNumber(caseNumber);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(int caseNumber) {
        this.caseNumber = caseNumber;
    }


}
