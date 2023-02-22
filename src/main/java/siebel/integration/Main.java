package siebel.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import siebel.integration.rule.*;
import siebel.integration.ruleEngine.Rule;
import siebel.integration.ruleEngine.RuleEngine;
import siebel.integration.ruleEngine.RuleNamespace;
import siebel.integration.rulesImpl.PricingInferenceEngine;
import siebel.integration.util.Operator;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static siebel.integration.util.constants.*;

public class Main {
    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            SiebelMessage siebel = new SiebelMessage();
            ClassLoader classLoader = siebel.getClass().getClassLoader();
            InputStream is = classLoader.getResourceAsStream("siebelmessage.json");
            SiebelMessage SR = mapper.readValue(is, SiebelMessage.class);

            ListOfIC listOfIC = SR.getIo().getListOfICS();
            LineItemIC lineItemIC = listOfIC.getLineItemICS()[0];
            Map<String, String> mapProperties = lineItemIC.getMapJsonPropertyField();

            PatternRule pr = new PatternRule();
            classLoader = pr.getClass().getClassLoader();
            is = classLoader.getResourceAsStream("pattern.json");
            pr = mapper.readValue(is, PatternRule.class);
            List<Rule> ruleList = new ArrayList<>();
            SetRule(pr, ruleList, mapProperties);

            System.out.println("Below is rule engine test");

            RuleEngine ruleEngine = new RuleEngine();
            PricingInferenceEngine pricingInferenceEngine = new PricingInferenceEngine();
            List<Object> actionResults = ruleEngine.runCriteria(pricingInferenceEngine, Collections.singletonList(listOfIC.getAllLinItem()), Collections.singletonList(ruleList));
            for (Object actionResult : actionResults) {
                System.out.println(actionResult);
            }
            System.out.println("End");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void SetRule(PatternRule pr, List<Rule> ruleList, Map<String, String> mapProperties) {
        AtomicReference<String> Input = new AtomicReference<>();
        HashMap<String, ActionDetails> actionMaps = new HashMap<>();
        HashMap<String, String> operator = new HashMap<>();
        operator.put(Operator.EQUALS.toString(), Operator.EQUALS.getMathsymbol());
        for (Pattern p : pr.getPatterns()) {
            if (!p.getId().isEmpty()) {
                if (p.getRelationTypeAmongGroup().compareToIgnoreCase(OR) == 0) {
                    for (CriteriaGroup criteriaGroup : p.getCriteriaGroups()) {
                        Rule rule = new Rule(true);
                        final StringJoiner[] stringJoiner = {new StringJoiner("")};
                        int counter = 0;
                        for (Criteria criteria : criteriaGroup.getCriteria()) {
                            if (counter != 0) {
                                if (criteriaGroup.getRelationTypeInGroup().compareToIgnoreCase(AND) == 0) {
                                    stringJoiner[0].add(" && ");
                                } else if (criteriaGroup.getRelationTypeInGroup().compareToIgnoreCase(OR) == 0) {
                                    stringJoiner[0].add("||");
                                }
                            }
                            if (criteria.getValueType().equals("NUMBER")) {
                                Input.set("input." + mapProperties.get(criteria.getCriteriaPara()) + " " + criteria.getCriteriaOperator() + " " + criteria.getCriteriaValue());
                            } else if (criteria.getValueType().equals("STRING") || criteria.getValueType().equals("PRODUCT_OFFER")) {
                                Input.set("input." + mapProperties.get(criteria.getCriteriaPara()) + " " + criteria.getCriteriaOperator() + " '" + criteria.getCriteriaValue() + "'");
                            } else
                                Input.set("");
                            operator.forEach((key, value) -> {
                                String convertedRule = java.util.regex.Pattern.compile(key).matcher(Input.get()).replaceAll(value);
                                stringJoiner[0].add(convertedRule);
                            });
                            counter++;
                        }
                        rule.setRuleNamespace(RuleNamespace.valueOf("PRICING"));
                        rule.setRuleId(criteriaGroup.getId());
                        rule.setCondition(stringJoiner[0].toString());
                        SetActions(pr, rule, actionMaps);
                        ruleList.add(rule);
                    }
                } else if (p.getRelationTypeAmongGroup().compareToIgnoreCase(AND) == 0) {
                    System.out.println("TODO");
                }

            }
        }
    }

    public static void SetActions(PatternRule pr, Rule rule, HashMap<String, ActionDetails> actionMaps) {
        SetActions(pr, actionMaps);
        List<String> actionSet = new ArrayList<>();
        AtomicReference<String> actionInput = new AtomicReference<>();
        actionMaps.forEach((key, value) -> {
            String actionExpression = value.getExpression().replace("{Input}", "input.originalListPrice");
            actionInput.set("output.setOriginalListPrice" + "(" + actionExpression + ");");
            actionSet.add(actionInput.toString());
        });
        rule.setActionSet(actionSet);
    }

    public static void SetActions(PatternRule pr, HashMap<String, ActionDetails> actionMaps) {
        AtomicReference<String> Input = new AtomicReference<>();
        for (Pattern p : pr.getPatterns()) {
            if (!p.getId().isEmpty()) {
                for (Action action : p.getActions()) {
                    ActionDetails actionDetails = new ActionDetails();
                    actionDetails.setAdjustmentValue(action.getActionValue());
                    actionDetails.setAdjustmentType(action.getActionType()[0].getActionTpe());
                    for (ActionValueObj actionValueObj : action.getActionValueObjs()) {
                        actionDetails.setPriceFieldName(actionValueObj.getActionObjectType());
                        actionDetails.setPriceType(actionValueObj.getAppliesTo());
                    }
                    actionDetails.setExpression(getActionExpression(Input, actionDetails));
                    actionMaps.put(action.getId(), actionDetails);
                }
            }
        }
    }

    public static String getActionExpression(AtomicReference<String> Input, ActionDetails actionDetails) {
        double ratio;
        switch (actionDetails.getAdjustmentType()) {
            case "DISCOUNT" -> {
                ratio = 1 - (Double) actionDetails.getAdjustmentValue();
                Input.set(INPUT_ARGS + " * " + ratio);
            }
            case "FIXED_MARKUP" -> Input.set(INPUT_ARGS + " + " + actionDetails.getAdjustmentValue().toString());
            case "OVERRIDE" -> Input.set(actionDetails.getAdjustmentValue().toString());
            case "PERCENT_MARKUP" -> {
                ratio = 1 + (Double) actionDetails.getAdjustmentValue();
                Input.set(INPUT_ARGS + " * " + ratio);
            }
            case "REDUCTION" -> Input.set(INPUT_ARGS + " - " + actionDetails.getAdjustmentValue());
            default -> {
            }
        }
        return Input.toString();
    }
}
