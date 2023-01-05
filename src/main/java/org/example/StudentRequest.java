package org.example;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;

public class StudentRequest {
    private Student[] student;
    private String enrollStatus;
    private Car[] car;
    public StudentRequest() {
    }

    public StudentRequest(Student[] student, String enrollStatus, Car[] car) {
        this.student = student;
        this.enrollStatus = enrollStatus;
        this.car = car;
    }

    public Student[] getStudent() {
        return student;
    }

    public void setStudent(Student[] student) {
        this.student = student;
    }

    public String getEnrollStatus() {
        return enrollStatus;
    }

    public void setEnrollStatus(String enrollStatus) {
        this.enrollStatus = enrollStatus;
    }

    public Car[] getCar() {
        return car;
    }

    public void setCar(Car[] car) {
        this.car = car;
    }
}
