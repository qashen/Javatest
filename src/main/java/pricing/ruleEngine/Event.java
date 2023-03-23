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
public class Event {
    Integer priority;
    String logicalRelationship;
    List<Rule> ruleListSet;
}
