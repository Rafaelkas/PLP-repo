package plpFields;

import conditions.Condition;

public class ProgressMeasure {
    private double frequency;
    private Condition condition;

    public ProgressMeasure(double frequency, Condition condition) {
        this.frequency = frequency;
        this.condition = condition;
    }

    public double getFrequency() {
        return this.frequency;
    }

    public Condition getCondition() {
        return this.condition;
    }

    public String toString() {
        return "[" + this.condition.toString() + " @ " + this.frequency + "Hz]";
    }
}
