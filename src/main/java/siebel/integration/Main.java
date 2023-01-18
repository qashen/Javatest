package siebel.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import siebel.integration.rule.Criteria;
import siebel.integration.rule.CriteriaGroup;
import siebel.integration.rule.Pattern;
import siebel.integration.rule.PatternRule;
import siebel.integration.util.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

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
                System.out.println(propertyName + " is " + propertyValue);
            }

            OnMatch one = new OnMatch();
            one.onMatch(mapProperties, "Id", "dummyId");


            PatternRule pr = new PatternRule();
            classLoader = pr.getClass().getClassLoader();
            is = classLoader.getResourceAsStream("pattern.json");
            pr = mapper.readValue(is, PatternRule.class);
            String str = pr.getPatterns()[0].getId();
            System.out.println(str);

            HashMap<String, Object> ruleMaps = new HashMap<>();
            SetRuleNodes(pr, ruleMaps);
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
