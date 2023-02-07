package siebel.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import siebel.integration.rule.Criteria;
import siebel.integration.rule.CriteriaGroup;
import siebel.integration.rule.Pattern;
import siebel.integration.rule.PatternRule;
import siebel.integration.ruleEngine.KnowledgeBaseService;
import siebel.integration.ruleEngine.Rule;
import siebel.integration.ruleEngine.RuleEngine;
import siebel.integration.rulesImpl.LoanDetails;
import siebel.integration.rulesImpl.LoanInferenceEngine;
import siebel.integration.rulesImpl.UserDetails;
import siebel.integration.util.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class Main {
    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            SiebelMessage siebel = new SiebelMessage();
            ClassLoader classLoader = siebel.getClass().getClassLoader();
            InputStream is = classLoader.getResourceAsStream("siebelmessage.json");
            SiebelMessage SR = mapper.readValue(is, SiebelMessage.class);
            IO list = SR.getIo();

            LineItemIC lineItemIC = SR.getIo().getListOfICS().getLineItemICS()[0];
            Map<String, String> mapProperties = lineItemIC.getProperties();
            for (String propertyName : mapProperties.keySet()) {
                String propertyValue = mapProperties.get(propertyName);
                //System.out.println(propertyName + " is " + propertyValue);
            }

            //OnMatch one = new OnMatch();
            //one.onMatch(mapProperties, "Id", "dummyId");


            PatternRule pr = new PatternRule();
            classLoader = pr.getClass().getClassLoader();
            is = classLoader.getResourceAsStream("pattern.json");
            pr = mapper.readValue(is, PatternRule.class);
            String str = pr.getPatterns()[0].getId();
            //System.out.println(str);

            HashMap<String, Object> ruleMaps = new HashMap<>();
            SetRuleNodes(pr, ruleMaps);

            HashMap<String, String> operator = new HashMap<>();
            operator.put(Operator.EQUALS.toString(), Operator.EQUALS.getMathsymbol());

            AtomicReference<String> criteriaInput = new AtomicReference<>();
            final StringJoiner[] stringJoiner = {new StringJoiner("")};
            for (Map.Entry m : ruleMaps.entrySet()) {
                //myInput = java.util.regex.Pattern.compile (m.getKey().toString()).matcher(myInput).replaceAll(m.getValue().toString());
                TreeMap sortedMap = (TreeMap) m.getValue();
                AtomicReference<String> finalCriteriaInput = criteriaInput;
                sortedMap.forEach((key1, value) -> {
                    RuleNode ruleNode = (RuleNode) value;
                    String myInput = new String ();
                    if (ruleNode.getRuleType().equals("CRITERIA")) {
                        RuleValue ruleValue = ruleNode.getRuleValue();
                        if (ruleValue.getValueType().equals("NUMBER")) {
                            finalCriteriaInput.set("input." + ruleValue.getCriteriaPara() + " " + ruleValue.getCriteriaOperator() + " " + ruleValue.getCriteriaValue());
                        } else if (ruleValue.getValueType().equals("STRING") || ruleValue.getValueType().equals("PRODUCT_OFFER")) {
                            finalCriteriaInput.set("input." + ruleValue.getCriteriaPara() + " " + ruleValue.getCriteriaOperator() + " '" + ruleValue.getCriteriaValue() + "'");
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
                    else if (ruleNode.getRuleType().equals("GROUP_OR"))
                    {
                        System.out.println(stringJoiner[0].toString());
                        stringJoiner[0] = new StringJoiner("");
                    }
                    stringJoiner[0].add (myInput);
                });
            }

            System.out.println(stringJoiner[0].toString());

            System.out.println("Below is rule engine test");

            KnowledgeBaseService knowledgeBaseService = new KnowledgeBaseService();
            classLoader = knowledgeBaseService.getClass().getClassLoader();
            is = classLoader.getResourceAsStream("knowledgebase.json");
            knowledgeBaseService = mapper.readValue(is, KnowledgeBaseService.class);
            List<Rule> allRules = knowledgeBaseService.getAllRuleByNamespace("LOAN");
            List<Rule> allDefaultRules = knowledgeBaseService.getAllRuleByNamespace("DEFAULT");

            UserDetails userDetails = new UserDetails();
            classLoader = userDetails.getClass().getClassLoader();
            is = classLoader.getResourceAsStream("user.json");
            userDetails = mapper.readValue(is, UserDetails.class);

            RuleEngine ruleEngine = new RuleEngine();
            //RuleParser ruleParser = new RuleParser();
            LoanInferenceEngine loanInferenceEngine = new LoanInferenceEngine();
            LoanDetails result = (LoanDetails) ruleEngine.run(loanInferenceEngine, userDetails);

            System.out.println("Account Number: " + result.getAccountNumber().toString());
            System.out.println("Approval Status: " + result.getApprovalStatus().toString());
            System.out.println("Interest Rate %: " + result.getInterestRate().toString());
            System.out.println("Processing Fee: " + result.getProcessingFees().toString());
            System.out.println("Sanction % of requested loan amount: " + result.getSanctionedPercentage().toString());

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
}
