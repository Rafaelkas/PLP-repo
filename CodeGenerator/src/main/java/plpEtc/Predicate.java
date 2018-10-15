package plpEtc;

import conditions.Condition;
import effects.Effect;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Predicate implements Condition, Effect {
    private String name;
    private List<String> values;

    public Predicate(String name) {
        this.name = name;
        this.values = new LinkedList();
    }

    public void addValue(String value) {
        this.values.add(value);
    }

    public String getName() {
        return this.name;
    }

    public List<String> getValues() {
        return this.values;
    }

    public boolean containsParam(String paramName) {
        Iterator var2 = this.values.iterator();

        String val;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            val = (String)var2.next();
        } while(!val.equals(paramName));

        return true;
    }

    public boolean sharesParams(ParamHolder ph) {
        if (ph.getClass().isAssignableFrom(Predicate.class)) {
            return this.name.equals(((Predicate)ph).getName());
        } else {
            Iterator var2 = this.values.iterator();

            String val;
            do {
                if (!var2.hasNext()) {
                    return false;
                }

                val = (String)var2.next();
            } while(!ph.containsParam(val));

            return true;
        }
    }

    public String toString() {
        int stringLength = Arrays.toString(this.values.toArray()).length();
        return stringLength <= 2 ? "(" + this.name + ")" : "(" + this.name + " " + Arrays.toString(this.values.toArray()).substring(1, stringLength - 1).replaceAll(",", "") + ")";
    }

    public String simpleString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.name);
        Iterator var2 = this.values.iterator();

        while(var2.hasNext()) {
            String s = (String)var2.next();
            sb.append("_").append(s);
        }

        return sb.toString();
    }

    public Effect createProperEffect() {
        return this;
    }

    public boolean equals(Object obj) {
        if (this.getClass().isInstance(obj)) {
            Predicate pobj = (Predicate)obj;
            if (this.name.equals(pobj.name) && this.values.size() == pobj.values.size()) {
                for(int i = 0; i < this.values.size(); ++i) {
                    if (!((String)this.values.get(i)).equals(pobj.values.get(i))) {
                        return false;
                    }
                }

                return true;
            }
        }

        return false;
    }

    public int hashCode() {
        return "predicate".concat(this.name).hashCode();
    }
}
