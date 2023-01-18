package siebel.integration;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LineItemIC {

    /*
                    "Dynamic Matrix Id": "",
                    "Product Id": "88-1W3BZP",
                    "Price List Id": "88-1X7ZRS",
                    "Description": "",
                    "Pricing Method": "List Price",
                    "Start Date": "02/15/2022 06:00:00",
                    "Volume Discount": "",
                    "Original List Price": 300,
                    "External Integration Id": "POP_10115",
                    "Id": "dummyId"
     */
    private String DynamicMatrixId;
    private String ProductId;
    private String PriceListId;

    private String Description;

    private String PricingMethod;
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy hh:mm:ss")
    private Date startDate;
    private Number VolumeDiscount;
    private Number OriginalListPrice;
    private String ExternalIntegrationId;
    private String Id;
    private final Map<String, String> properties;

    private LineItemIC[] lineItemICS;
    public LineItemIC() {
        properties = new HashMap<>();
    }

    public LineItemIC(String dynamicMatrixId, String productId, String priceListId, String description, String pricingMethod, Date startDate, Number volumeDiscount, Number originalListPrice, String externalIntegrationId, String id, LineItemIC[] lineItemICS) {
        DynamicMatrixId = dynamicMatrixId;
        ProductId = productId;
        PriceListId = priceListId;
        Description = description;
        PricingMethod = pricingMethod;
        this.startDate = startDate;
        VolumeDiscount = volumeDiscount;
        OriginalListPrice = originalListPrice;
        ExternalIntegrationId = externalIntegrationId;
        Id = id;
        this.lineItemICS = lineItemICS;
        properties = new HashMap<>();
    }

    public String getDynamicMatrixId() {
        return DynamicMatrixId;
    }

    @JsonProperty("Dynamic Matrix Id")
    public void setDynamicMatrixId(String dynamicMatrixId) {
        this.DynamicMatrixId = dynamicMatrixId;
        add ("Dynamic Matrix Id", dynamicMatrixId);
    }

    public String getProductId() {
        return ProductId;
    }
    @JsonProperty("Product Id")
    public void setProductId(String productId) {
        this.ProductId = productId;
        add ("Product Id", productId);
    }

    public String getPriceListId() {
        return PriceListId;
    }
    @JsonProperty("Price List Id")
    public void setPriceListId(String priceListId) {
        this.PriceListId = priceListId;
        add ("Price List Id", priceListId);
    }

    public String getDescription() {
        return Description;
    }
    @JsonProperty("Description")
    public void setDescription(String description) {
        this.Description = description;
        add ("Description", description);
    }

    public String getPricingMethod() {
        return PricingMethod;
    }
    @JsonProperty("Pricing Method")
    public void setPricingMethod(String pricingMethod) {
        this.PricingMethod = pricingMethod;
        add ("Pricing Method", pricingMethod);
    }

    public Date getStartDate() {
        return startDate;
    }
    @JsonProperty("Start Date")
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
        if (startDate != null) {
            add("Start Date", startDate.toString());
        }
        else
            add("Start Date", "");
    }

    public Number getVolumeDiscount() {
        return VolumeDiscount;
    }
    @JsonProperty("Volume Discount")
    public void setVolumeDiscount(Number volumeDiscount) {
        this.VolumeDiscount = volumeDiscount;
        if (volumeDiscount != null)
        {
            add("Volume Discount", volumeDiscount.toString());
        }
        else
            add("Volume Discount", "");
    }

    public Number getOriginalListPrice() {
        return OriginalListPrice;
    }
    @JsonProperty("Original List Price")
    public void setOriginalListPrice(Number originalListPrice) {
        this.OriginalListPrice = originalListPrice;
        if (originalListPrice != null) {
            add("Original List Price", originalListPrice.toString());
        }
        else
            add("Original List Price", "");
    }

    public String getExternalIntegrationId() {
        return ExternalIntegrationId;
    }
    @JsonProperty("External Integration Id")
    public void setExternalIntegrationId(String externalIntegrationId) {
        ExternalIntegrationId = externalIntegrationId;
        add ("External Integration Id", externalIntegrationId);
    }

    public String getId() {
        return Id;
    }
    @JsonProperty("Id")
    public void setId(String id) {
        Id = id;
        add ("Id", id);
    }

    public LineItemIC[] getLineItemICS() {
        return lineItemICS;
    }

    @JsonProperty("Price List Item - Import")
    public void setLineItemICS(LineItemIC[] lineItemICS) {
        this.lineItemICS = lineItemICS;
    }

    @JsonAnyGetter
    public Map<String, String> getProperties(){
        return properties;
    }
    public void add(String property, String value){
        properties.put(property, value);
    }

    public String  getProperty (String name){
        return properties.get (name);
    }
}
