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
public class Pattern {
    Integer priority;
    String logicalRelationship;
    List<Rule> ruleListSet;
}
