package pricing.rulesImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import pricing.ruleEngine.Rule;
import pricing.service.CBDPricingService;
import pricing.util.ObjectUtils;
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
            Object inputMessage = retrieveInputMessage(inputStream);
            Object rule = retrieveInputMessage(ruleStream);
            LinkedHashMap<String, Object> readValue = (LinkedHashMap<String, Object>) retrieveIndexYaml(indexYaml);
            LinkedHashMap<String, Object> input = null;
            LinkedHashMap<String, Object> ruleIndex;
            Map<String, Object> mainObj = new HashMap<>();
            for (Map.Entry m : readValue.entrySet()) {
                if (!Objects.isNull(m.getKey()) && (m.getKey().toString().compareToIgnoreCase(PRODUCT_ORDER) == 0)) {
                    input = (LinkedHashMap<String, Object>) m.getValue();
                } else if (!Objects.isNull(m.getKey()) && (m.getKey().toString().compareToIgnoreCase(RULE) == 0)) {
                    ruleIndex = (LinkedHashMap<String, Object>) m.getValue();
                    if (!ObjectUtils.isEmpty(ruleIndex)) {
                        Map<String, String> mapProperties = new HashMap<>();
                        Map<String, String> mapRuleFields = new HashMap<>();
                        Map<String, String> mapInputFields = new HashMap<>();
                        PricingUtil.addToRef(ruleIndex, mapProperties, mapRuleFields, mapInputFields);
                        mainObj.put(FIELD_MAP, mapInputFields);
                        //List<Object> ruleListObj = null;
                        Map<String, Object> ruleListObj = new HashMap<>();
                        if (rule instanceof Map) {
                            ruleListObj = retrieveItemMap(ruleIndex, (Map<String, Object>) rule);
                        } else if (rule instanceof Collection) {
                            ruleListObj = retrieveItemMap(ruleIndex, (List<Object>) rule);
                        }
                        if (!ObjectUtils.isEmpty(ruleListObj)) {

                            Map<String, Object> mapRuleList = new HashMap<>();
                            Map<String, String> mapRelationship = new HashMap<>();
                            for (Map.Entry<String, Object> subObject : ruleListObj.entrySet()) {
                                List<Rule> ruleList = new ArrayList<>();
                                List<Object> ruleListSubObject = (List<Object>) subObject.getValue();
                                String relationshipAmongRule = PricingUtil.SetRule(ruleListSubObject, ruleList, mapProperties, mapRuleFields);
                                mapRuleList.put (subObject.getKey(), ruleList);
                                mapRelationship.put (subObject.getKey(), relationshipAmongRule);
                            }
                            mainObj.put(RULE, mapRuleList);
                            mainObj.put(RELATIONSHIP, mapRelationship);
                        }
                    }
                }
            }
            if (!ObjectUtils.isEmpty(input)) {
                if (inputMessage instanceof Map) {
                    mainObj.put(INPUT, retrieveItems(input, (Map<String, Object>) inputMessage));
                } else if (inputMessage instanceof Collection) {
                    mainObj.put(INPUT, retrieveItems(input, (List<Object>) inputMessage));
                }
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
                } else if (entry.getKey().compareToIgnoreCase(RootType) == 0) {
                    return (List<Object>) object.get(entry.getValue().toString());
                }
            }
        }
        return null;
    }

    @Override
    public List<Object> retrieveItems(LinkedHashMap<String, Object> input, List<Object> object) throws NullPointerException {

        Iterator<Map.Entry<String, Object>> it = input.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            if (!Objects.isNull(entry.getValue()) && entry.getValue() instanceof String) {
                if (entry.getValue().toString().compareToIgnoreCase(MAP) == 0) {
                    Map<String, Object> subObject = (Map<String, Object>) ((Map<String, Object>) object.get(0)).get(entry.getKey());
                    if (it.hasNext()) {
                        return retrieveItems(entry, it, subObject);
                    }
                } else if (entry.getValue().toString().compareToIgnoreCase(LIST) == 0) {
                    List<Object> subObject = (List<Object>) object.get(0);
                    if (it.hasNext()) {
                        return retrieveItems(entry, it, subObject);
                    } else return subObject;
                } else if (entry.getKey().compareToIgnoreCase(RootType) == 0) {
                    List<Object> ruleListObj = new ArrayList<>();
                    //return (List<Object>) object.get(0);
                    return filterByType(entry.getValue(), ruleListObj, object);
                }
            }
        }
        return null;
    }

    @Override
    public List<Object> retrieveItems
            (Map.Entry<String, Object> entry, Iterator<Map.Entry<String, Object>> it, Map<String, Object> object) throws
            NullPointerException {
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
            } else {
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
            } else {
                return ((List<Object>) object.get(0));
            }
        }
        return object;
    }

    @Override
    public Map<String, Object> retrieveItemMap(LinkedHashMap<String, Object> input, Map<String, Object> object) throws NullPointerException {
        for (Map.Entry<String, Object> entry : input.entrySet()) {
            if (!Objects.isNull(entry.getValue()) && entry.getValue() instanceof String) {
                if (entry.getKey().compareToIgnoreCase(RootType) == 0) {
                    Map<String, Object> ruleListObj = new HashMap<>();
                    return filterByType(entry.getValue(), ruleListObj, object);
                }
            }
        }
        return null;
    }

    @Override
    public Map<String, Object> retrieveItemMap(LinkedHashMap<String, Object> input, List<Object> object) throws NullPointerException {

        for (Map.Entry<String, Object> entry : input.entrySet()) {
            if (!Objects.isNull(entry.getValue()) && entry.getValue() instanceof String) {
                if (entry.getKey().compareToIgnoreCase(RootType) == 0) {
                    Map<String, Object> ruleListObj = new HashMap<>();
                    return filterByType(entry.getValue(), ruleListObj, object);
                }
            }
        }
        return null;
    }


    @Override
    public List<Object> filterByType(Object type, List<Object> input, List<Object> object) throws NullPointerException {
        if (type instanceof String) {
            for (Object child : object) {
                if (child instanceof Collection) {
                    return filterByType(type, input, (List<Object>) child);
                } else {
                    Object matched = ((Map<?, ?>) child).get(type);
                    if (!ObjectUtils.isEmpty(matched)) {
                        if (matched instanceof Collection) {
                            input.addAll((List<Object>) matched);
                        } else if (matched instanceof Map) {
                            input.add(matched);
                        }
                    }
                }
            }
        }
        return input;
    }


    @Override
    public List<Object> filterByType(Object type, List<Object> input, Map<String, Object> object) throws NullPointerException {
        if (type instanceof String) {
            Object subObject = object.get(type);
            if (!ObjectUtils.isEmpty(subObject)) {
                if (subObject instanceof Collection) {
                    input.addAll((List<Object>) subObject);
                }
            }
        }
        return input;
    }

    @Override
    public Map<String, Object> filterByType(Object type, Map<String, Object> input, List<Object> object) throws NullPointerException {
        for (Object child : object) {
            if (child instanceof Map) {
                Object id = ((Map<?, ?>) child).get("id");
                if (id instanceof String && !ObjectUtils.isEmpty(id)) {
                    //if not created already
                    if (ObjectUtils.isEmpty(input.get(id))) {
                        List<Object> ruleListObj = new ArrayList<>();
                        ruleListObj = filterByType(type, ruleListObj, (Map<String, Object>) child);
                        input.put(id.toString(), ruleListObj);
                    }
                }
            } else if (child instanceof Collection) {
                return filterByType(type, input, (List<Object>) child);
            }
        }
        return input;
    }

    @Override
    public Map<String, Object> filterByType(Object type, Map<String, Object> input, Map<String, Object> object) throws NullPointerException {
        for (Map.Entry<String, Object> subObject : object.entrySet()) {
            if (subObject instanceof Collection) {
                Object id = ((Map<?, ?>) subObject).get("id");
                if (id instanceof String && !ObjectUtils.isEmpty(id)) {
                    //if not created already
                    if (ObjectUtils.isEmpty(input.get(id))) {
                        List<Object> ruleListObj = new ArrayList<>();
                        ruleListObj = filterByType(type, ruleListObj, (List<Object>) subObject);
                        input.put(id.toString(), ruleListObj);
                    }
                }
            }
        }
        return input;
    }

}
