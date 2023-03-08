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

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static pricing.util.constants.*;

@SuppressWarnings({"unchecked", "rawtypes"})
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

    public static <T> T convertJsonMapToObject(Map<String, Object> jsonMap, Class<T> clazz) {
        try {
            String json = convertObjectToJson(jsonMap);
            return mapper.readValue(json, clazz);
        } catch (JsonProcessingException jsonError) {
            LOGGER.error("Error converting json to object {}", jsonError.getMessage());
            throw new RuntimeException("JSONMap_Obj_Err");
        }
    }


    public static void addToRef(LinkedHashMap<String, Object> ruleIndex,
                                Map<String, String> mapProperties,
                                Map<String, String> mapRuleFields,
                                Map<String, String> mapInputFields) {
        String strTagValue = String.valueOf(ruleIndex.get (TAG_INTER_GROUP));
        mapProperties.put(TAG_INTER_GROUP, strTagValue);
        strTagValue = String.valueOf(ruleIndex.get (TAG_GROUP_NAME));
        mapProperties.put(TAG_GROUP_NAME, strTagValue);
        strTagValue = String.valueOf(ruleIndex.get (TAG_ID));
        mapProperties.put(TAG_ID, strTagValue);
        strTagValue = String.valueOf(ruleIndex.get (TAG_INTRA_GROUP));
        mapProperties.put(TAG_INTRA_GROUP, strTagValue);
        strTagValue = String.valueOf(ruleIndex.get (TAG_CRITERIA_NAME));
        mapProperties.put(TAG_CRITERIA_NAME, strTagValue);
        strTagValue = String.valueOf(ruleIndex.get (TAG_VALUE_TYPE));
        mapProperties.put(TAG_VALUE_TYPE, strTagValue);
        strTagValue = String.valueOf(ruleIndex.get (TAG_CRITERIA_OP));
        mapProperties.put(TAG_CRITERIA_OP, strTagValue);
        strTagValue = String.valueOf(ruleIndex.get (TAG_CRITERIA_VAL));
        mapProperties.put(TAG_CRITERIA_VAL, strTagValue);
        strTagValue = String.valueOf(ruleIndex.get (TAG_CRITERIA_PARA));
        mapProperties.put(TAG_CRITERIA_PARA, strTagValue);
        strTagValue = String.valueOf(ruleIndex.get (TAG_ACTION));
        mapProperties.put(TAG_ACTION, strTagValue);
        LinkedHashMap<String, Object> criteriaField = (LinkedHashMap<String, Object>)ruleIndex.get (TAG_CRITERIA_FLD);
        for (Map.Entry field : criteriaField.entrySet()) {
            if (!Objects.isNull (field.getKey())) {
                mapRuleFields.put(field.getKey().toString(), field.getKey().toString().replaceAll("\\s", ""));
                mapInputFields.put(field.getValue().toString(), field.getKey().toString().replaceAll("\\s", ""));
            }
        }
    }

    public static void setFields(Map<String, String> from, List<Object> to) {
        if (to.size() > 0) {
            for (Object item : to) {
                for (Map.Entry<String, String> m : from.entrySet()) {
                    {
                        if (((LinkedHashMap<?, ?>) item).get(m.getKey()) != null) {
                            ((LinkedHashMap) item).put(m.getValue(), ((LinkedHashMap<?, ?>) item).get(m.getKey()));
                        }
                    }
                }
            }
        }
    }

    public static void SetRule(List<Object> pr, List<Rule> ruleList, Map<String, String> mapProperties, Map<String, String> mapFields) {
        AtomicReference<String> Input = new AtomicReference<>();
        HashMap<String, ActionDetails> actionMaps = new HashMap<>();
        HashMap<String, String> operator = new HashMap<>();
        operator.put(Operator.EQUALS.toString(), Operator.EQUALS.getMathsymbol());
        for (Object p : pr) {
            if (!Objects.isNull(((LinkedHashMap) p).get(mapProperties.get(TAG_ID))) &&
                    !((LinkedHashMap) p).get(mapProperties.get(TAG_ID)).toString().isEmpty()) {
                if (!Objects.isNull(((LinkedHashMap) p).get(mapProperties.get(TAG_INTER_GROUP))) &&
                        ((LinkedHashMap) p).get(mapProperties.get(TAG_INTER_GROUP)).toString().compareToIgnoreCase(constants.OR) == 0) {
                    List<Object> criteriaGroupList = (List<Object>) ((LinkedHashMap) p).get(mapProperties.get(TAG_GROUP_NAME));
                    if (criteriaGroupList != null && criteriaGroupList.size() > 0) {
                        for (Object criteriaGroup : criteriaGroupList) {
                            Rule rule = new Rule(true);
                            final StringJoiner[] stringJoiner = {new StringJoiner("")};
                            int counter = 0;
                            List<Object> criteriaList = (List<Object>) ((LinkedHashMap) criteriaGroup).get(mapProperties.get(TAG_CRITERIA_NAME));
                            if (criteriaList != null && criteriaList.size() > 0) {
                                for (Object criteria : criteriaList) {
                                    if (counter != 0) {
                                        if (((LinkedHashMap) criteriaGroup).get(mapProperties.get(TAG_INTRA_GROUP)).toString().compareToIgnoreCase(constants.AND) == 0) {
                                            stringJoiner[0].add(" && ");
                                        } else if (((LinkedHashMap) criteriaGroup).get(mapProperties.get(TAG_INTRA_GROUP)).toString().compareToIgnoreCase(constants.OR) == 0) {
                                            stringJoiner[0].add(" || ");
                                        }
                                    }
                                    if (((LinkedHashMap) criteria).get(mapProperties.get(TAG_VALUE_TYPE)).toString().compareToIgnoreCase("NUMBER") == 0) {
                                        Input.set("input." + mapFields.get(((LinkedHashMap) criteria).get(mapProperties.get(TAG_CRITERIA_PARA)).toString()) + " " +
                                                ((LinkedHashMap) criteria).get(mapProperties.get(TAG_CRITERIA_OP)).toString() + " " +
                                                ((LinkedHashMap) criteria).get(mapProperties.get(TAG_CRITERIA_VAL)).toString());
                                    } else if ((((LinkedHashMap) criteria).get(mapProperties.get(TAG_VALUE_TYPE)).toString().compareToIgnoreCase("STRING") == 0) ||
                                            (((LinkedHashMap) criteria).get(mapProperties.get(TAG_VALUE_TYPE)).toString().compareToIgnoreCase("PRODUCT_OFFER") == 0)) {
                                        Input.set("input." + mapFields.get(((LinkedHashMap) criteria).get(mapProperties.get(TAG_CRITERIA_PARA)).toString()) + " " +
                                                ((LinkedHashMap) criteria).get(mapProperties.get(TAG_CRITERIA_OP)).toString() + " '" +
                                                ((LinkedHashMap) criteria).get(mapProperties.get(TAG_CRITERIA_VAL)).toString() + "'");

                                    } else
                                        Input.set("");
                                    operator.forEach((key, value) -> {
                                        String convertedRule = java.util.regex.Pattern.compile(key).matcher(Input.get()).replaceAll(value);
                                        stringJoiner[0].add(convertedRule);
                                    });
                                    counter++;
                                }
                            }
                            rule.setRuleNamespace(RuleNamespace.valueOf("PRICING"));
                            if (!Objects.isNull(((LinkedHashMap) criteriaGroup).get(mapProperties.get(TAG_ID)))) {
                                rule.setRuleId(((LinkedHashMap) criteriaGroup).get(mapProperties.get(TAG_ID)).toString());
                            }
                            rule.setCondition(stringJoiner[0].toString());
                            SetActions(pr, rule, actionMaps);
                            ruleList.add(rule);
                        }
                    } else if (!Objects.isNull(((LinkedHashMap) p).get(mapProperties.get(TAG_INTER_GROUP))) &&
                            ((LinkedHashMap) p).get(mapProperties.get(TAG_INTER_GROUP)).toString().compareToIgnoreCase(constants.AND) == 0) {
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
            String actionExpression = value.getExpression().replace(constants.INPUT_ARGS, "input.Price");
            actionInput.set("map.put(\"Price\"," + actionExpression + ");map.put(\"Adjustment Type\",\"" + value.getAdjustmentType() + "\");map.put(\"Adjustment Amount\"," + value.getAdjustmentValue() + ");");
            actionSet.add(actionInput.toString());
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
                Input.set("(double)" + constants.INPUT_ARGS + " * " + ratio);
            }
            case "FIXED_MARKUP" ->
                    Input.set("(double)" + constants.INPUT_ARGS + " + " + actionDetails.getAdjustmentValue());
            case "OVERRIDE" -> Input.set(actionDetails.getAdjustmentValue().toString());
            case "PERCENT_MARKUP" -> {
                ratio = 1 + (Double) actionDetails.getAdjustmentValue();
                Input.set("(double)" + constants.INPUT_ARGS + " * " + ratio);
            }
            case "REDUCTION" ->
                    Input.set("(double)" + constants.INPUT_ARGS + " - " + actionDetails.getAdjustmentValue());
            default -> {
            }
        }
        return Input.toString();
    }
}