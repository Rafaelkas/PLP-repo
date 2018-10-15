package distributions;

public class UnkownDistribution implements Distribution {
    private String description;

    public UnkownDistribution(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public String toString() {
        return "(unknown): " + this.description;
    }
}
