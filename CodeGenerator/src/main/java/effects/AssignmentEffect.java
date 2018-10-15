package effects;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import plpEtc.ParamHolder;
import plpFields.PLPParameter;

public class AssignmentEffect implements Effect {
    private PLPParameter param;
    private String expression;
    private String key_desc;

    public AssignmentEffect(PLPParameter param, String expression) {
        this.param = param;
        this.expression = expression;
    }

    public PLPParameter getParam() {
        return this.param;
    }

    public String getExpression() {
        return this.expression;
    }

    public void setDescription(String desc) {
        this.key_desc = desc;
    }

    public String getKeyDesc() {
        return this.key_desc;
    }

    public boolean containsParam(String paramName) {
        if (paramName.equals(this.param.toString())) {
            return true;
        } else {
            Pattern p = Pattern.compile("[a-zA-Z]\\w*|[_]\\w+");
            Matcher matcher = p.matcher(this.expression);

            do {
                if (!matcher.find()) {
                    return false;
                }
            } while(!paramName.equals(matcher.group()));

            return true;
        }
    }

    public boolean sharesParams(ParamHolder c) {
        Pattern p = Pattern.compile("[_a-zA-Z]\\w*");
        Matcher matcher;
        if (!Arrays.asList("TRUE", "FALSE", "NULL").contains(this.expression)) {
            matcher = p.matcher(this.param.toString());
        } else {
            matcher = p.matcher(this.expression.concat("|").concat(this.param.toString()));
        }

        do {
            if (!matcher.find()) {
                return false;
            }
        } while(!c.containsParam(matcher.group()));

        return true;
    }

    public String toString() {
        return "[" + this.param.toString() + " = " + this.expression + "]";
    }

    public String simpleString() {
        return this.key_desc;
    }
}
