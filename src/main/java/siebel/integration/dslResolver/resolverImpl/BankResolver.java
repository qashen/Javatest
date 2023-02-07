package siebel.integration.dslResolver.resolverImpl;

import siebel.integration.dslResolver.DSLResolver;

public class BankResolver implements DSLResolver {
    private static final String RESOLVER_KEYWORD = "bank";
    private static final String INTEREST = "interest";
    private static final String TARGET_DONE = "target_done";

    @Override
    public String getResolverKeyword() {
        return RESOLVER_KEYWORD;
    }

    @Override
    public Object resolveValue(String keyword) {
        if (keyword.equalsIgnoreCase(INTEREST)) {
            //Code to calculate the current variable interest rates.
            return 9.0;
        }

        if (keyword.equalsIgnoreCase(TARGET_DONE)) {
            //Code to see the bank target of giving loan for this current year is done or not.
            return false;
        }

        return null;
    }
}
