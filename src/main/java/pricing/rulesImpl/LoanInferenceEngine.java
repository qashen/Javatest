package pricing.rulesImpl;

import lombok.extern.slf4j.Slf4j;
import pricing.ruleEngine.InferenceEngine;
import pricing.ruleEngine.RuleNamespace;

@Deprecated
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

