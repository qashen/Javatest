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
public class Pattern {
    private Action[] actions;
    private CriteriaGroup[] criteriaGroups;
    private String id;
    private String name;
    private String description;
    private Number priority;
    private String relationTypeAmongGroup;

    @JsonProperty("action")
    public void setActions(Action[] actions) {
        this.actions = actions;
    }

    @JsonProperty("criteriaGroup")
    public void setCriteriaGroups(CriteriaGroup[] criteriaGroups) {
        this.criteriaGroups = criteriaGroups;
    }

}
