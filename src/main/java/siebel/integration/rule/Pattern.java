package siebel.integration.rule;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
public class Pattern {
    /*
    	"id": "pattern-773938262-167116563465",
	    "name": "JS_Promotion1_Pattern1",
		"description": "JS_Promotion1_Pattern1",
		"priority": 1,
		"relationTypeAmongGroup": "AND",
     */
    private Action[] actions;
    private CriteriaGroup[] criteriaGroups;
    private String id;
    private String name;
    private String description;
    private Number priority;
    private String relationTypeAmongGroup;

    public Pattern() {
    }

    public Pattern(Action[] actions, CriteriaGroup[] criteriaGroups, String id, String name, String description, Number priority, String relationTypeAmongGroup) {
        this.actions = actions;
        this.criteriaGroups = criteriaGroups;
        this.id = id;
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.relationTypeAmongGroup = relationTypeAmongGroup;
    }

    public Action[] getActions() {
        return actions;
    }
    @JsonProperty("action")
    public void setActions(Action[] actions) {
        this.actions = actions;
    }

    public CriteriaGroup[] getCriteriaGroups() {
        return criteriaGroups;
    }
    @JsonProperty("criteriaGroup")
    public void setCriteriaGroups(CriteriaGroup[] criteriaGroups) {
        this.criteriaGroups = criteriaGroups;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Number getPriority() {
        return priority;
    }

    public void setPriority(Number priority) {
        this.priority = priority;
    }

    public String getRelationTypeAmongGroup() {
        return relationTypeAmongGroup;
    }

    public void setRelationTypeAmongGroup(String relationTypeAmongGroup) {
        this.relationTypeAmongGroup = relationTypeAmongGroup;
    }
}
