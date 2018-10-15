package distributions;

import conditions.Condition;

public class ConditionalDist {
    private Distribution dist;
    private Condition condition;

    public ConditionalDist() {
    }

    public ConditionalDist(Distribution dist, Condition condition) {
        this.dist = dist;
        this.condition = condition;
    }

    public boolean isConditional() {
        return this.condition != null;
    }

    public Distribution getDist() {
        return this.dist;
    }

    public Condition getCondition() {
        return this.condition;
    }

    public String toString() {
        return this.condition == null ? this.dist.toString() : "[" + this.dist.toString() + "|" + this.condition.toString() + "]";
    }
}
