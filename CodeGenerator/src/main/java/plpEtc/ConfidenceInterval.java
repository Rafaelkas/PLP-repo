package plpEtc;

public class ConfidenceInterval {
    private Range interval;
    private double confidence_level;

    public ConfidenceInterval(Range interval, double confidence_level) {
        this.interval = interval;
        this.confidence_level = confidence_level;
    }

    public Range getInterval() {
        return this.interval;
    }

    public double getConfidence_level() {
        return this.confidence_level;
    }

    public String toString() {
        return "[" + this.interval.toString() + ", " + this.confidence_level + "]";
    }
}
