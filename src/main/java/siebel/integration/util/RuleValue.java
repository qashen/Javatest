package siebel.integration.util;

public class RuleValue {

    //"valueType": "NUMBER”
    // "criteriaValue": "1",
    //"criteriaPara": "Quantity",
    //"criteriaOperator": "EQUALS"

    private String valueType;
    private String criteriaValue;
    private String criteriaPara;
    private String criteriaOperator;

    public RuleValue() {
    }

    public RuleValue(String valueType, String criteriaValue, String criteriaPara, String criteriaOperator) {
        this.valueType = valueType;
        this.criteriaValue = criteriaValue;
        this.criteriaPara = criteriaPara;
        this.criteriaOperator = criteriaOperator;
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
