package effects;

import conditions.Condition;
import plpEtc.ParamHolder;

public class ConditionalEffect implements Effect {
    private Condition condition;
    private Effect effect;

    public ConditionalEffect(Condition condition, Effect effect) {
        this.condition = condition;
        this.effect = effect;
    }

    public Condition getCondition() {
        return this.condition;
    }

    public Effect getEffect() {
        return this.effect;
    }

    public boolean isConditional() {
        return this.condition != null;
    }

    public boolean sharesParams(ParamHolder ph) {
        return this.effect.sharesParams(ph);
    }

    public boolean containsParam(String paramName) {
        return this.effect.containsParam(paramName);
    }

    public String toString() {
        return "[when " + this.effect.toString() + " -> " + this.condition.toString() + "]";
    }

    public String simpleString() {
        return this.condition.simpleString() + "_" + this.effect.simpleString();
    }
}
