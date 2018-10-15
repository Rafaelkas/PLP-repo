package plpFields;

public class RequiredResource {
    private String name;
    private double quantity;
    private RequiredResource.RequirementStatus reqStatus;
    private double frequency;
    private double duration;

    public RequiredResource(String name, RequiredResource.RequirementStatus reqStatus) {
        this.name = name;
        this.reqStatus = reqStatus;
        this.quantity = -1.0D;
    }

    public String getName() {
        return this.name;
    }

    public double getQuantity() {
        return this.quantity;
    }

    public RequiredResource.RequirementStatus getReqStatus() {
        return this.reqStatus;
    }

    public double getFrequency() {
        return this.frequency;
    }

    public double getDuration() {
        return this.duration;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public String toString() {
        return this.reqStatus.equals(RequiredResource.RequirementStatus.Exclusive) ? "[" + this.name + "(exclusive)" + (this.quantity == -1.0D ? "" : "- quantity: " + this.quantity) : "[" + this.name + " - frequency: " + this.frequency + ", duration: " + this.duration + (this.quantity == -1.0D ? "" : ", quantity: " + this.quantity);
    }

    public static enum RequirementStatus {
        Exclusive,
        Frequency;

        private RequirementStatus() {
        }
    }
}
