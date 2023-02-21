package siebel.integration;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Data
@Builder
@AllArgsConstructor
public class LineItemIC {
    public String dynamicMatrixId;
    public String productId;
    public String priceListId;

    public String description;

    public String pricingMethod;
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy hh:mm:ss")
    public Date startDate;
    public Number volumeDiscount;
    public Number originalListPrice;
    public String externalIntegrationId;
    public String id;

    public Integer quantity;

    public String country;

    public String city;

    public String productOffering;
    public HashMap<String, String> mapJsonPropertyField;
    public LineItemIC[] lineItemICS;

    public LineItemIC() {
        mapJsonPropertyField = new HashMap<>();
    }


    @JsonProperty("Dynamic Matrix Id")
    public void setDynamicMatrixId(String dynamicMatrixId) {
        this.dynamicMatrixId = dynamicMatrixId;
        add ("Dynamic Matrix Id", "dynamicMatrixId");
    }

    @JsonProperty("Product Id")
    public void setProductId(String productId) {
        this.productId = productId;
        add ("Product Id", "productId");
    }

    @JsonProperty("Price List Id")
    public void setPriceListId(String priceListId) {
        this.priceListId = priceListId;
        add ("Price List Id", "priceListId");
    }

    @JsonProperty("Description")
    public void setDescription(String description) {
        this.description = description;
        add ("Description", "description");
    }


    @JsonProperty("Pricing Method")
    public void setPricingMethod(String pricingMethod) {
        this.pricingMethod = pricingMethod;
        add ("Pricing Method", "pricingMethod");
    }


    @JsonProperty("Start Date")
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
        add("Start Date", "startDate");
    }

    @JsonProperty("Volume Discount")
    public void setVolumeDiscount(Number volumeDiscount) {
        this.volumeDiscount = volumeDiscount;
        add("Volume Discount", "volumeDiscount");
    }

    @JsonProperty("Original List Price")
    public void setOriginalListPrice(Number originalListPrice) {
        this.originalListPrice = originalListPrice;
            add("Original List Price", "originalListPrice");
    }

    @JsonProperty("External Integration Id")
    public void setExternalIntegrationId(String externalIntegrationId) {
        this.externalIntegrationId = externalIntegrationId;
        add ("External Integration Id", externalIntegrationId);
    }

    @JsonProperty("Id")
    public void setId(String id) {
        this.id = id;
        add ("Id", id);
    }
    @JsonProperty("Quantity")
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        add ("Quantity", "quantity");
    }
    @JsonProperty("Country")
    public void setCountry(String country) {
        this.country = country;
        add ("Country", "country");
    }
    @JsonProperty("City")
    public void setCity(String city) {
        this.city = city;
        add ("City", "city");
    }
    @JsonProperty("Product Offering")
    public void setProductOffering(String productOffering) {
        this.productOffering = productOffering;
        add ("Product Offering", "productOffering");
    }

    @JsonProperty("Price List Item - Import")
    public void setLineItemICS(LineItemIC[] lineItemICS) {
        this.lineItemICS = lineItemICS;
    }

    @JsonAnyGetter
    public Map<String, String> getMapJsonPropertyField(){
        return mapJsonPropertyField;
    }

    public void add(String property, String value){
        mapJsonPropertyField.put(property, value);
    }

    public String  getJsonField (String jsonName){
        return mapJsonPropertyField.get (jsonName);
    }
}
