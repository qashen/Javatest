package siebel.integration;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class Main {
    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            SiebelMessage siebel = new SiebelMessage();
            ClassLoader classLoader = siebel.getClass().getClassLoader();
            InputStream is = classLoader.getResourceAsStream("siebelmessage.json");
            SiebelMessage SR = mapper.readValue(is, SiebelMessage.class);
            IO list = SR.getIo();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
