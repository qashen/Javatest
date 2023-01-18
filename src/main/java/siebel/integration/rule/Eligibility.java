package siebel.integration.rule;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
public class Eligibility {

    /*
    						"id": "PL_1002",
							"name": "Fiber Modems",
							"actionObjectType": "PRODUCTLINE",
							"_atreferredType": "ProductLineOracle",
							"_attype": "ActionObjectRefOracle"
     */

    private String id;
    private String name;
    private String actionObjectType;
    private String atreferredType;
    private String attype;

    public Eligibility() {
    }

    public Eligibility(String id, String name, String actionObjectType, String atreferredType, String attype) {
        this.id = id;
        this.name = name;
        this.actionObjectType = actionObjectType;
        this.atreferredType = atreferredType;
        this.attype = attype;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getActionObjectType() {
        return actionObjectType;
    }

    public void setActionObjectType(String actionObjectType) {
        this.actionObjectType = actionObjectType;
    }

    public String getAtreferredType() {
        return atreferredType;
    }
    @JsonProperty("_atreferredType")
    public void setAtreferredType(String atreferredType) {
        this.atreferredType = atreferredType;
    }

    public String getAttype() {
        return attype;
    }
    @JsonProperty("_attype")
    public void setAttype(String attype) {
        this.attype = attype;
    }
}
