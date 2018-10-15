package plpFields;

import plpEtc.FieldType;

public class Constant {
    private String name;
    private FieldType type;
    private String value;

    public Constant(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public FieldType getType() {
        return this.type;
    }

    public String getValue() {
        return this.value;
    }

    public void setType(FieldType type) {
        this.type = type;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(this.name).append(" - ").append(this.type);
        if (this.value != null) {
            sb.append(", value: ").append(this.value);
        }

        sb.append("]");
        return sb.toString();
    }
}
