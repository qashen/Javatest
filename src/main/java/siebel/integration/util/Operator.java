package siebel.integration.util;

import java.util.stream.Stream;

public enum Operator {

    EQUALS("=="),
    GREATEREQUAL(">="),
    GREATERTHAN(">"),
    NOTEQUAL("<>"),
    SMALLEREQUAL("<="),
    SMALLERTHAN("<");

    private String mathsymbol;

    Operator(String mathsymbol) {
        this.mathsymbol = mathsymbol;
    }

    public static Stream<org.example.Operator> stream() {
        return Stream.of(org.example.Operator.values());
    }

    // standard getters and setters
    public String getMathsymbol() {
        return mathsymbol;
    }

    public void setMathsymbol(String mathsymbol) {
        this.mathsymbol = mathsymbol;
    }
}
