package pricing.langParser;

import java.util.HashMap;
import java.util.Map;
import static pricing.util.constants.*;

public class RuleParser<INPUT_DATA, OUTPUT_RESULT> {

    protected MVELParser mvelParser;

    private final String MAP_KEYWORD = "map";
    public RuleParser() {
        this.mvelParser = new MVELParser();
    }

    /**
     * Parsing in given priority/steps.
     *
     * Step 1. Resolve domain specific keywords first: $(rulenamespace.keyword)
     * Step 2. Resolve MVEL expression.
     *
     * @param expression
     * @param inputData
     */
    public boolean parseCondition(String expression, INPUT_DATA inputData) {
        Map<String, Object> input = new HashMap<>();
        input.put(INPUT, inputData);
        boolean match = mvelParser.parseMvelExpression(expression, input);
        return match;
    }

    /**
     * Parsing in given priority/steps.
     *
     * Step 1. Resolve domain specific keywords: $(rulenamespace.keyword)
     * Step 2. Resolve MVEL expression.
     *
     * @param expression
     * @param inputData
     * @param outputResult
     * @return
     */
    public OUTPUT_RESULT parseAction(String expression, INPUT_DATA inputData, OUTPUT_RESULT outputResult) {
        Map<String, Object> input = new HashMap<>();
        input.put(INPUT, inputData);
        input.put(MAP_KEYWORD, outputResult);
        mvelParser.parseMvelExpression(expression, input);
        return outputResult;
    }

}
