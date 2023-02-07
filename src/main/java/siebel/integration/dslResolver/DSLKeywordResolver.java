package siebel.integration.dslResolver;

import lombok.extern.slf4j.Slf4j;
import siebel.integration.dslResolver.resolverImpl.BankResolver;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class DSLKeywordResolver {
    Map<String, DSLResolver> dslKeywordResolverList;

    BankResolver bankResolver;

    public DSLKeywordResolver() {
        dslKeywordResolverList = new HashMap();

    }

    public DSLKeywordResolver(List<DSLResolver> resolverList) {
        dslKeywordResolverList = resolverList.stream()
                .collect(Collectors.toMap(DSLResolver::getResolverKeyword, Function.identity()));
    }

    public Map<String, DSLResolver> getDslKeywordResolverList(){
        return dslKeywordResolverList;
    }

    public Optional<DSLResolver> getResolver(String keyword) {
        bankResolver = new BankResolver();
        dslKeywordResolverList.put (keyword, bankResolver);
        return Optional.ofNullable(dslKeywordResolverList.get(keyword));
    }
}
