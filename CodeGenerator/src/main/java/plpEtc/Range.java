package plpEtc;

public class Range {
    private String minValue;
    private String maxValue;
    private boolean minInclusive;
    private boolean maxInclusive;

    public Range(String minValue, boolean minInclusive, String maxValue, boolean maxInclusive) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.maxInclusive = maxInclusive;
        this.minInclusive = minInclusive;
    }

    public String getMinValue() {
        return this.minValue;
    }

    public String getMaxValue() {
        return this.maxValue;
    }

    public boolean isMinInclusive() {
        return this.minInclusive;
    }

    public boolean isMaxInclusive() {
        return this.maxInclusive;
    }

    public String toString() {
        return (this.minInclusive ? "[" : "(") + this.minValue + ", " + this.maxValue + (this.maxInclusive ? "]" : ")");
    }

    public boolean equals(Object obj) {
        if (!this.getClass().isInstance(obj)) {
            return false;
        } else {
            Range robj = (Range)obj;
            return this.minValue.replaceAll("\\s+", "").equalsIgnoreCase(robj.minValue.replaceAll("\\s+", "")) && this.maxValue.replaceAll("\\s+", "").equalsIgnoreCase(robj.maxValue.replaceAll("\\s+", "")) && this.maxInclusive == robj.maxInclusive && this.minInclusive == robj.minInclusive;
        }
    }
}
