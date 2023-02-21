package siebel.integration.rule;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CriteriaGroup {
    private Criteria[] criteria;
    private String id;
    private String groupName;
    private String relationTypeInGroup;
}
