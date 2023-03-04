package pricing.rulesImpl;

import lombok.extern.slf4j.Slf4j;
import pricing.LineItemIC;
import pricing.LineItemInternal;
import pricing.ruleEngine.InferenceEngine;
import pricing.ruleEngine.RuleNamespace;

@Deprecated
@Slf4j
public class PricingInferenceEngineObsolete extends InferenceEngine<LineItemIC, LineItemInternal> {
    @Override
    protected RuleNamespace getRuleNamespace() {
        return RuleNamespace.PRICING;
    }

    @Override
    protected LineItemInternal initializeOutputResult() {
        return LineItemInternal.builder().build();
    }
}
