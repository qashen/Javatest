package siebel.integration.rule;
import com.fasterxml.jackson.annotation.JsonProperty;
public class CriteriaGroup {
/*
					"id": "CG-1619882051-1671165579339",
					"groupName": "JS_Promotion1_CriteriaGroup1",
					"relationTypeInGroup": "AND",
 */
    private Criteria[] criteria;
    private String id;
    private String groupName;
    private String relationTypeInGroup;

    public CriteriaGroup() {
    }

    public CriteriaGroup(Criteria[] criteria, String id, String groupName, String relationTypeInGroup) {
        this.criteria = criteria;
        this.id = id;
        this.groupName = groupName;
        this.relationTypeInGroup = relationTypeInGroup;
    }

    public Criteria[] getCriteria() {
        return criteria;
    }
    @JsonProperty("criteria")
    public void setCriteria(Criteria[] criteria) {
        this.criteria = criteria;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getRelationTypeInGroup() {
        return relationTypeInGroup;
    }

    public void setRelationTypeInGroup(String relationTypeInGroup) {
        this.relationTypeInGroup = relationTypeInGroup;
    }
}
