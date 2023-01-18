package siebel.integration.rule;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
public class Action {
    /*
    	"id": "action-3146164870-167116563311",
	    "actionValue": 0.05,
     */
    private ActionValueObj[] actionValueObjs;
    private String[] actionType;
    private Eligibility[] eligibilities;
    private String id;
    private Number actionValue;

    public Action() {
    }

    public Action(ActionValueObj[] actionValueObjs, String[] actionType, Eligibility[] eligibilities, String id, Number actionValue) {
        this.actionValueObjs = actionValueObjs;
        this.actionType = actionType;
        this.eligibilities = eligibilities;
        this.id = id;
        this.actionValue = actionValue;
    }

    public ActionValueObj[] getActionValueObjs() {
        return actionValueObjs;
    }
    @JsonProperty("actionValueObj")
    public void setActionValueObjs(ActionValueObj[] actionValueObjs) {
        this.actionValueObjs = actionValueObjs;
    }

    public String[] getActionType() {
        return actionType;
    }
    @JsonProperty("actionType")
    public void setActionType(String[] actionType) {
        this.actionType = actionType;
    }

    public Eligibility[] getEligibilities() {
        return eligibilities;
    }
    @JsonProperty("eligibility")
    public void setEligibilities(Eligibility[] eligibilities) {
        this.eligibilities = eligibilities;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Number getActionValue() {
        return actionValue;
    }

    public void setActionValue(Number actionValue) {
        this.actionValue = actionValue;
    }
}
