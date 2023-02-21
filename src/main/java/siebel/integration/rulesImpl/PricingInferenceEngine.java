package siebel.integration.rulesImpl;

import lombok.extern.slf4j.Slf4j;
import siebel.integration.LineItemIC;
import siebel.integration.LineItemInternal;
import siebel.integration.ruleEngine.InferenceEngine;
import siebel.integration.ruleEngine.RuleNamespace;

@Slf4j
public class PricingInferenceEngine extends InferenceEngine<LineItemIC, LineItemInternal> {
    @Override
    protected RuleNamespace getRuleNamespace() {
        return RuleNamespace.PRICING;
    }

    @Override
    protected LineItemInternal initializeOutputResult() {
        return LineItemInternal.builder().build();
    }
}
