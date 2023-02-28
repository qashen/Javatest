package siebel.integration.rulesImpl;

import siebel.integration.ruleEngine.InferenceEngine;
import siebel.integration.ruleEngine.RuleNamespace;

import java.util.HashMap;

public class PricingInterference extends InferenceEngine<Object, HashMap> {
    @Override
    protected RuleNamespace getRuleNamespace() {
        return RuleNamespace.PRICING;
    }

    @Override
    protected HashMap initializeOutputResult() {
        return new HashMap();
    }
}