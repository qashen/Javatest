package siebel.integration.ruleEngine;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@NoArgsConstructor
public class RuleEngine {

    public Object run(InferenceEngine inferenceEngine, Object inputData) {
        String ruleNamespace = inferenceEngine.getRuleNamespace().toString();
        ObjectMapper mapper = new ObjectMapper();
        KnowledgeBaseService knowledgeBaseService = new KnowledgeBaseService();

        try {
            ClassLoader classLoader = knowledgeBaseService.getClass().getClassLoader();
            InputStream is = classLoader.getResourceAsStream("knowledgebase_1.json");
            knowledgeBaseService = mapper.readValue(is, KnowledgeBaseService.class);

        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Rule> allRulesByNamespace = knowledgeBaseService.getAllRuleByNamespace(ruleNamespace);
        Object result = inferenceEngine.run(allRulesByNamespace, inputData);
        return result;
    }

    /*
    public Map<String, Object> run (InferenceEngine inferenceEngine, Map<String, Object> inputData) {
        Map<String, Object> resultMap = new HashMap<>();
        inputData.forEach((key, value) -> {
            resultMap.put (key, run(inferenceEngine, value));
        });
        return resultMap;
    }*/

    public List<Object> run(InferenceEngine inferenceEngine, List<Object> inputData) {
        List<Object> resultList = new ArrayList<>();
        List<Object> inputChild = (List<Object>) inputData.get(0);
        for (Object o : inputChild) {
            Object result = run(inferenceEngine, o);
            if (result != null) {
                resultList.add(result);
            }
        }
        return resultList;
    }

    public List<Object> runCriteria(InferenceEngine inferenceEngine, List<Object> inputData, List<Object> inputCriteria) {
        List<Object> resultList = new ArrayList<>();
        List<Object> inputChild = (List<Object>) inputData.get(0);
        for (Object o : inputChild) {
            Object result = runCriteria(inferenceEngine, o, inputCriteria);
            if (result != null) {
                resultList.add(result);
            }
        }
        return resultList;
    }

    public Object runCriteria(InferenceEngine inferenceEngine, Object inputData, List<Object> inputCriteria) {
        List<Rule> allRulesByNamespace = (List<Rule>) inputCriteria.get(0);
        Object result = inferenceEngine.run(allRulesByNamespace, inputData);
        return result;
    }
}
