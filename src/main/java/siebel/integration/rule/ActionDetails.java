package siebel.integration.rule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActionDetails {
    Number adjustmentValue;
    String priceFieldName;
    String priceType;
    String adjustmentType;
    String expression;
}
