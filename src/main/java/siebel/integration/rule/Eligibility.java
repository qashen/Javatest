package siebel.integration.rule;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Eligibility {
    private String id;
    private String name;
    private String actionObjectType;
    private String atreferredType;
    private String attype;

    private int versionState;
    @JsonProperty("_atreferredType")
    public void setAtreferredType(String atreferredType) {
        this.atreferredType = atreferredType;
    }

    @JsonProperty("_attype")
    public void setAttype(String attype) {
        this.attype = attype;
    }
}
