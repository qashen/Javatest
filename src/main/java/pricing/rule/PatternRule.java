package pricing.rule;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Deprecated
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PatternRule {
    private Pattern[] patterns;
    @JsonProperty("pattern")
    public void setPatterns(Pattern[] patterns) {
        this.patterns = patterns;
    }
}
