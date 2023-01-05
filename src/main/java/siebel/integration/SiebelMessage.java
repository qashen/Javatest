package siebel.integration;

import com.fasterxml.jackson.annotation.JsonProperty;
import siebel.integration.IO;

public class SiebelMessage {

    private IO io;

    public SiebelMessage() {
    }

    public SiebelMessage(IO io) {
        this.io = io;
    }

    public IO getIo() {
        return io;
    }
    @JsonProperty("SiebelMessage")
    public void setIo(IO io) {
        this.io = io;
    }
}
