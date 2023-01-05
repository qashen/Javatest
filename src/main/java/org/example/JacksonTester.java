package org.example;

import java.util.*;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.Class;
import java.io.InputStream;
public class JacksonTester {

    private JacksonTester(){
    }
    public static void main(String args[]) {

        /*try (InputStream inputStream = JacksonTester.class.getClass()
                .getClassLoader().getResourceAsStream("/Product.json")) {

        } catch (IOException e) {
            e.printStackTrace();
        }*/
        //ClassLoader classLoader = getClass().getClassLoader();
        //InputStream inputStream = JacksonTester.class.getClass().getClassLoader().getResourceAsStream("Product.json");
        //setup();
        ObjectMapper mapper = new ObjectMapper();
        try {
            Student student = new Student("Start","0");
            student.add("name", "Mark");
            student.add("rollNo", "1");
            String jsonString = mapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(student);
            System.out.println(jsonString);

            Car car = new Car();
            StudentRequest studentRequest = new StudentRequest();
            ClassLoader classLoader = studentRequest.getClass().getClassLoader();
            //create JSON file in \src\main\resources folder.
            InputStream is = classLoader.getResourceAsStream("studentstatus.json");
            //mapper.readValue(is, student);
           //Student studentJS = mapper.readValue(is, Student.class);
             StudentRequest SR = mapper.readValue (is, StudentRequest.class);
             String enrollStatus = SR.getEnrollStatus();
            Student[] st = SR.getStudent();

            Car[] cars = SR.getCar();
            for (int i = 0; i< cars.length; i++)
            {
                String carcolor = cars[i].getColor();
                String cartype = cars[i].getType();
                System.out.println(carcolor);
                System.out.println(cartype);

            }
            System.out.println (st[0].getName());
            System.out.println (st[0].getRollNo());
            System.out.println(enrollStatus);
            //String json = "{ \"Name\" : \"Black\", \"RollNo\" : \"BMW\" }";
            //Student studentJS = mapper.readValue(json, Student.class);
            //String json = "{ \"color\" : \"Black\", \"type\" : \"BMW\" }";
            //Car car1 = mapper.readValue(is, Car.class);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void setup() {
        //Student students = new Student();
        //ClassLoader classLoader = students.getClass().getClassLoader();
        //InputStream inputStream = classLoader.getResourceAsStream("Product.json");
    }
}

