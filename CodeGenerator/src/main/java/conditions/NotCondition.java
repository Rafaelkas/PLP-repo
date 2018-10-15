package conditions;

import effects.Effect;
import effects.NotEffect;
import plpEtc.ParamHolder;
import plpEtc.Predicate;

public class NotCondition implements Condition {
    Condition condition;

    public NotCondition(Condition c) {
        this.condition = c;
    }

    public Condition getCondition() {
        return this.condition;
    }

    public String toString() {
        return "[Not " + this.condition.toString() + "]";
    }

    public boolean containsParam(String paramName) {
        return this.condition.containsParam(paramName);
    }

    public boolean sharesParams(ParamHolder c) {
        return this.condition.sharesParams(c);
    }

    public Effect createProperEffect() {
        if (!this.condition.getClass().isAssignableFrom(Predicate.class)) {
            throw new UnsupportedOperationException("Can't treat condition " + this.toString() + " as an action effect, " + "the inner condition needs to be a Predicate");
        } else {
            return new NotEffect((Predicate)this.condition);
        }
    }

    public String simpleString() {
        return "not_".concat(this.condition.simpleString());
    }

    public boolean equals(Object obj) {
        return this.getClass().isInstance(obj) ? this.condition.equals(((NotCondition)obj).condition) : false;
    }
}
