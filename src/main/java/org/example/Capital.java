package org.example;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class Capital {
    private String Color;
    private String Make;

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy hh:mm:ss")
    private Date datePurchased;

    private Number pricePurchased;

    public Capital() {
    }

    public Capital(String color, String make, Date datePurchased, Number pricePurchased) {
        Color = color;
        Make = make;
        this.datePurchased = datePurchased;
        this.pricePurchased = pricePurchased;
    }

    public String getColor() {
        return Color;
    }
    @JsonProperty("Color")
    public void setColor(String Color) {
        this.Color = Color;
    }

    public String getMake() {
        return Make;
    }

    @JsonProperty("Make")
    public void setMake(String make) {
        Make = make;
    }

    public Date getDatePurchased() {
        return datePurchased;
    }

    @JsonProperty("Date Purchased")
    public void setDatePurchased(Date datePurchased) {
        this.datePurchased = datePurchased;
    }

    public Number getPricePurchased() {
        return pricePurchased;
    }

    @JsonProperty("Price Purchased")
    public void setPricePurchased(Number pricePurchased) {
        this.pricePurchased = pricePurchased;
    }
}
