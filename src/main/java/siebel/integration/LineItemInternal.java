package siebel.integration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LineItemInternal {
    public String dynamicMatrixId;
    public String productId;
    public String priceListId;
    public String description;
    public String pricingMethod;
    public Date startDate;
    public Number volumeDiscount;
    public Number originalListPrice;
    public String externalIntegrationId;
    public String id;
    public Integer quantity;
    public String country;
    public String city;
    public String productOffering;
    public List<String> list;
}

