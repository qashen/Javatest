package siebel.integration.rule;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
public class Criteria {
    /*
    						"id": "criteria-1277039798-1671165605",
							"valueType": "NUMBER",
							"criteriaValue": "1",
							"criteriaPara": "Quantity",
							"criteriaOperator": "EQUALS"
     */
    private String id;
    private String valueType;
    private String criteriaValue;
    private String criteriaPara;
    private String criteriaOperator;

    public Criteria() {
    }

    public Criteria(String id, String valueType, String criteriaValue, String criteriaPara, String criteriaOperator) {
        this.id = id;
        this.valueType = valueType;
        this.criteriaValue = criteriaValue;
        this.criteriaPara = criteriaPara;
        this.criteriaOperator = criteriaOperator;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public String getCriteriaValue() {
        return criteriaValue;
    }

    public void setCriteriaValue(String criteriaValue) {
        this.criteriaValue = criteriaValue;
    }

    public String getCriteriaPara() {
        return criteriaPara;
    }

    public void setCriteriaPara(String criteriaPara) {
        this.criteriaPara = criteriaPara;
    }

    public String getCriteriaOperator() {
        return criteriaOperator;
    }

    public void setCriteriaOperator(String criteriaOperator) {
        this.criteriaOperator = criteriaOperator;
    }
}
