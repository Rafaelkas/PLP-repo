package plpFields;

import conditions.Condition;
import plpEtc.Probability;

public class ConditionalProb implements Probability {
    private String prob;
    private Condition condition;

    public ConditionalProb(String prob, Condition condition) {
        this.prob = prob;
        this.condition = condition;
    }

    public String getProb() {
        return this.prob;
    }

    public Condition getCondition() {
        return this.condition;
    }

    public boolean isConditional() {
        return this.condition != null;
    }

    public String toString() {
        return this.condition == null ? this.prob : "[" + this.prob + "|" + this.condition.toString() + "]";
    }
}
