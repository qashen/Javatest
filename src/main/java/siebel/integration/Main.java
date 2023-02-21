package siebel.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import siebel.integration.rule.*;
import siebel.integration.ruleEngine.KnowledgeBaseService;
import siebel.integration.ruleEngine.Rule;
import siebel.integration.ruleEngine.RuleEngine;
import siebel.integration.ruleEngine.RuleNamespace;
import siebel.integration.rulesImpl.LoanInferenceEngine;
import siebel.integration.rulesImpl.PricingInferenceEngine;
import siebel.integration.rulesImpl.Users;
import siebel.integration.util.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static siebel.integration.util.constants.INPUT_ARGS;

public class Main {
    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            SiebelMessage siebel = new SiebelMessage();
            ClassLoader classLoader = siebel.getClass().getClassLoader();
            InputStream is = classLoader.getResourceAsStream("siebelmessage.json");
            SiebelMessage SR = mapper.readValue(is, SiebelMessage.class);
            IO list = SR.getIo();

            ListOfIC listOfIC = SR.getIo().getListOfICS();
            LineItemIC lineItemIC = listOfIC.getLineItemICS()[0];
            Map<String, String> mapProperties = lineItemIC.getMapJsonPropertyField();

            //OnMatch one = new OnMatch();
            //one.onMatch(mapProperties, "Id", "dummyId");


            PatternRule pr = new PatternRule();
            classLoader = pr.getClass().getClassLoader();
            is = classLoader.getResourceAsStream("pattern.json");
            pr = mapper.readValue(is, PatternRule.class);
            String str = pr.getPatterns()[0].getId();
            //System.out.println(str);

            HashMap<String, Object> ruleMaps = new HashMap<>();
            HashMap<String, ActionDetails> actionMaps = new HashMap<>();
            SetRuleNodes(pr, ruleMaps);
            SetActions(pr, actionMaps);

            HashMap<String, String> operator = new HashMap<>();
            operator.put(Operator.EQUALS.toString(), Operator.EQUALS.getMathsymbol());


            List<Rule> ruleList = new ArrayList<>();

            AtomicReference<String> criteriaInput = new AtomicReference<>();
            AtomicReference<String> actionInput = new AtomicReference<>();
            final StringJoiner[] stringJoiner = {new StringJoiner("")};
            for (Map.Entry m : ruleMaps.entrySet()) {
                //myInput = java.util.regex.Pattern.compile (m.getKey().toString()).matcher(myInput).replaceAll(m.getValue().toString());
                TreeMap sortedMap = (TreeMap) m.getValue();
                AtomicReference<String> finalCriteriaInput = criteriaInput;
                sortedMap.forEach((key1, value) -> {
                    RuleNode ruleNode = (RuleNode) value;
                    String myInput = "";
                    if (ruleNode.getRuleType().equals("CRITERIA")) {
                        RuleValue ruleValue = ruleNode.getRuleValue();
                        if (ruleValue.getValueType().equals("NUMBER")) {
                            finalCriteriaInput.set("input." + mapProperties.get(ruleValue.getCriteriaPara()) + " " + ruleValue.getCriteriaOperator() + " " + ruleValue.getCriteriaValue());
                        } else if (ruleValue.getValueType().equals("STRING") || ruleValue.getValueType().equals("PRODUCT_OFFER")) {
                            finalCriteriaInput.set("input." + mapProperties.get(ruleValue.getCriteriaPara()) + " " + ruleValue.getCriteriaOperator() + " '" + ruleValue.getCriteriaValue() + "'");
                        } else
                            finalCriteriaInput.set("");

                        String criteria = finalCriteriaInput.get();
                        for (Map.Entry mapEntry : operator.entrySet()) {
                            myInput = java.util.regex.Pattern.compile(mapEntry.getKey().toString()).matcher(criteria).replaceAll(mapEntry.getValue().toString());
                        }
                    }
                    else if (ruleNode.getRuleType().equals("AND"))
                    {
                        stringJoiner[0].add (" && ");
                    }
                    else if (ruleNode.getRuleType().equals("OR"))
                    {
                        stringJoiner[0].add (" || ");
                    }
                    else if (ruleNode.getRuleType().equals("GROUP_OR")) {
                        System.out.println(stringJoiner[0].toString());
                        Rule rule = new Rule(true);
                        rule.setCondition(stringJoiner[0].toString());
                        rule.setRuleNamespace(RuleNamespace.valueOf("PRICING"));
                        rule.setRuleId(ruleNode.getCriteriaGroupId());
                        List<String> actionSet = new ArrayList<>();
                        //actionSet.add("output.setExternalIntegrationId(input.externalIntegrationId);");
                        //actionSet.add("output.setOriginalListPrice(input.originalListPrice * 0.95);");
                        for (Map.Entry actionMap : actionMaps.entrySet())
                        {
                            ActionDetails actionDetails = (ActionDetails) actionMap.getValue();
                            String actionExpression = actionDetails.getExpression().replace ("{Input}", "input.originalListPrice");
                            System.out.println (actionExpression);
                            actionInput.set ("output.setOriginalListPrice" + "(" + actionExpression + ");");
                            actionSet.add(actionInput.toString());
                        }
                        rule.setActionSet(actionSet);
                        ruleList.add(rule);
                        stringJoiner[0] = new StringJoiner("");
                    }
                    stringJoiner[0].add(myInput);
                });
            }


            System.out.println(stringJoiner[0].toString());
            Rule rule = new Rule(true);
            rule.setCondition(stringJoiner[0].toString());
            rule.setRuleNamespace(RuleNamespace.valueOf("PRICING"));
            rule.setRuleId("TOBECHANGEDLATER");
            List<String> actionSet = new ArrayList<>();
            //actionSet.add("output.setExternalIntegrationId(input.externalIntegrationId);");
            //actionSet.add("output.setOriginalListPrice(input.originalListPrice * 0.95);");
            for (Map.Entry actionMap : actionMaps.entrySet())
            {
                ActionDetails actionDetails = (ActionDetails) actionMap.getValue();
                String actionExpression = actionDetails.getExpression().replace ("{Input}", "input.originalListPrice");
                System.out.println (actionExpression);
                actionInput.set ("output.setOriginalListPrice" + "(" + actionExpression + ");");
                actionSet.add(actionInput.toString());
            }
            rule.setActionSet(actionSet);
            ruleList.add(rule);

            System.out.println("Below is rule engine test");

            KnowledgeBaseService knowledgeBaseService = new KnowledgeBaseService();
            classLoader = knowledgeBaseService.getClass().getClassLoader();
            is = classLoader.getResourceAsStream("knowledgebase.json");
            knowledgeBaseService = mapper.readValue(is, KnowledgeBaseService.class);
            List<Rule> allRules = knowledgeBaseService.getAllRuleByNamespace("LOAN");
            List<Rule> allDefaultRules = knowledgeBaseService.getAllRuleByNamespace("DEFAULT");

            /*UserDetails userDetails = new UserDetails();
            classLoader = userDetails.getClass().getClassLoader();
            is = classLoader.getResourceAsStream("user.json");
            userDetails = mapper.readValue(is, UserDetails.class);*/

            Users user = new Users();
            classLoader = user.getClass().getClassLoader();
            is = classLoader.getResourceAsStream("users.json");
            user = mapper.readValue(is, Users.class);
            HashMap<String, Object> userMap = new HashMap<>();
            // List<UserDetails> allUser  = user.getAllUser();


            RuleEngine ruleEngine = new RuleEngine();
            //RuleParser ruleParser = new RuleParser();
            LoanInferenceEngine loanInferenceEngine = new LoanInferenceEngine();
            //LoanDetails result = (LoanDetails) ruleEngine.run(loanInferenceEngine, userDetails);

/*
            List<Object> resultS = ruleEngine.run(loanInferenceEngine, Collections.singletonList(user.getAllUser()));

            for (Object result : resultS) {
                System.out.print("Account Number: " + ((LoanDetails) result).getAccountNumber().toString() + "; ");
                System.out.print("Approval Status: " + ((LoanDetails) result).getApprovalStatus().toString() + "; ");
                System.out.print("Interest Rate %: " + ((LoanDetails) result).getInterestRate().toString() + "; ");
                System.out.print("Processing Fee: " + ((LoanDetails) result).getProcessingFees().toString() + "; ");
                System.out.print("Sanction % of requested loan amount: " + ((LoanDetails) result).getSanctionedPercentage().toString());
                System.out.println ();
            }
            System.out.println("End");

*/

            PricingInferenceEngine pricingInferenceEngine = new PricingInferenceEngine();

            List<Object> actionResults = ruleEngine.runCriteria(pricingInferenceEngine, Collections.singletonList(listOfIC.getAllLinItem()), Collections.singletonList(ruleList));
            Iterator<Object> iterator = actionResults.iterator();
            while(iterator.hasNext())
            {
                System.out.println(iterator.next());
            }
            /*for (Object result: actionResults) {
                //System.out.print("External Integration Id: " + ((LineItemInternal) result).getExternalIntegrationId().toString() + "; ");
                //System.out.print("Updated Price List: " + ((ArrayList) result.getOriginalListPrice().toString() + "; ");
                //System.out.print("Updated Price List: " + ((LineItemInternal)result[0]).getOriginalListPrice().toString() + "; ");

                System.out.println ();
            }*/
            System.out.println("End");


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void SetRuleNodes(PatternRule pr, HashMap<String, Object> ruleMaps) {
        RuleNode ruleNode;
        String patternId;
        int counter = 0;
        int criteriaGroupInit = 0;
        int criteriaInit = 0;
        SortedMap<Integer, RuleNode> ruleNodeSortedMap;
        for (Pattern p : pr.getPatterns()) {
            if (!p.getId().isEmpty()) {
                patternId = p.getId();
                ruleNodeSortedMap = new TreeMap<>();

                String typeAmongGroup = p.getRelationTypeAmongGroup();
                for (CriteriaGroup c : p.getCriteriaGroups()) {
                    if (criteriaGroupInit != 0) {
                        ruleNode = new RuleNode();
                        counter++;
                        ruleNode.setSequence(counter);
                        if (typeAmongGroup.equalsIgnoreCase(constants.AND)) {
                            ruleNode.setRuleType(RuleType.GROUP_AND.toString());
                        } else if (typeAmongGroup.equalsIgnoreCase(constants.OR)) {
                            ruleNode.setRuleType(RuleType.GROUP_OR.toString());
                        }
                        ruleNode.setCriteriaGroupId(c.getId());
                        ruleNode.setPatternId(patternId);
                        ruleNodeSortedMap.put(counter, ruleNode);
                    }
                    String getTypeInGroup = c.getRelationTypeInGroup();
                    for (Criteria cr : c.getCriteria()) {
                        if (criteriaInit != 0) {
                            ruleNode = new RuleNode();
                            counter++;
                            ruleNode.setSequence(counter);
                            ruleNode.setRuleType(getTypeInGroup);
                            ruleNode.setPatternId(patternId);
                            ruleNode.setCriteriaId(cr.getId());
                            ruleNode.setCriteriaGroupId(c.getId());
                            ruleNodeSortedMap.put(counter, ruleNode);
                        }
                        ruleNode = new RuleNode();
                        counter++;
                        ruleNode.setSequence(counter);
                        ruleNode.setRuleType(RuleType.CRITERIA.toString());
                        ruleNode.setPatternId(patternId);
                        RuleValue ruleValue = new RuleValue();
                        ruleValue.setCriteriaValue(cr.getCriteriaValue());
                        ruleValue.setValueType(cr.getValueType());
                        ruleValue.setCriteriaOperator(cr.getCriteriaOperator());
                        ruleValue.setCriteriaPara(cr.getCriteriaPara());
                        ruleNode.setRuleValue(ruleValue);
                        ruleNode.setCriteriaId(cr.getId());
                        ruleNode.setCriteriaGroupId(c.getId());
                        ruleNodeSortedMap.put(counter, ruleNode);
                        criteriaInit++;
                    }
                    criteriaInit = 0; //reset
                    criteriaGroupInit++;
                }

                criteriaGroupInit = 0; //reset

                ruleNode = new RuleNode();
                counter++;
                ruleNode.setSequence(counter);
                ruleNode.setRuleType(RuleType.ROOT.toString());
                ruleNode.setPatternId(patternId);
                ruleNodeSortedMap.put(counter, ruleNode);

                ruleMaps.put(patternId, ruleNodeSortedMap);
            }
            counter = 0; //reset
        }

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
        Double ratio;
        switch (actionDetails.getAdjustmentType()) {
            case "DISCOUNT":
                ratio = 1 - (Double) actionDetails.getAdjustmentValue();
                Input.set(INPUT_ARGS + " * " + ratio);
                break;
            case "FIXED_MARKUP":
                Input.set (INPUT_ARGS + " + " + actionDetails.getAdjustmentValue().toString());
                break;
            case "OVERRIDE":
                Input.set (actionDetails.getAdjustmentValue().toString());
                break;
            case "PERCENT_MARKUP":
                ratio = 1 + (Double) actionDetails.getAdjustmentValue();
                Input.set(INPUT_ARGS + " * " + ratio);
                break;
            case "REDUCTION":
                Input.set (INPUT_ARGS + " - " + actionDetails.getAdjustmentValue());
                break;
            default:
        }
        return Input.toString();
    }
}
