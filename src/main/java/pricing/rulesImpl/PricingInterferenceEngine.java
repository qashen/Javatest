package pricing.rulesImpl;

import pricing.ruleEngine.InferenceEngine;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static pricing.util.constants.ADJUSTMENT;

public class PricingInterferenceEngine extends InferenceEngine<Object, HashMap> {

    @Override
    protected HashMap initializeOutputResult() {
        return new HashMap();
    }

    @Override
    protected Object add(Object inputData, Object child) {

        if (inputData instanceof LinkedHashMap) {
            ((LinkedHashMap<String, Object>) inputData).put(ADJUSTMENT, child);
        }
        else if (inputData instanceof Collection) {
            ((Collection<Object>) inputData).add(child);
        }
        else if (inputData instanceof Map) {
            ((Map<String, Object>) inputData).put (ADJUSTMENT, child);
        }
        else if (inputData instanceof HashMap) {
            ((HashMap<String, Object>) inputData).put (ADJUSTMENT, child);
        }
        return inputData;
    }
}