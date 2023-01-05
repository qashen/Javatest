package siebel.integration;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

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

    public LineItemIC() {
    }

    public LineItemIC(String dynamicMatrixId, String productId, String priceListId, String description, String pricingMethod, Date startDate, Number volumeDiscount, Number originalListPrice, String externalIntegrationId, String id) {
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
    }

    public String getDynamicMatrixId() {
        return DynamicMatrixId;
    }

    @JsonProperty("Dynamic Matrix Id")
    public void setDynamicMatrixId(String dynamicMatrixId) {
        DynamicMatrixId = dynamicMatrixId;
    }

    public String getProductId() {
        return ProductId;
    }
    @JsonProperty("Product Id")
    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getPriceListId() {
        return PriceListId;
    }
    @JsonProperty("Price List Id")
    public void setPriceListId(String priceListId) {
        PriceListId = priceListId;
    }

    public String getDescription() {
        return Description;
    }
    @JsonProperty("Description")
    public void setDescription(String description) {
        Description = description;
    }

    public String getPricingMethod() {
        return PricingMethod;
    }
    @JsonProperty("Pricing Method")
    public void setPricingMethod(String pricingMethod) {
        PricingMethod = pricingMethod;
    }

    public Date getStartDate() {
        return startDate;
    }
    @JsonProperty("Start Date")
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Number getVolumeDiscount() {
        return VolumeDiscount;
    }
    @JsonProperty("Volume Discount")
    public void setVolumeDiscount(Number volumeDiscount) {
        VolumeDiscount = volumeDiscount;
    }

    public Number getOriginalListPrice() {
        return OriginalListPrice;
    }
    @JsonProperty("Original List Price")
    public void setOriginalListPrice(Number originalListPrice) {
        OriginalListPrice = originalListPrice;
    }

    public String getExternalIntegrationId() {
        return ExternalIntegrationId;
    }
    @JsonProperty("External Integration Id")
    public void setExternalIntegrationId(String externalIntegrationId) {
        ExternalIntegrationId = externalIntegrationId;
    }

    public String getId() {
        return Id;
    }
    @JsonProperty("Id")
    public void setId(String id) {
        Id = id;
    }
}
