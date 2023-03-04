package pricing.rule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Deprecated
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
