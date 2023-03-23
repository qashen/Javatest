package pricing;

import pricing.ruleEngine.Event;
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
        InputStream is = classLoader.getResourceAsStream("siebelmessage_PO_match.json");
        InputStream rule = classLoader.getResourceAsStream("promotion_buying_pricelist.json");
        InputStream resource = classLoader.getResourceAsStream("values.yaml");
        System.out.println("Below is rule engine test\n");
        System.out.println(PricingUtil.convertObjectToJson(cbdPricingService.priceCBD(is, rule, resource)));
    }
}
