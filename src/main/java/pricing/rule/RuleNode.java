package pricing.rule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Deprecated
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RuleNode {
    private int sequence;
    private RuleValue ruleValue;
    private String ruleType;
    private String criteriaGroupId;
    private String criteriaId;
    private String patternId;
    private String expression;
}
