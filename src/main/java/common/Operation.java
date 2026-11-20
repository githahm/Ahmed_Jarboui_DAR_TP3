package common;

import java.io.Serializable;

public class Operation implements Serializable {
    private static final long serialVersionUID = 1L;

    public double num1;
    public double num2;
    public char operator; // '+', '-', '*', '/'

    public Operation(double num1, double num2, char operator) {
        this.num1 = num1;
        this.num2 = num2;
        this.operator = operator;
    }
}