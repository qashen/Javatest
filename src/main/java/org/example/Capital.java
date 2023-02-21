package org.example;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
public class Capital {
    private String Color;
    private String Make;

    public Capital() {
        mapJsonPropertyField = new HashMap<>();
    }

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy hh:mm:ss")
    private Date datePurchased;

    private Number pricePurchased;

    private HashMap<String, String> mapJsonPropertyField;

    @JsonAnyGetter
    public HashMap<String, String> getMapJsonPropertyField() {
        return mapJsonPropertyField;
    }

    public void setMapJsonPropertyField(HashMap<String, String> mapJsonPropertyField) {
        this.mapJsonPropertyField = mapJsonPropertyField;
    }

    @JsonProperty("Color")
    public void setColor(String Color) {
        this.Color = Color;
        this.mapJsonPropertyField.put("Color", "Color");
    }

    @JsonProperty("Make")
    public void setMake(String make) {
        this.Make = make;
        this.mapJsonPropertyField.put("Make", "Make");
    }

    @JsonProperty("Date Purchased")
    public void setDatePurchased(Date datePurchased) {
        this.datePurchased = datePurchased;
        this.mapJsonPropertyField.put("Date Purchased", "datePurchased");
    }

    @JsonProperty("Price Purchased")
    public void setPricePurchased(Number pricePurchased) {
        this.pricePurchased = pricePurchased;
        this.mapJsonPropertyField.put("Price Purchased", "pricePurchased");
    }
}
