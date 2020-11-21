package com.ac.assignment_project_014.covid19;


import java.io.Serializable;

/**
 * Province data Pojo
 */
public class Covid19ProvinceData implements Serializable {

    /**
     * fields
     */
    private String name;
    private int caseNumber;
    private int increase;

    /**
     * default constructor
     */
    public Covid19ProvinceData(){}

    /**
     * parameterized contructor
     * @param name
     * @param caseNumber
     * @param increase
     */
    public Covid19ProvinceData(String name, int caseNumber, int increase){
        setName(name);
        setCaseNumber(caseNumber);
        setIncrease(increase);
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

    public int getIncrease() {
        return increase;
    }

    public void setIncrease(int increase) {
        this.increase = increase;
    }
}
