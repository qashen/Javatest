package pricing.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pricing.rule.ActionDetails;
import pricing.ruleEngine.Pattern;
import pricing.ruleEngine.Rule;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static pricing.util.constants.*;

@SuppressWarnings({"unchecked", "rawtypes"})
@Slf4j
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
        String strTagValue = String.valueOf(ruleIndex.get(TAG_PRIORITY));
        mapProperties.put(TAG_PRIORITY, strTagValue);
        strTagValue = String.valueOf(ruleIndex.get(TAG_INTER_GROUP));
        mapProperties.put(TAG_INTER_GROUP, strTagValue);
        strTagValue = String.valueOf(ruleIndex.get(TAG_GROUP_NAME));
        mapProperties.put(TAG_GROUP_NAME, strTagValue);
        strTagValue = String.valueOf(ruleIndex.get(TAG_ID));
        mapProperties.put(TAG_ID, strTagValue);
        strTagValue = String.valueOf(ruleIndex.get(TAG_INTRA_GROUP));
        mapProperties.put(TAG_INTRA_GROUP, strTagValue);
        strTagValue = String.valueOf(ruleIndex.get(TAG_CRITERIA_NAME));
        mapProperties.put(TAG_CRITERIA_NAME, strTagValue);
        strTagValue = String.valueOf(ruleIndex.get(TAG_CRITERIA_OP));
        mapProperties.put(TAG_CRITERIA_OP, strTagValue);
        strTagValue = String.valueOf(ruleIndex.get(TAG_CRITERIA_VAL));
        mapProperties.put(TAG_CRITERIA_VAL, strTagValue);
        strTagValue = String.valueOf(ruleIndex.get(TAG_CRITERIA_PARA));
        mapProperties.put(TAG_CRITERIA_PARA, strTagValue);
        strTagValue = String.valueOf(ruleIndex.get(TAG_ACTION));
        mapProperties.put(TAG_ACTION, strTagValue);
        strTagValue = String.valueOf(ruleIndex.get(TAG_ACTION_TYPE));
        mapProperties.put(TAG_ACTION_TYPE, strTagValue);
        strTagValue = String.valueOf(ruleIndex.get(TAG_ACTION_VAL));
        mapProperties.put(TAG_ACTION_VAL, strTagValue);
        strTagValue = String.valueOf(ruleIndex.get(TAG_ACTION_OBJ_TYPE));
        mapProperties.put(TAG_ACTION_OBJ_TYPE, strTagValue);
        strTagValue = String.valueOf(ruleIndex.get(TAG_ACTION_VAL_OBJ));
        mapProperties.put(TAG_ACTION_VAL_OBJ, strTagValue);
        strTagValue = String.valueOf(ruleIndex.get(TAG_ACTION_APPLIES_TO));
        mapProperties.put(TAG_ACTION_APPLIES_TO, strTagValue);

        LinkedHashMap<String, Object> criteriaField = (LinkedHashMap<String, Object>) ruleIndex.get(TAG_CRITERIA_FLD);
        for (Map.Entry field : criteriaField.entrySet()) {
            if (!Objects.isNull(field.getKey())) {
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
                        if (!ObjectUtils.isEmpty(((LinkedHashMap<?, ?>) item).get(m.getKey()))) {
                            ((LinkedHashMap) item).put(m.getValue(), ((LinkedHashMap<?, ?>) item).get(m.getKey()));
                        }
                    }
                }
            }
        }
    }

    public static void SetPattern(List<Object> input, List<Pattern> patternList, Map<String, String> mapProperties, Map<String, String> mapFields) {
        for (Object child : input) {
            Pattern pattern = new Pattern();
            List<Rule> ruleList = new ArrayList<>();
            if (!Objects.isNull(((LinkedHashMap) child).get(mapProperties.get(TAG_INTER_GROUP)))) {
                pattern.setLogicalRelationship(((LinkedHashMap) child).get(mapProperties.get(TAG_INTER_GROUP)).toString());
            }
            if (!Objects.isNull(((LinkedHashMap) child).get(mapProperties.get(TAG_PRIORITY))) &&
                    !((LinkedHashMap) child).get(mapProperties.get(TAG_PRIORITY)).toString().isEmpty()) {
                pattern.setPriority(Integer.parseInt(((LinkedHashMap) child).get(mapProperties.get(TAG_PRIORITY)).toString()));
            }
            SetRule((LinkedHashMap<String, Object>) child, ruleList, mapProperties, mapFields);
            pattern.setRuleListSet(ruleList);
            patternList.add(pattern);
        }
    }

    public static void SetRule(LinkedHashMap<String, Object> input, List<Rule> ruleList, Map<String, String> mapProperties, Map<String, String> mapFields) {
        AtomicReference<String> Input = new AtomicReference<>();
        HashMap<String, ActionDetails> actionMaps = new HashMap<>();
        HashMap<String, String> operator = new HashMap<>();
        operator.put(Operator.EQUALS.toString(), Operator.EQUALS.getMathsymbol());
        for (Object child : (List<Object>) input.get(mapProperties.get(TAG_GROUP_NAME))) {
            if (!ObjectUtils.isEmpty(child) && !Objects.isNull(((LinkedHashMap) child).get(mapProperties.get(TAG_ID))) &&
                    !((LinkedHashMap) child).get(mapProperties.get(TAG_ID)).toString().isEmpty()) {
                Rule rule = new Rule();
                final StringJoiner[] stringJoiner = {new StringJoiner("")};
                int counter = 0;
                List<Object> criteriaList = (List<Object>) ((LinkedHashMap) child).get(mapProperties.get(TAG_CRITERIA_NAME));
                if (!ObjectUtils.isEmpty(criteriaList)) {
                    for (Object criteria : criteriaList) {
                        if (counter != 0) {
                            if (((LinkedHashMap) child).get(mapProperties.get(TAG_INTRA_GROUP)).toString().compareToIgnoreCase(constants.AND) == 0) {
                                stringJoiner[0].add(" && ");
                            } else if (((LinkedHashMap) child).get(mapProperties.get(TAG_INTRA_GROUP)).toString().compareToIgnoreCase(constants.OR) == 0) {
                                stringJoiner[0].add(" || ");
                            }
                        }
                        if (ObjectUtils.isNumeric(((LinkedHashMap) criteria).get(mapProperties.get(TAG_CRITERIA_VAL)).toString())) {
                            Input.set("input." + mapFields.get(((LinkedHashMap) criteria).get(mapProperties.get(TAG_CRITERIA_PARA)).toString()) + " " +
                                    ((LinkedHashMap) criteria).get(mapProperties.get(TAG_CRITERIA_OP)).toString() + " " +
                                    ((LinkedHashMap) criteria).get(mapProperties.get(TAG_CRITERIA_VAL)).toString());
                        } else if (!ObjectUtils.isNumeric(((LinkedHashMap) criteria).get(mapProperties.get(TAG_CRITERIA_VAL)).toString())) {
                            Input.set("input." + mapFields.get(((LinkedHashMap) criteria).get(mapProperties.get(TAG_CRITERIA_PARA)).toString()) + " " +
                                    ((LinkedHashMap) criteria).get(mapProperties.get(TAG_CRITERIA_OP)).toString() + " '" +
                                    ((LinkedHashMap) criteria).get(mapProperties.get(TAG_CRITERIA_VAL)).toString() + "'");

                        } else {
                            Input.set("");
                        }
                        operator.forEach((key, value) -> {
                            String convertedRule = java.util.regex.Pattern.compile(key).matcher(Input.get()).replaceAll(value);
                            stringJoiner[0].add(convertedRule);
                        });
                        counter++;
                    }
                }
                if (!Objects.isNull(((LinkedHashMap) child).get(mapProperties.get(TAG_ID)))) {
                    rule.setRuleId(((LinkedHashMap) child).get(mapProperties.get(TAG_ID)).toString());
                }
                rule.setCondition(stringJoiner[0].toString());
                SetActions(input, rule, actionMaps, mapProperties);
                ruleList.add(rule);
            }
        }
    }

    public static void SetActions(LinkedHashMap<String, Object> input, Rule rule, HashMap<String, ActionDetails> actionMaps, Map<String, String> mapProperties) {
        SetActions(input, actionMaps, mapProperties);
        List<String> actionSet = new ArrayList<>();
        AtomicReference<String> actionInput = new AtomicReference<>();
        actionMaps.forEach((key, value) -> {
            String actionExpression = value.getExpression().replace(constants.INPUT_ARGS, "input.Price");
            actionInput.set("map.put(\"Price\"," + actionExpression + ");map.put(\"Adjustment Type\",\"" + value.getAdjustmentType() + "\");map.put(\"Adjustment Amount\"," + value.getAdjustmentValue() + ");");
            actionSet.add(actionInput.toString());
        });

        rule.setActionSet(actionSet);
    }

    public static void SetActions(LinkedHashMap<String, Object> input, HashMap<String, ActionDetails> actionMaps, Map<String, String> mapProperties) {
        AtomicReference<String> expression = new AtomicReference<>();
        for (Object child : (List<Object>) input.get(mapProperties.get(TAG_ACTION))) {
            if (!ObjectUtils.isEmpty(child) && !((LinkedHashMap) child).get(mapProperties.get(TAG_ID)).toString().isEmpty()) {
                ActionDetails actionDetails = new ActionDetails();
                BigDecimal actionValue = new BigDecimal((((LinkedHashMap) child).get(mapProperties.get(TAG_ACTION_VAL)).toString()));
                actionDetails.setAdjustmentValue(actionValue);
                Object actionTypeList = ((LinkedHashMap) child).get(mapProperties.get(TAG_ACTION_TYPE));
                if (!ObjectUtils.isEmpty(actionTypeList)) {
                    if (actionTypeList instanceof Collection) {
                        actionDetails.setAdjustmentType(((List<Object>) actionTypeList).get(0).toString());
                    } else if (actionTypeList instanceof String) {
                        actionDetails.setAdjustmentType((String) actionTypeList);
                    }
                }
                List<Object> actionValueObjList = (List<Object>) ((LinkedHashMap) child).get(mapProperties.get(TAG_ACTION_VAL_OBJ));
                if (!ObjectUtils.isEmpty(actionValueObjList)) {
                    for (Object actionValueObj : actionValueObjList) {
                        actionDetails.setPriceFieldName(((LinkedHashMap) actionValueObj).get(mapProperties.get(TAG_ACTION_OBJ_TYPE)).toString());
                    }
                }
                actionDetails.setExpression(getActionExpression(expression, actionDetails));
                actionMaps.put((((LinkedHashMap) child).get(mapProperties.get(TAG_ID)).toString()), actionDetails);
            }
        }
    }

    public static String getActionExpression(AtomicReference<String> Input, ActionDetails actionDetails) {
        BigDecimal ratio;
        BigDecimal one = new BigDecimal(1);
        switch (actionDetails.getAdjustmentType().toUpperCase()) {
            case "DISCOUNT":
            case ADJUSTMENT_TYPE_PERCENTAGE: {
                ratio = one.subtract(actionDetails.getAdjustmentValue());
                Input.set("new java.math.BigDecimal(" + constants.INPUT_ARGS + " * " + ratio + ")");
                break;
            }
            case "FIXED_MARKUP":
            case ADJUSTMENT_TYPE_MARKUP: {
                Input.set("new java.math.BigDecimal(" + constants.INPUT_ARGS + " + " + actionDetails.getAdjustmentValue());
                break;
            }
            case "OVERRIDE":
            case ADJUSTMENT_TYPE_OVERRIDE: {
                Input.set(actionDetails.getAdjustmentValue().toString());
                break;
            }
            case "PERCENT_MARKUP":
            case ADJUSTMENT_TYPE_MARKUP_PERCENTAGE: {
                ratio = one.add(actionDetails.getAdjustmentValue());
                Input.set("new java.math.BigDecimal(" + constants.INPUT_ARGS + " * " + ratio);
                break;
            }
            case "REDUCTION":
            case ADJUSTMENT_TYPE_DISCOUNT: {
                Input.set("new java.math.BigDecimal(" + constants.INPUT_ARGS + " - " + actionDetails.getAdjustmentValue());
                break;
            }
            default: {break;
            }
        }
        return Input.toString();
    }
}