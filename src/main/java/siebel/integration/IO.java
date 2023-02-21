package siebel.integration;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IO {
    private ListOfIC listOfICS;
    private String IntObjectName;
    private String IntObjectFormat;
    private String MessageId;
    private String MessageType;

    @JsonProperty("ListOfSWIISSPriceListItemIO")
    public void setListOfICS(ListOfIC listOfICS) {
        this.listOfICS = listOfICS;
    }

    @JsonProperty("IntObjectName")
    public void setIntObjectName(String intObjectName) {
        IntObjectName = intObjectName;
    }

    @JsonProperty("IntObjectFormat")
    public void setIntObjectFormat(String intObjectFormat) {
        IntObjectFormat = intObjectFormat;
    }

    @JsonProperty("MessageId")
    public void setMessageId(String messageId) {
        MessageId = messageId;
    }

    @JsonProperty("MessageType")
    public void setMessageType(String messageType) {
        MessageType = messageType;
    }
}
