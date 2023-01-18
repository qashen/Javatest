package siebel.integration.rule;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
public class ActionValueObj {

        /*
							"id": "pricealt-1145484163-1671165638",
							"name": "pricealt-1145484163-1671165638",
							"actionObjectType": "PRODUCT_OFFERING_PRICE",
							"appliesTo": "ONE_TIME",
							"maxQuantity": 1,
							"_atreferredType": "ProductOfferPriceAlterationOracle"
     */
    private String id;
    private String name;
    private String actionObjectType;
    private String appliesTo;
    private Number maxQuantity;
    private String atreferredType;

    public ActionValueObj() {
    }

    public ActionValueObj(String id, String name, String actionObjectType, String appliesTo, Number maxQuantity, String atreferredType) {
        this.id = id;
        this.name = name;
        this.actionObjectType = actionObjectType;
        this.appliesTo = appliesTo;
        this.maxQuantity = maxQuantity;
        this.atreferredType = atreferredType;
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

    public String getAppliesTo() {
        return appliesTo;
    }

    public void setAppliesTo(String appliesTo) {
        this.appliesTo = appliesTo;
    }

    public Number getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(Number maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public String getAtreferredType() {
        return atreferredType;
    }
    @JsonProperty("_atreferredType")
    public void setAtreferredType(String atreferredType) {
        this.atreferredType = atreferredType;
    }
}
