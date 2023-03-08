package pricing.rulesImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import pricing.ruleEngine.Rule;
import pricing.service.CBDPricingService;
import pricing.util.PricingUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static pricing.util.constants.*;
@SuppressWarnings({ "unchecked", "rawtypes" })
public class CBDPricingServiceImpl implements CBDPricingService {

    @Override
    public Object buildRule (InputStream inputStream, InputStream ruleStream, InputStream indexYaml) {
        try {
            Map<String, Object> inputMessage = (Map<String, Object>) retrieveInputMessage(inputStream);
            Map<String, Object> rule = (Map<String, Object>) retrieveInputMessage(ruleStream);
            LinkedHashMap<String, Object> readValue = (LinkedHashMap<String, Object>)retrieveIndexYaml(indexYaml);
            LinkedHashMap<String, Object> input = null;
            LinkedHashMap<String, Object> ruleIndex;
            Map<String, Object> mainObj = new HashMap<>();
            for (Map.Entry m : readValue.entrySet()) {
                if (!Objects.isNull( m.getKey()) && ( m.getKey().toString().compareToIgnoreCase(PRODUCT_ORDER) == 0))
                {
                    input = (LinkedHashMap<String, Object>)m.getValue();
                }
                else if (!Objects.isNull( m.getKey()) && ( m.getKey().toString().compareToIgnoreCase(RULE) == 0))
                {
                    ruleIndex = (LinkedHashMap<String, Object>)m.getValue();
                    if (ruleIndex != null)
                    {
                        List<Object> ruleListObj = retrieveItems(ruleIndex, rule);
                        if (ruleListObj != null) {
                            Map<String, String> mapProperties = new HashMap<>();
                            Map<String, String> mapRuleFields = new HashMap<>();
                            Map<String, String> mapInputFields = new HashMap<>();
                            PricingUtil.addToRef(ruleIndex, mapProperties, mapRuleFields, mapInputFields);
                            List<Rule> ruleList = new ArrayList<>();
                            PricingUtil.SetRule(ruleListObj, ruleList, mapProperties, mapRuleFields);
                            mainObj.put (RULE, ruleList);
                            mainObj.put (FIELD_MAP, mapInputFields);
                        }
                    }
                }
            }
            if (input != null)
            {
                mainObj.put (INPUT, retrieveItems(input, inputMessage));
            }
            return mainObj;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Object retrieveInputMessage(InputStream jsonInputStream) throws JsonProcessingException {
        String json;
        json = new BufferedReader(
                new InputStreamReader(jsonInputStream, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));
        return PricingUtil.convertJsonToObject(json, Object.class);
    }

    @Override
    public Object retrieveInputMessage(String json) {
        return PricingUtil.convertJsonToObject(json, Object.class);
    }

    @Override
    public Object retrieveRule (InputStream is) {
        ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
        try {
            return yamlMapper.readValue(is, Map.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object retrieveIndexYaml (InputStream is) {
        ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
        try {
            return yamlMapper.readValue(is, Map.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Object> retrieveItems(LinkedHashMap<String, Object> input, Map<String, Object> object) throws NullPointerException {

        Iterator<Map.Entry<String, Object>> it = input.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            if (!Objects.isNull (entry.getValue()) && entry.getValue() instanceof String) {
                if (entry.getValue().toString().compareToIgnoreCase(MAP) == 0) {
                    Map<String, Object> subObject = (Map<String, Object>) object.get(entry.getKey());
                    if (it.hasNext()) {
                        return retrieveItems(entry, it, subObject);
                    }
                } else if (entry.getValue().toString().compareToIgnoreCase(LIST) == 0) {
                    List<Object> subObject = (List<Object>) object.get(entry.getKey());
                    if (it.hasNext()) {
                        return retrieveItems(entry, it, subObject);
                    } else return subObject;
                }
                else if (entry.getKey().compareToIgnoreCase(RootType)==0)
                {
                    return (List<Object>) object.get(entry.getValue().toString());
                }
            }
        }
        return null;
    }

    @Override
    public List<Object> retrieveItems(Map.Entry<String, Object> entry, Iterator<Map.Entry<String, Object>> it, Map<String, Object> object) throws NullPointerException {
        if (it.hasNext()) {
            Map.Entry<String, Object> nextEntry = it.next();
            if (!Objects.isNull(entry.getValue()) && entry.getValue() instanceof String) {
                if (nextEntry.getValue().toString().compareToIgnoreCase(MAP) == 0) {
                    Map<String, Object> subObject = (Map<String, Object>) object.get(nextEntry.getKey());
                    if (it.hasNext()) {
                        return retrieveItems(nextEntry, it, subObject);
                    }
                } else if (nextEntry.getValue().toString().compareToIgnoreCase(LIST) == 0) {
                    List<Object> subObject = (List<Object>) object.get(nextEntry.getKey());
                    if (it.hasNext()) {
                        return retrieveItems(nextEntry, it, subObject);
                    } else return subObject;
                }
            }
            else {
                return (List<Object>) object.get(entry.getKey());
            }
        }
        return null;
    }


    @Override
    public List<Object> retrieveItems(Map.Entry<String, Object> entry, Iterator<Map.Entry<String, Object>> it, List<Object> object) throws NullPointerException {
        if (it.hasNext()) {
            Map.Entry<String, Object> nextEntry = it.next();
            if (!Objects.isNull(entry.getValue()) && entry.getValue() instanceof String) {
                if (nextEntry.getValue().toString().compareToIgnoreCase(MAP) == 0) {
                    Map<String, Object> subObject = (Map<String, Object>) ((Map<String, Object>) object.get(0)).get(nextEntry.getKey());
                    if (it.hasNext()) {
                        return retrieveItems(nextEntry, it, subObject);
                    }
                } else if (nextEntry.getValue().toString().compareToIgnoreCase(LIST) == 0) {
                    List<Object> subObject = (List<Object>) object.get(0);
                    if (it.hasNext()) {
                        return retrieveItems(nextEntry, it, subObject);
                    } else return subObject;
                }
            }
            else {
                return ((List<Object>) object.get(0));
            }
        }
        return object;
    }

}
