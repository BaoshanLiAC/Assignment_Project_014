package com.ac.assignment_project_014.covid19;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Covid19CountryData implements Serializable {

    private String countryName;
    private LocalDateTime searchDateTime;
    private Integer totalCase;
    private List<Covid19ProvinceData> dataList;

    public Covid19CountryData(){}
    public Covid19CountryData(String countryName, LocalDateTime searchDateTime){
        dataList = new ArrayList<>();

    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public LocalDateTime getSearchDateTime() {
        return searchDateTime;
    }

    public void setSearchDateTime(LocalDateTime searchDateTime) {
        this.searchDateTime = searchDateTime;
    }

    public Integer getTotalCase() {
        if(this.totalCase == null){
           Integer total = 0;
           for(Covid19ProvinceData p : getDataList()) total += p.getCaseNumber();
           return total;
        }
        return this.totalCase;
    }

    public void setTotalCase(Integer totalCase) {
        this.totalCase = totalCase;
    }

    public List<Covid19ProvinceData> getDataList() {
        return dataList;
    }
}
