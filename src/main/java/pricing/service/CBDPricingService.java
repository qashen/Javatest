package pricing.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.yaml.snakeyaml.error.MarkedYAMLException;

import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface CBDPricingService {

    Object buildRule (InputStream inputStream, InputStream ruleStream, InputStream indexYaml);

    Object retrieveInputMessage(InputStream is) throws JsonProcessingException;
    Object retrieveInputMessage(String json) throws JsonProcessingException;

    Object retrieveIndexYaml (InputStream is);

    Object retrieveRule (InputStream is);

    List<Object> retrieveItems (LinkedHashMap<String, String> lhm, Map<String,Object> object) throws NullPointerException;
    List<Object> retrieveItems (Map.Entry<String, String> entry, Iterator it, Map<String, Object> object) throws NullPointerException;
    List<Object> retrieveItems (Map.Entry<String, String> entry, Iterator it, List<Object> object) throws NullPointerException;
}
