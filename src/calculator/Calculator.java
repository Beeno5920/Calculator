package calculator;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class Calculator implements EventHandler<ActionEvent> {
    private String input = "0";
    private double prevResult = 0;
    private boolean computed = false;
    private Label inputDisplay;
    private Label outputDisplay;

    public Calculator(Label inputDisplay, Label outputDisplay) {
        this.inputDisplay = inputDisplay;
        this.outputDisplay = outputDisplay;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        String curr = ((Button)actionEvent.getSource()).getText();

        if (computed) {
            clear();
            computed = false;
        }
        switch (curr) {
            case "AC":
                clear();
                break;
            case "Del":
                delete();
                break;
            case "=":
                try {
                    compute();
                } catch (RuntimeException e) {
                    outputDisplay.setText("Error");
                }
                break;
            case "Ans":
                curr = ((Double)prevResult).toString();
            default:
                if (input.equals("0") && !curr.equals("."))
                    input = curr;
                else
                    input += curr;
        }
        inputDisplay.setText(input);
    }

    private void clear() {
        this.input = "0";
        this.outputDisplay.setText("0");
    }

    private void delete() {
        if (this.input.length() == 1)
            this.input = "0";
        else
            this.input = this.input.substring(0, this.input.length() - 1);
    }

    private void compute() {
        this.computed = true;
        prevResult = eval(this.input);
        outputDisplay.setText(((Double)eval(this.input)).toString());
    }

    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            //        | number | functionName factor | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }
}
