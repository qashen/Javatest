package siebel.integration;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SiebelMessage {
    private IO io;
    @JsonProperty("SiebelMessage")
    public void setIo(IO io) {
        this.io = io;
    }
}
