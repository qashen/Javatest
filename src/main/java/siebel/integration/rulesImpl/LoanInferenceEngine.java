package siebel.integration.rulesImpl;

import lombok.extern.slf4j.Slf4j;
import siebel.integration.langParser.RuleParser;
import siebel.integration.ruleEngine.InferenceEngine;
import siebel.integration.ruleEngine.RuleNamespace;

@Slf4j
public class LoanInferenceEngine extends InferenceEngine<UserDetails, LoanDetails> {

    @Override
    protected RuleNamespace getRuleNamespace() {
        return RuleNamespace.PRICING;
    }

    @Override
    protected LoanDetails initializeOutputResult() {
        return LoanDetails.builder().build();
    }
}

