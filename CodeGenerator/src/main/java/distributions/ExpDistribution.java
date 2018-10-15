package distributions;

public class ExpDistribution implements Distribution {
    private String lambda;

    public ExpDistribution(String lambda) {
        this.lambda = lambda;
    }

    public String getLambda() {
        return this.lambda;
    }

    public String toString() {
        return "Exp(" + this.lambda + ")";
    }
}
