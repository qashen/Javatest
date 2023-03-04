package pricing.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pricing.rule.ActionDetails;
import pricing.ruleEngine.Rule;
import pricing.ruleEngine.RuleNamespace;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
@SuppressWarnings({ "unchecked", "rawtypes" })
public class PricingUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(PricingUtil.class);

    private static final ObjectMapper mapper = new ObjectMapper()
            .setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    public static String convertObjectToJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException jsonError) {
            LOGGER.error("Error converting object to json {}", jsonError.getMessage());
            throw new RuntimeException("Obj_JSON_Err");
        }
    }

    public static <T> T convertJsonToObject(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (JsonProcessingException jsonError) {
            LOGGER.error("Error converting json to object {}", jsonError.getMessage());
            throw new RuntimeException("JSON_Obj_Err");
        }
    }

    public static <T> T convertJsonMapToObject(Map<String,Object> jsonMap, Class<T> clazz) {
        try {
            String json = convertObjectToJson(jsonMap);
            return mapper.readValue(json, clazz);
        } catch (JsonProcessingException jsonError) {
            LOGGER.error("Error converting json to object {}", jsonError.getMessage());
            throw new RuntimeException("JSONMap_Obj_Err");
        }
    }

    public static void setFields(Object from, Object to) {
        Field[] fields = from.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                Field fieldFrom = from.getClass().getDeclaredField(field.getName());
                Object value = fieldFrom.get(from);
                to.getClass().getDeclaredField(field.getName()).set(to, value);

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                //e.printStackTrace();
            }
        }
    }

    public static void setFields(LinkedHashMap from, LinkedHashMap to) {
        to.putAll(from);
    }


    public static void SetRule(List<Object> pr, List<Rule> ruleList, Map<String, String> mapProperties) {
        AtomicReference<String> Input = new AtomicReference<>();
        HashMap<String, ActionDetails> actionMaps = new HashMap<>();
        HashMap<String, String> operator = new HashMap<>();
        operator.put(Operator.EQUALS.toString(), Operator.EQUALS.getMathsymbol());
        for (Object p : pr) {
            if (!((LinkedHashMap) p).get("id").toString().isEmpty()) {
                if (((LinkedHashMap) p).get("relationTypeAmongGroup").toString().compareToIgnoreCase(constants.OR) == 0) {
                    List<Object> criteriaGroupList = (List<Object>) ((LinkedHashMap) p).get("criteriaGroup");
                    if (criteriaGroupList != null && criteriaGroupList.size() > 0) {
                        for (Object criteriaGroup : criteriaGroupList) {
                            Rule rule = new Rule(true);
                            final StringJoiner[] stringJoiner = {new StringJoiner("")};
                            int counter = 0;
                            List<Object> criteriaList = (List<Object>) ((LinkedHashMap)criteriaGroup).get("criteria");
                            for (Object criteria : criteriaList) {
                                if (counter != 0) {
                                    if (((LinkedHashMap) criteriaGroup).get("relationTypeInGroup").toString().compareToIgnoreCase(constants.AND) == 0) {
                                        stringJoiner[0].add(" && ");
                                    } else if (((LinkedHashMap) criteriaGroup).get("relationTypeInGroup").toString().compareToIgnoreCase(constants.OR) == 0) {
                                        stringJoiner[0].add("||");
                                    }
                                }

                                if (((LinkedHashMap)criteria).get("valueType").toString().compareToIgnoreCase("NUMBER") == 0) {
                                    Input.set("input." + mapProperties.get(((LinkedHashMap)criteria).get("criteriaPara").toString())+ " " + ((LinkedHashMap)criteria).get("criteriaOperator").toString() + " " + ((LinkedHashMap)criteria).get("criteriaValue").toString());
                                } else if ((((LinkedHashMap)criteria).get("valueType").toString().compareToIgnoreCase("STRING") == 0)|| (((LinkedHashMap)criteria).get("valueType").toString().compareToIgnoreCase("PRODUCT_OFFER") == 0)) {
                                    Input.set("input." + mapProperties.get(((LinkedHashMap)criteria).get("criteriaPara").toString())+ " " + ((LinkedHashMap)criteria).get("criteriaOperator").toString() + " '" + ((LinkedHashMap)criteria).get("criteriaValue").toString() + "'");

                                } else
                                    Input.set("");
                                operator.forEach((key, value) -> {
                                    String convertedRule = java.util.regex.Pattern.compile(key).matcher(Input.get()).replaceAll(value);
                                    stringJoiner[0].add(convertedRule);
                                });
                                counter++;
                            }
                            rule.setRuleNamespace(RuleNamespace.valueOf("PRICING"));
                            rule.setRuleId(((LinkedHashMap)criteriaGroup).get("id").toString());
                            rule.setCondition(stringJoiner[0].toString());
                            SetActions(pr, rule, actionMaps);
                            ruleList.add(rule);
                        }
                    } else if (((LinkedHashMap) p).get("relationTypeAmongGroup").toString().compareToIgnoreCase(constants.AND) == 0) {
                        System.out.println("TODO");
                    }
                }
            }
        }
    }

    public static void SetActions(List<Object> pr, Rule rule, HashMap<String, ActionDetails> actionMaps) {
        SetActions(pr, actionMaps);
        List<String> actionSet = new ArrayList<>();
        AtomicReference<String> actionInput = new AtomicReference<>();
        actionMaps.forEach((key, value) -> {
            String actionExpression = value.getExpression().replace("{Input}", "input.currentPrice");
            actionInput.set ("map.put(\"currentPrice\"," + actionExpression + ");");
            actionSet.add (actionInput.toString());
        });

        rule.setActionSet(actionSet);
    }

    public static void SetActions(List<Object> pr, HashMap<String, ActionDetails> actionMaps) {
        AtomicReference<String> Input = new AtomicReference<>();
        for (Object p : pr) {
            if (!((LinkedHashMap) p).get("id").toString().isEmpty()) {
                List<Object> actionList = (List<Object>) ((LinkedHashMap) p).get("action");
                if (actionList != null && actionList.size() > 0) {
                    for (Object action : actionList) {
                        ActionDetails actionDetails = new ActionDetails();
                        double	d = Double.parseDouble(((LinkedHashMap) action).get("actionValue").toString());
                        actionDetails.setAdjustmentValue(d);
                        List<Object> actionTypeList = (List<Object>) ((LinkedHashMap) action).get("actionType");
                        if (actionTypeList.size() > 0) {
                            actionDetails.setAdjustmentType(actionTypeList.get(0).toString());
                        }
                        List<Object> actionValueObjList = (List<Object>) ((LinkedHashMap) action).get("actionValueObj");
                        if (actionValueObjList != null && actionValueObjList.size() > 0) {
                            for (Object actionValueObj : actionValueObjList) {
                                actionDetails.setPriceFieldName(((LinkedHashMap) actionValueObj).get("actionObjectType").toString());
                                actionDetails.setPriceType(((LinkedHashMap) actionValueObj).get("appliesTo").toString());
                            }
                        }
                        actionDetails.setExpression(getActionExpression(Input, actionDetails));
                        actionMaps.put((((LinkedHashMap) action).get("id").toString()), actionDetails);
                    }
                }
            }
        }
    }

    public static String getActionExpression(AtomicReference<String> Input, ActionDetails actionDetails) {
        double ratio;
        switch (actionDetails.getAdjustmentType()) {
            case "DISCOUNT" -> {
                ratio = 1 - (Double) actionDetails.getAdjustmentValue();
                Input.set(constants.INPUT_ARGS + " * " + ratio);
            }
            case "FIXED_MARKUP" -> Input.set(constants.INPUT_ARGS + " + " + actionDetails.getAdjustmentValue().toString());
            case "OVERRIDE" -> Input.set(actionDetails.getAdjustmentValue().toString());
            case "PERCENT_MARKUP" -> {
                ratio = 1 + (Double) actionDetails.getAdjustmentValue();
                Input.set(constants.INPUT_ARGS + " * " + ratio);
            }
            case "REDUCTION" -> Input.set(constants.INPUT_ARGS + " - " + actionDetails.getAdjustmentValue());
            default -> {
            }
        }
        return Input.toString();
    }
}