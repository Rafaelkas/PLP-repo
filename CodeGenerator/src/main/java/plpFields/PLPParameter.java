package plpFields;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PLPParameter implements ObservationGoal {
    public static String PLPParameterRegex = "\\b[^()]+\\((.*)\\)$|\\b[^()]+";
    private String name;
    private List<String> paramFieldValues;
    private double readFrequency;
    private String errorParam;

    public PLPParameter(String name) {
        this.name = name;
        this.paramFieldValues = new LinkedList();
    }

    public PLPParameter(String name, List<String> paramFieldValues) {
        this.name = name;
        this.paramFieldValues = paramFieldValues;
    }

    public String getName() {
        return this.name;
    }

    public List<String> getParamFieldValues() {
        return this.paramFieldValues;
    }

    public void addParamFieldValue(String val) {
        this.paramFieldValues.add(val);
    }

    public void setReadFrequency(double readFrequency) {
        this.readFrequency = readFrequency;
    }

    public void setErrorParam(String errorParam) {
        this.errorParam = errorParam;
    }

    public boolean containsParam(String paramName) {
        if (paramName.indexOf(40) >= 0) {
            String name = paramName.substring(0, paramName.indexOf(40));
            return this.name.equals(name);
        } else {
            return this.name.equals(paramName);
        }
    }

    public String toString() {
        if (this.paramFieldValues.isEmpty()) {
            return this.name;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(this.name).append("(");
            Iterator var2 = this.paramFieldValues.iterator();

            while(var2.hasNext()) {
                String s = (String)var2.next();
                sb.append(s).append(", ");
            }

            sb.deleteCharAt(sb.length() - 1);
            sb.deleteCharAt(sb.length() - 1);
            sb.append(")");
            return sb.toString();
        }
    }

    public String simpleString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.name);
        Iterator var2 = this.paramFieldValues.iterator();

        while(var2.hasNext()) {
            String s = (String)var2.next();
            sb.append("_").append(s);
        }

        return sb.toString();
    }

    public static PLPParameter createParamFromString(String param) {
        if (!param.matches(PLPParameterRegex)) {
            return null;
        } else {
            Pattern p = Pattern.compile("[_a-zA-Z]\\w*");
            Matcher matcher = p.matcher(param);
            boolean isFirstMatch = true;

            PLPParameter resultParam;
            for(resultParam = null; matcher.find(); isFirstMatch = false) {
                if (isFirstMatch) {
                    resultParam = new PLPParameter(matcher.group());
                } else {
                    resultParam.addParamFieldValue(matcher.group());
                }
            }

            return resultParam;
        }
    }
}
