package com.ac.assignment_project_014.covid19;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Covid19CountryData implements Serializable {
    private Long id;
    private String countryName;
    private String searchDateTime;
    private Integer totalCase;
    private Integer dailyIncrease;
    private ArrayList<Covid19ProvinceData> dataList;


    public Covid19CountryData(){}
    public Covid19CountryData(String countryName, String searchDateTime){
        dataList = new ArrayList<>();
        setCountryName(countryName);
        setSearchDateTime(searchDateTime);
    }

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id; }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getSearchDateTime() {
        return searchDateTime;
    }

    public void setSearchDateTime(String searchDateTime) {
        this.searchDateTime = searchDateTime;
    }

    public Integer getTotalCase() {
        return this.totalCase;
    }

    public void setTotalCase(Integer totalCase) {
        this.totalCase = totalCase;
    }

    public ArrayList<Covid19ProvinceData> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<Covid19ProvinceData> dataList) {
        this.dataList = dataList;
        Integer total = 0,increase = 0;
        for(Covid19ProvinceData p : getDataList()) {
            total += p.getCaseNumber();
            increase += p.getIncrease();
        }
        setTotalCase(total);
        setDailyIncrease(increase);
    }

    public Integer getDailyIncrease() {
        return dailyIncrease;
    }

    public void setDailyIncrease(Integer dailyIncrease) {
        this.dailyIncrease = dailyIncrease;
    }

    @Override
    public String toString() {
        return searchDateTime +
                ", totalCase: " + totalCase +
                ", dailyIncrease: " + dailyIncrease;
    }
}
