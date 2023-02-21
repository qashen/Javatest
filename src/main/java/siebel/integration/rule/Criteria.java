package siebel.integration.rule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Criteria {
    private String id;
    private String valueType;
    private String criteriaValue;
    private String criteriaPara;
    private String criteriaOperator;

}
