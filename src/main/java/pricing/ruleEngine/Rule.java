package pricing.ruleEngine;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Rule {
    public Rule(boolean bSkipDSLHandling) {
        this.bSkipDSLHandling = bSkipDSLHandling;
    }

    RuleNamespace ruleNamespace;
    String ruleId;
    String condition;
    String action;
    Integer priority;
    String description;
    boolean bSkipDSLHandling;
    List<String> actionSet;
}
