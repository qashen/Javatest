package siebel.integration.ruleEngine;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
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
            InputStream is = classLoader.getResourceAsStream("knowledgebase.json");
            knowledgeBaseService = mapper.readValue(is, KnowledgeBaseService.class);

        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Rule> allRulesByNamespace = knowledgeBaseService.getAllRuleByNamespace(ruleNamespace);
        Object result = inferenceEngine.run(allRulesByNamespace, inputData);
        return result;
    }
}
