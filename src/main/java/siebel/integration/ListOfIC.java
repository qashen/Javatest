package siebel.integration;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListOfIC {
    private LineItemIC[] lineItemICS;

    @JsonProperty("Price List Item - Import")
    public void setLineItemICS(LineItemIC[] lineItemICS) {
        this.lineItemICS = lineItemICS;
    }

    public List<LineItemIC> getAllLinItem(){
        return Arrays.stream(getLineItemICS()).collect((Collectors.toList()));
    }
}
