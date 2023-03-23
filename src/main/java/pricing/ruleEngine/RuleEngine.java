package pricing.ruleEngine;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
@SuppressWarnings({ "unchecked", "rawtypes" })
//@Slf4j
@NoArgsConstructor
public class RuleEngine {

    public List<Object> run(InferenceEngine inferenceEngine, List<Object> inputData, List<Object> inputCriteria) {
        List<Object> resultList = new ArrayList<>();
        List<Object> inputChild = (List<Object>) inputData.get(0);
        for (Object o : inputChild) {
            Object result = run(inferenceEngine, o, inputCriteria);
            if (result != null) {
                resultList.add(result);
            }
        }
        return resultList;
    }

    public Object run(InferenceEngine inferenceEngine, Object inputData, List<Object> inputCriteria) {
        List<Rule> rules = (List<Rule>) inputCriteria.get(0);
        return inferenceEngine.run(rules, inputData);
    }
}
