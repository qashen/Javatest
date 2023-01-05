package siebel.integration;

import com.fasterxml.jackson.annotation.JsonProperty;
import siebel.integration.LineItemIC;

public class ListOfIC {
    private LineItemIC[] lineItemICS;

    public ListOfIC() {
    }

    public ListOfIC(LineItemIC[] lineItemICS) {
        this.lineItemICS = lineItemICS;
    }

    public LineItemIC[] getLineItemICS() {
        return lineItemICS;
    }
    @JsonProperty("Price List Item - Import")
    public void setLineItemICS(LineItemIC[] lineItemICS) {
        this.lineItemICS = lineItemICS;
    }
}
