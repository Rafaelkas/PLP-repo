package conditions;

import effects.AssignmentEffect;
import effects.Effect;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import plpEtc.ParamHolder;
import plpEtc.Range;
import plpFields.PLPParameter;

public class Formula implements Condition {
    private String operator;
    private String leftExpr;
    private String rightExpr;
    private String key_desc;
    private Range inRange;
    private boolean isMeasure;
    private String input;

    public Formula(String leftExpr, String rightExpr, String operator) {
        this.leftExpr = leftExpr;
        this.rightExpr = rightExpr;
        this.operator = operator;
    }

    public Formula(String leftExpr, Range inRange, boolean isMeasure, String input) {
        this.leftExpr = leftExpr;
        this.inRange = inRange;
        this.isMeasure = isMeasure;
        this.input = input;
    }

    public void setDescription(String desc) {
        this.key_desc = desc;
    }

    public boolean getIsMeasure() {
        return this.isMeasure;
    }

    public String getKeyDesc() {
        return this.key_desc;
    }

    public String getInput() {
        return this.input;
    }

    public Range getRange() {
        return this.inRange;
    }

    public String getOperator() {
        return this.operator;
    }

    public String getRightExpr() {
        return this.rightExpr;
    }

    public String getLeftExpr() {
        return this.leftExpr;
    }

    public String toString() {
        return this.inRange == null ? "[" + this.leftExpr + " " + this.operator + " " + this.rightExpr + "]" : "[" + this.leftExpr + " in " + this.inRange.toString() + "]";
    }

    public boolean containsParam(String paramName) {
        Pattern p = Pattern.compile("[a-zA-Z]\\w*|[_]\\w+");
        Matcher matcher;
        if (this.rightExpr != null) {
            matcher = p.matcher(this.leftExpr.concat("|").concat(this.rightExpr));
        } else {
            matcher = p.matcher(this.leftExpr.concat("|").concat(this.inRange.getMinValue()).concat("|").concat(this.inRange.getMaxValue()));
        }

        do {
            if (!matcher.find()) {
                return false;
            }
        } while(!paramName.equals(matcher.group()));

        return true;
    }

    public boolean sharesParams(ParamHolder c) {
        Pattern p = Pattern.compile("[_a-zA-Z]\\w*");
        Matcher matcher;
        if (this.rightExpr != null && !Arrays.asList("TRUE", "FALSE", "NULL").contains(this.rightExpr)) {
            matcher = p.matcher(this.leftExpr.concat("|").concat(this.rightExpr));
        } else if (this.inRange != null) {
            matcher = p.matcher(this.leftExpr.concat("|").concat(this.inRange.getMinValue()).concat("|").concat(this.inRange.getMaxValue()));
        } else {
            matcher = p.matcher(this.leftExpr);
        }

        do {
            if (!matcher.find()) {
                return false;
            }
        } while(!c.containsParam(matcher.group()));

        return true;
    }

    public Effect createProperEffect() {
        if (this.leftExpr.matches(PLPParameter.PLPParameterRegex) && this.rightExpr != null) {
            return new AssignmentEffect(PLPParameter.createParamFromString(this.leftExpr), this.rightExpr);
        } else {
            throw new UnsupportedOperationException("Can't treat condition " + this.toString() + " as an action effect, " + "the left expression needs to be a parameter");
        }
    }

    public String simpleString() {
        return this.key_desc;
    }

    public boolean equals(Object obj) {
        if (this.getClass().isInstance(obj)) {
            Formula fobj = (Formula)obj;
            if (this.inRange == null && fobj.inRange == null && this.operator.equals(fobj.operator) && this.leftExpr.replaceAll("\\s+", "").equalsIgnoreCase(fobj.leftExpr.replaceAll("\\s+", "")) && this.rightExpr.replaceAll("\\s+", "").equalsIgnoreCase(fobj.rightExpr.replaceAll("\\s+", ""))) {
                return true;
            }

            if (this.inRange != null && fobj.inRange != null && this.leftExpr.replaceAll("\\s+", "").equalsIgnoreCase(fobj.leftExpr.replaceAll("\\s+", "")) && this.inRange.equals(fobj.inRange)) {
                return true;
            }
        }

        return false;
    }

    public int hashCode() {
        return "formula".concat(this.leftExpr.replaceAll("\\s+", "")).hashCode();
    }
}
