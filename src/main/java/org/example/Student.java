package org.example;

import java.util.HashMap;
import java.util.Map;


import com.fasterxml.jackson.annotation.JsonAnyGetter;


public class Student {
    private String name;
    public Student() {
        properties = new HashMap<>();
    }
    public Student(String name, String rollNo) {
        this.name = name;
        this.rollNo = rollNo;
        properties = new HashMap<>();
    }

    private String rollNo;

    public void setName(String name) {
        this.name = name;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public String getName() {
        return name;
    }

    public String getRollNo() {
        return rollNo;
    }

    private Map<String, Object> properties;

    @JsonAnyGetter
    public Map<String, Object> getProperties(){
        return properties;
    }
    public void add(String property, Object value){
        properties.put(property, value);
    }
}