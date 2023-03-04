package pricing.rule;
@Deprecated
public enum RuleType {

    //ROOT, CRITERIA, AND, OR, GROUP_AND, GROUP_OR
    ROOT("ROOT"),

    CRITERIA("CRITERIA"),

    AND("AND"),

    OR("OR"),

    GROUP_AND("GROUP_AND"),

    GROUP_OR("GROUP_OR");

    private final String value;

    RuleType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static RuleType fromValue(String text) {
        for (RuleType b : RuleType.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + text + "'");
    }

}