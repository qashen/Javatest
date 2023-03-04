package pricing;

import pricing.ruleEngine.Rule;
import pricing.ruleEngine.RuleEngine;
import pricing.rulesImpl.CBDPricingServiceImpl;
import pricing.util.constants;
import pricing.rulesImpl.PricingInterferenceEngine;

import java.io.InputStream;
import java.util.*;
@SuppressWarnings({ "unchecked", "rawtypes" })
public class Main {
    public static void main(String[] args) {

        CBDPricingServiceImpl cbdPricingService = new CBDPricingServiceImpl();
        ClassLoader classLoader = cbdPricingService.getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("siebelmessage_PO.json");
        InputStream rule = classLoader.getResourceAsStream("pattern.json");
        InputStream resource = classLoader.getResourceAsStream("values.yaml");

        Map<String, Object> mainObject = (Map<String, Object>) cbdPricingService.buildRule(is, rule, resource);
        List<Object> lineItem = (List<Object>) mainObject.get(constants.INPUT);
        List<Rule> ruleList = (List<Rule>) mainObject.get(constants.RULE);
        Map<String, String> mapFields = new HashMap<>();
        mapFields.put("Quantity", "quantity");
        mapFields.put("Service Account Country", "country");
        mapFields.put("Service Account City", "city");
        mapFields.put("Name", "productOffering");
        mapFields.put("Current Price", "currentPrice");

            /*
            Map<String, String> mapProperties = new HashMap<>();
            mapProperties.put("Quantity", "quantity");
            mapProperties.put("Country", "country");
            mapProperties.put("City", "city");
            mapProperties.put("Product Offering", "productOffering");
*/

        if (lineItem.size() > 0) {
            for (Object item : lineItem) {
                for (Map.Entry<String, String> m : mapFields.entrySet()) {
                    {
                        if (((LinkedHashMap<?, ?>) item).get(m.getKey()) != null) {
                            ((LinkedHashMap) item).put(m.getValue(), ((LinkedHashMap<?, ?>) item).get(m.getKey()));
                        }
                    }
                }
            }
        }

        System.out.println("Below is rule engine test");

        RuleEngine ruleEngine = new RuleEngine();

        PricingInterferenceEngine pricingInterference = new PricingInterferenceEngine();
        List<Object> actionResults = ruleEngine.run(pricingInterference, Collections.singletonList(lineItem), Collections.singletonList(ruleList));
        for (Object actionResult : actionResults) {
            System.out.println(actionResult);
        }
        System.out.println("End");

    }
}
