package siebel.integration.rule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActionType {
    /*
    ALLOWANCE,
    AWARD,
    DISCOUNT,
    FIXED_DISCOUNT,
    FIXED_MARKUP,
    GIFT_ALLOWANCE,
    REDUCTION,
    PERCENTAGE_DISCOUNT,
    PERCENTAGE_MARKUP,
    PRICE_OVERRIDE*/
    String actionTpe;
}
