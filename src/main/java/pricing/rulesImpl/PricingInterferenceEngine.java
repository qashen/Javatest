package pricing.rulesImpl;

import pricing.ruleEngine.InferenceEngine;
import pricing.ruleEngine.RuleNamespace;

import java.util.HashMap;

public class PricingInterferenceEngine extends InferenceEngine<Object, HashMap> {
    @Override
    protected RuleNamespace getRuleNamespace() {
        return RuleNamespace.PRICING;
    }

    @Override
    protected HashMap initializeOutputResult() {
        return new HashMap();
    }
}