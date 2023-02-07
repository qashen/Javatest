package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

            String myInput = "Country EQUALS India";
            System.out.println("My Input is : " + myInput);
            //String myOutputWithRegEX = Pattern.compile("EQUALS").matcher(myInput).replaceAll("=");
            //System.out.println("My Output is : " + myOutputWithRegEX);


            HashMap<String, String> operator = new HashMap<>();

            operator.put (Operator.EQUALS.toString(), Operator.EQUALS.getMathsymbol());

            // Iterating the map
            for (Map.Entry m : operator.entrySet()) {
                myInput = Pattern.compile (m.getKey().toString()).matcher(myInput).replaceAll(m.getValue().toString());
            }
            System.out.println("My Output is : " + myInput);

            Operator.stream()
                    .filter(d -> d.getMathsymbol().contains("<"))
                    .forEach(System.out::println);
            //Operator.stream()
             //       .forEach(System.out::println);
            //String equals = "EQUALS";
            //List<Operator> operators = new ArrayList<>();
            //for (Operator day : operators) {
            //    System.out.println(day);
            //}

            //System.out.println (Operator.EQUALS.toString() + " " + Operator.EQUALS.getMathsymbol());

            /*
            				"id": "criteria-1277039798-1671165605",
							"valueType": "NUMBER",
							"criteriaValue": "1",
							"criteriaPara": "Quantity",
							"criteriaOperator": "EQUALS"

							convert to input.criteriaPara criteriaOperator valueType criteriaValue
             */
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
