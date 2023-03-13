package pricing;

import pricing.ruleEngine.Rule;
import pricing.ruleEngine.RuleEngine;
import pricing.rulesImpl.CBDPricingServiceImpl;
import pricing.util.constants;
import pricing.util.PricingUtil;
import pricing.rulesImpl.PricingInterferenceEngine;

import java.io.InputStream;
import java.util.*;
@SuppressWarnings({ "unchecked"})
public class Main {
    public static void main(String[] args) {

        CBDPricingServiceImpl cbdPricingService = new CBDPricingServiceImpl();
        ClassLoader classLoader = cbdPricingService.getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("siebelmessage_PO1.json");
        //InputStream rule = classLoader.getResourceAsStream("pattern.json");
        InputStream rule = classLoader.getResourceAsStream("promotion_buying_pricelist.json");
        InputStream resource = classLoader.getResourceAsStream("values.yaml");

        Map<String, Object> mainObject = (Map<String, Object>) cbdPricingService.buildRule(is, rule, resource);
        List<Object> lineItem = (List<Object>) mainObject.get(constants.INPUT);
        Map<String, Object> mapRuleList= (Map<String, Object>) mainObject.get(constants.RULE);
        Map<String, String> mapFields = (Map<String, String>) mainObject.get(constants.FIELD_MAP);
        PricingUtil.setFields(mapFields, lineItem);

        System.out.println("Below is rule engine test\n");

        RuleEngine ruleEngine = new RuleEngine();

        PricingInterferenceEngine pricingInterference = new PricingInterferenceEngine();
        for (Map.Entry<String, Object> ruleList : mapRuleList.entrySet()) {
            if (!Objects.isNull(ruleList.getValue()) && ruleList.getValue() instanceof Collection) {
                List<Object> actionResults = ruleEngine.run(pricingInterference, Collections.singletonList(lineItem), Collections.singletonList(ruleList.getValue()));
                System.out.println(PricingUtil.convertObjectToJson(actionResults));
            }
        }


    }
}
