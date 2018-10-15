package effects;

import plpEtc.ParamHolder;
import plpEtc.Predicate;

public class NotEffect implements Effect {
    private Predicate effect;

    public NotEffect(Predicate effect) {
        this.effect = effect;
    }

    public Predicate getEffect() {
        return this.effect;
    }

    public boolean sharesParams(ParamHolder ph) {
        return this.effect.sharesParams(ph);
    }

    public boolean containsParam(String paramName) {
        return this.effect.containsParam(paramName);
    }

    public String toString() {
        return "[Not " + this.effect.toString() + "]";
    }

    public String simpleString() {
        return "not_".concat(this.effect.simpleString());
    }
}
