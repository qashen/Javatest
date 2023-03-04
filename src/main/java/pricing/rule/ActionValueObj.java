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
public class ActionValueObj {

    private String id;
    private String name;
    private String actionObjectType;  //All, One Time, Usage, Recurring
    private String appliesTo;
    private Number maxQuantity;
    private String atreferredType;
    private int versionState;
    @JsonProperty("_atreferredType")
    public void setAtreferredType(String atreferredType) {
        this.atreferredType = atreferredType;
    }
}
