package siebel.integration;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IO {
    private ListOfIC listOfICS;

    private String IntObjectName;
    private String IntObjectFormat;
    private String MessageId;
    private String MessageType;

    public IO() {
    }

    public IO(ListOfIC listOfICS, String intObjectName, String intObjectFormat, String messageId, String messageType) {
        this.listOfICS = listOfICS;
        IntObjectName = intObjectName;
        IntObjectFormat = intObjectFormat;
        MessageId = messageId;
        MessageType = messageType;
    }

    public ListOfIC getListOfICS() {
        return listOfICS;
    }
    @JsonProperty("ListOfSWIISSPriceListItemIO")
    public void setListOfICS(ListOfIC listOfICS) {
        this.listOfICS = listOfICS;
    }

    public String getIntObjectName() {
        return IntObjectName;
    }
    @JsonProperty("IntObjectName")
    public void setIntObjectName(String intObjectName) {
        IntObjectName = intObjectName;
    }

    public String getIntObjectFormat() {
        return IntObjectFormat;
    }
    @JsonProperty("IntObjectFormat")
    public void setIntObjectFormat(String intObjectFormat) {
        IntObjectFormat = intObjectFormat;
    }

    public String getMessageId() {
        return MessageId;
    }
    @JsonProperty("MessageId")
    public void setMessageId(String messageId) {
        MessageId = messageId;
    }

    public String getMessageType() {
        return MessageType;
    }
    @JsonProperty("MessageType")
    public void setMessageType(String messageType) {
        MessageType = messageType;
    }
}
