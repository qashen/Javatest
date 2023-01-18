package siebel.integration.rule;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
public class ActionType {
    private String[] actionType;

    public ActionType() {
    }

    public ActionType(String[] actionType) {
        this.actionType = actionType;
    }

    public String[] getActionType() {
        return actionType;
    }
    @JsonProperty("actionType")
    public void setActionType(String[] actionType) {
        this.actionType = actionType;
    }
}
