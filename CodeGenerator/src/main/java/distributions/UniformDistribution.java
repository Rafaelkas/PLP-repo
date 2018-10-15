package distributions;

public class UniformDistribution implements Distribution {
    private String lowerBound;
    private String upperBound;

    public UniformDistribution(String lowerBound, String upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public String getLowerBound() {
        return this.lowerBound;
    }

    public String getUpperBound() {
        return this.upperBound;
    }

    public String toString() {
        return "Uniform(" + this.lowerBound + "," + this.upperBound + ")";
    }
}
