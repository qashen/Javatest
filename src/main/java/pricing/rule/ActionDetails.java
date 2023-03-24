package pricing.rule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActionDetails {
    BigDecimal adjustmentValue;
    String priceFieldName;
    String priceType;
    String adjustmentType;
    String expression;
}
