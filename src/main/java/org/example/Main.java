package org.example;

import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import siebel.integration.IO;
import siebel.integration.SiebelMessage;

import java.io.InputStream;

public class Main {
    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();

        try {

            Capital car = new Capital();
            ClassLoader classLoader = car.getClass().getClassLoader();
            InputStream is = classLoader.getResourceAsStream("capital.json");
            Capital SR = mapper.readValue(is, Capital.class);

            System.out.println(SR.getColor());
            System.out.println(SR.getMake());
            System.out.println(SR.getDatePurchased().toString());
            System.out.println(SR.getPricePurchased().toString());
/*
            LineItemIC lineItem = new LineItemIC();
            ClassLoader classLoader = lineItem.getClass().getClassLoader();
            InputStream is = classLoader.getResourceAsStream("lineitemic.json");
            LineItemIC SR = mapper.readValue(is, LineItemIC.class);
            System.out.println(SR.getExternalIntegrationId());


            ListOfIC lineItem = new ListOfIC();
            ClassLoader classLoader = lineItem.getClass().getClassLoader();
            InputStream is = classLoader.getResourceAsStream("listof.json");
            ListOfIC SR = mapper.readValue(is, ListOfIC.class);

            IO lineItem = new IO();
            ClassLoader classLoader = lineItem.getClass().getClassLoader();
            InputStream is = classLoader.getResourceAsStream("io.json");
            IO SR = mapper.readValue(is, IO.class);
            ListOfIC list = SR.getListOfICS();

            SiebelMessage siebel = new SiebelMessage();
            ClassLoader classLoader = siebel.getClass().getClassLoader();
            InputStream is = classLoader.getResourceAsStream("siebelmessage.json");
            SiebelMessage SR = mapper.readValue(is, SiebelMessage.class);
            IO list = SR.getIo();*/

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    //function capitalizeFirstLetter(string) {
    //    return string.charAt(0).toUpperCase() + string.slice(1);
    //}

    //console.log(capitalizeFirstLetter('foo')); // Foo

    //DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm a z");
    //mapper.setDateFormat(df);
}
