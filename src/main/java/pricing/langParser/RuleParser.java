package pricing.langParser;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
public class RuleParser<INPUT_DATA, OUTPUT_RESULT> {


    protected DSLParser dslParser;

    protected MVELParser mvelParser;

    private final String INPUT_KEYWORD = "input";
    private final String OUTPUT_KEYWORD = "output";

    private final String MAP_KEYWORD = "map";
    public RuleParser() {
        this.dslParser = new DSLParser();
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
    public boolean parseCondition(String expression, INPUT_DATA inputData, boolean bSkipDSLHandling) {
        //String resolvedDslExpression;
        if (!bSkipDSLHandling) {
            expression = dslParser.resolveDomainSpecificKeywords(expression);
        }
        //else {
        //    resolvedDslExpression = expression;
        //}
        Map<String, Object> input = new HashMap<>();
        input.put(INPUT_KEYWORD, inputData);
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
    public OUTPUT_RESULT parseAction(String expression, INPUT_DATA inputData, OUTPUT_RESULT outputResult, boolean bSkipDSLHandling) {
        if (!bSkipDSLHandling){
            expression = dslParser.resolveDomainSpecificKeywords(expression);
        }
        Map<String, Object> input = new HashMap<>();
        input.put(INPUT_KEYWORD, inputData);
        input.put(OUTPUT_KEYWORD, outputResult);
        Map<String, String> maps = new HashMap<>();
        input.put(MAP_KEYWORD, maps);
        mvelParser.parseMvelExpression(expression, input);
        for (Map.Entry m : maps.entrySet())
        {
            if (((LinkedHashMap) outputResult).get (m.getKey()) != null)
            {
                ((LinkedHashMap) outputResult).put (m.getKey(),m.getValue());
            }
        }
        return outputResult;
    }

    public OUTPUT_RESULT parseAction(String expression, INPUT_DATA inputData, OUTPUT_RESULT outputResult) {
        Map<String, Object> input = new HashMap<>();
        input.put(INPUT_KEYWORD, inputData);
        input.put(MAP_KEYWORD, outputResult);
        mvelParser.parseMvelExpression(expression, input);
        return outputResult;
    }

}
