package siebel.integration.util;

public class RuleNode {
    private int sequence;
    private RuleValue ruleValue;
    private String ruleType;
    private String patternId;

    public RuleNode() {
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public RuleValue getRuleValue() {
        return ruleValue;
    }

    public void setRuleValue(RuleValue ruleValue) {
        this.ruleValue = ruleValue;
    }

    public String getRuleType() {
        return ruleType;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    public String getPatternId() {
        return patternId;
    }

    public void setPatternId(String patternId) {
        this.patternId = patternId;
    }
}
