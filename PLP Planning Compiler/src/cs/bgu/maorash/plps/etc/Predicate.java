package cs.bgu.maorash.plps.etc;

import cs.bgu.maorash.plps.conditions.Condition;
import cs.bgu.maorash.plps.effects.Effect;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Predicate implements Condition, Effect {

    private String name;
    private List<String> values;

    public Predicate(String name) {
        this.name = name;
        this.values = new LinkedList<>();
    }

    public void addValue(String value) {
        this.values.add(value);
    }

    public String getName() {
        return name;
    }

    public List<String> getValues() {
        return values;
    }

    @Override
    public boolean containsParam(String paramName) {
        for (String val : values) {
            if (val.equals(paramName))
                return true;
        }
        return false;
    }

    @Override
    public boolean sharesParams(ParamHolder ph) {
        if (ph.getClass().isAssignableFrom(Predicate.class)) {
            return name.equals(((Predicate) ph).getName());
        }
        for (String val : values) {
            if (ph.containsParam(val))
                return true;
        }
        return false;
        /*if (c.getClass().isAssignableFrom(Formula.class)) {
            for (PLPParameter p : values) {
                if (p.getName().equals(((Formula) c).getLeftExpr().getName())
                        || p.getName().equals(((Formula) c).getRightExpr()))
                    return true;
            }
            return false;
        }
        return c.sharesParams(this);*/
    }

    public String toString() {
        int stringLength = Arrays.toString(values.toArray()).length();
        if (stringLength <= 2) return "(" + name + ")";
        return "(" + name + " " +Arrays.toString(values.toArray()).substring(1,stringLength-1).replaceAll(",","")+ ")";
    }

    @Override
    public Effect createProperEffect() {
        return this;
    }
}
