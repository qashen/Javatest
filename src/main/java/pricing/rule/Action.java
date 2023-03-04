package pricing.rule;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Deprecated
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Action {
    private ActionValueObj[] actionValueObjs;
    private ActionType[] actionType;
    private Eligibility[] eligibility;
    private String id;
    private Number actionValue;

    @JsonProperty("actionValueObj")
    public void setActionValueObjs(ActionValueObj[] actionValueObjs) {
        this.actionValueObjs = actionValueObjs;
    }

    @JsonProperty("actionType")
    public void setActionType(ActionType[] actionType) {
        this.actionType = actionType;
    }

    @JsonProperty("eligibility")
    public void setEligibility(Eligibility[] eligibility) {
        this.eligibility = eligibility;
    }

}
