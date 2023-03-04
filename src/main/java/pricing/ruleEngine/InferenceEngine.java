package pricing.ruleEngine;
import pricing.langParser.RuleParser;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class InferenceEngine<INPUT_DATA, OUTPUT_RESULT> {
    private final RuleParser<INPUT_DATA, OUTPUT_RESULT> ruleParser;

    public InferenceEngine() {
        this.ruleParser = new RuleParser();
    }

    public OUTPUT_RESULT run (List<Rule> listOfRules, INPUT_DATA inputData){
        if (null == listOfRules || listOfRules.isEmpty()){
            return null;
        }

        //STEP 1 (MATCH) : Match the facts and data against the set of rules.
        List<Rule> conflictSet = match(listOfRules, inputData);

        //STEP 2 (RESOLVE) : Resolve the conflict and give the selected one rule.
        Rule resolvedRule = resolve(conflictSet);
        if (null == resolvedRule){
            return null;
        }

        //STEP 3 (EXECUTE) : Run the action of the selected rule on given data and return the output.

        return executeRule(resolvedRule, inputData);
    }

    protected List<Rule> match(List<Rule> listOfRules, INPUT_DATA inputData){
        return listOfRules.stream()
                .filter(
                        rule -> {
                            String condition = rule.getCondition();
                            return ruleParser.parseCondition(condition, inputData, rule.bSkipDSLHandling);
                        }
                )
                .collect(Collectors.toList());
    }


    protected Rule resolve(List<Rule> conflictSet){
        Optional<Rule> rule = conflictSet.stream()
                .findFirst();
        return rule.orElse(null);
    }


    protected OUTPUT_RESULT executeRule(Rule rule, INPUT_DATA inputData){
        if (!rule.bSkipDSLHandling) {
            OUTPUT_RESULT outputResult = initializeOutputResult();
            return ruleParser.parseAction(rule.getAction(), inputData, outputResult, false);
        }
        else
        {
            List<OUTPUT_RESULT> outputParent = new ArrayList<>();
            for (String expression: rule.getActionSet()) {
                OUTPUT_RESULT outputResult = initializeOutputResult();
                outputResult = ruleParser.parseAction(expression, inputData, outputResult);
                outputParent.add(outputResult);
            }
            return (OUTPUT_RESULT) outputParent;

        }

    }

    protected abstract OUTPUT_RESULT initializeOutputResult();
    protected abstract RuleNamespace getRuleNamespace();
}
