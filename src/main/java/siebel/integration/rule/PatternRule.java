package siebel.integration.rule;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
public class PatternRule {
    private Pattern[] patterns;

    public PatternRule() {
    }

    public PatternRule(Pattern[] patterns) {
        this.patterns = patterns;
    }

    public Pattern[] getPatterns() {
        return patterns;
    }
    @JsonProperty("pattern")
    public void setPatterns(Pattern[] patterns) {
        this.patterns = patterns;
    }
}
