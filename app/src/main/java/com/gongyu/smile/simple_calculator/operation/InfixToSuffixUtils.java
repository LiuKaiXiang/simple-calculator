package com.gongyu.smile.simple_calculator.operation;

import android.text.TextUtils;

import java.util.Stack;

public class InfixToSuffixUtils {

    private String symbol = "+-×÷()";

    private String SYMBOL_ADD = "+";
    private String SYMBOL_SUBTRACT = "-";
    private String SYMBOL_MULTIPLY = "×";
    private String SYMBOL_DIVIDE = "÷";
    private String SYMBOL_BRACKET_LEFT = "(";
    private String SYMBOL_BRACKET_RIGHT = ")";

    private String currentNumS = "";
    private Stack<String> outStack = new Stack<>();
    private Stack<String> calculateStack = new Stack<>();

    public synchronized String infixToSuffix(String operation) {
        Stack<String> stack = new Stack<>();

        char[] chars = operation.toCharArray();

        for (char c : chars) {
            String currentChar = String.valueOf(c);
            if (isSymbol(currentChar)) {
                if (!TextUtils.isEmpty(currentNumS)) {
                    outStack.push(currentNumS);
                    currentNumS = "";
                }

                //栈不为空，并且是一级运算符
                if (!stack.empty()) {
                    String afterSymbol = stack.peek();
                    if (!SYMBOL_BRACKET_LEFT.equals(afterSymbol)) {
                        int afterPriority = getSymbolPriority(afterSymbol);
                        int currentPriority = getSymbolPriority(currentChar);
                        if (afterPriority > currentPriority) {//栈顶是高级
                            outputSymbol(stack);
                        } else if (afterPriority == currentPriority) {
                            stack.pop();
                            outStack.push(afterSymbol);
                        }
                    }
                }

                stack.push(currentChar);
                if (SYMBOL_BRACKET_RIGHT.equals(currentChar)) {
                    outputSymbol(stack);
                }
            } else {
                currentNumS += currentChar;
            }
        }

        if (!TextUtils.isEmpty(currentNumS)) {
            outStack.push(currentNumS);
            currentNumS = "";
        }

        outputSymbol(stack);

        String suffixOperation = "";
        while (!outStack.empty()) {
            String pop = outStack.pop();
            suffixOperation = pop + suffixOperation;
            calculateStack.push(pop);
        }
        return suffixOperation;
    }

    /**
     * 获取运算结果
     */
    public String getResult() {
        Stack<String> cacheStack = new Stack<>();

        String number1 = "";//运算数
        String number2 = "";//被运算数

        while (!calculateStack.empty()) {
            String currentChar = calculateStack.pop();

            if (isSymbol(currentChar)) {
                if (cacheStack.size() >= 2) {
                    number2 = cacheStack.pop();
                    number1 = cacheStack.pop();

                    if (SYMBOL_ADD.equals(currentChar)) {
                        cacheStack.push(String.valueOf(getFloat(number1) + getFloat(number2)));
                    } else if (SYMBOL_SUBTRACT.equals(currentChar)) {
                        cacheStack.push(String.valueOf(getFloat(number1) - getFloat(number2)));
                    } else if (SYMBOL_MULTIPLY.equals(currentChar)) {
                        cacheStack.push(String.valueOf(getFloat(number1) * getFloat(number2)));
                    } else if (SYMBOL_DIVIDE.equals(currentChar)) {
                        cacheStack.push(String.valueOf(getFloat(number1) / getFloat(number2)));
                    }
                }
            } else {
                cacheStack.push(currentChar);
            }
        }

        if (!cacheStack.empty()) {
            return cacheStack.pop();
        }

        return "";
    }

    private float getFloat(String number) {
        return Float.valueOf(number);
    }

    //出符号栈
    private void outputSymbol(Stack<String> stack) {
        if (stack.empty()) {
            return;
        }

        String wSymbol = "";
        String symbol1Priority = "";//一级运算符
        String symbol2Priority = "";//二级运算符
        while (!stack.empty() && !SYMBOL_BRACKET_LEFT.equals(wSymbol)) {
            wSymbol = stack.pop();
            if (getSymbolPriority(wSymbol) == 1) {
                symbol1Priority = wSymbol + symbol1Priority;
            }
            if (getSymbolPriority(wSymbol) == 2) {
                symbol2Priority = wSymbol + symbol2Priority;
            }
        }

        char[] chars = (symbol2Priority + symbol1Priority).toCharArray();

        for (char c : chars) {
            outStack.push(String.valueOf(c));
        }
    }

    /**
     * 是否是运算符
     */
    private boolean isSymbol(String chars) {
        return symbol.contains(chars);
    }

    /**
     * 获取符号优先级，数越大优先级越高
     *
     * @param symbol 运算符
     * @return 1：+、-   2：x、÷   3：）（
     */
    private int getSymbolPriority(String symbol) {
        if (SYMBOL_MULTIPLY.equals(symbol) || SYMBOL_DIVIDE.equals(symbol)) {
            return 2;
        } else if (SYMBOL_BRACKET_LEFT.equals(symbol) || SYMBOL_BRACKET_RIGHT.equals(symbol)) {
            return 3;
        }

        return 1;
    }
}
