package siebel.integration.ruleEngine;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
public class KnowledgeBaseService {
    private Rule[] rules;

    public Rule[] getRule() {
        return rules;
    }

    @JsonProperty("knowledgebase")
    public void setRule(Rule[] rules) {
        this.rules = rules;
    }

    public List<Rule> getAllRuleByNamespace(String ruleNamespace){
        return Arrays.stream(getRule()).filter(c -> c.ruleNamespace.toString().equals(ruleNamespace)).collect((Collectors.toList()));
    }

    public List<Rule> getAllRule(){
        return Arrays.stream(getRule()).collect((Collectors.toList()));
    }
}
