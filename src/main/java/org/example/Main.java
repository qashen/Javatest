package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mvel2.MVEL;
import org.mvel2.integration.VariableResolverFactory;
import org.mvel2.integration.impl.MapVariableResolverFactory;
import java.io.Serializable;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.sql.Timestamp;

import static org.junit.Assert.assertEquals;
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
            if (SR.getMapJsonPropertyField().get("Date Purchased") != null) {
                System.out.println(SR.getDatePurchased().toString());
            }
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

            testFunctionReuse();
            testCompileExpression();
            testCompileExpression1();
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

    public static void testFunctionReuse() {
        VariableResolverFactory functionFactory = new MapVariableResolverFactory();
        MVEL.eval("def Map1() { \"input.monthlySalary >= 50000.0\"; }; def Map2() { \"input.creditScore >= 500\" }; def group() { \" && \" };", functionFactory);
        VariableResolverFactory myVarFactory = new MapVariableResolverFactory();
        myVarFactory.setNextFactory(functionFactory);
        Serializable s = MVEL.compileExpression("Map1() + group() + Map2();");
        System.out.println ("Actual value: " + MVEL.executeExpression(s, myVarFactory).toString());
        //assertEquals("foobar", MVEL.executeExpression(s, myVarFactory));
    }

    public static void testCompileExpression() {
        Map imports = new HashMap();
        imports.put("CustomizeClassName", java.util.HashMap.class); // import a class
        imports.put("CustomizeMethodName", MVEL.getStaticMethod(System.class, "currentTimeMillis", new Class[0])); // import a static method

        // Compile the expression
        Serializable compiled = MVEL.compileExpression("map = new CustomizeClassName(); map.put('time', CustomizeMethodName()); map.time", imports);

        // Execute with a blank Map to allow vars to be declared.
        Long val = (Long) MVEL.executeExpression(compiled, new HashMap());
        Timestamp ts = new Timestamp (val);
        System.out.println ("Current time is: " + ts.toString());
        assert val > 0;
    }

    public static void testCompileExpression1() {

        // Compile the expression
        Serializable compiled = MVEL.compileExpression("x * 10");

        // Create a Map to hold the variables.
        Map vars = new HashMap();

        // Create a factory to envelop the variable map
        VariableResolverFactory factory = new MapVariableResolverFactory(vars);

        int total = 0;
        for (int i = 0; i < 100; i++) {
            // Update the 'x' variable.
            vars.put("x", i);

            // Execute the expression against the compiled payload and factory, and add the result to the total variable.
            total += (Integer) MVEL.executeExpression(compiled, factory);
        }

        // Total should be 49500
        assert total == 49500;
        System.out.println ("Current value is: " + total);
    }

    public static void testCompileExpression2() {

        // Compile the expression
        String multiply = "*";
        VariableResolverFactory functionFactory = new MapVariableResolverFactory();
        MVEL.eval("def DISCOUNT() { \"1 - \"; }; def LeftQuote() { \"(\" }; def RightQuote() { \"(\" }; def group() { \" && \" };", functionFactory);

        Serializable compiled = MVEL.compileExpression("x * 10");

        // Create a Map to hold the variables.
        Map vars = new HashMap();

        // Create a factory to envelop the variable map
        VariableResolverFactory factory = new MapVariableResolverFactory(vars);

        int total = 0;
        for (int i = 0; i < 100; i++) {
            // Update the 'x' variable.
            vars.put("x", i);

            // Execute the expression against the compiled payload and factory, and add the result to the total variable.
            total += (Integer) MVEL.executeExpression(compiled, factory);
        }

        // Total should be 49500
        assert total == 49500;
        System.out.println ("Current value is: " + total);
    }
}


