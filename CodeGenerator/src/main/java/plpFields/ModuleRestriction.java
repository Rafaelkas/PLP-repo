package plpFields;

public class ModuleRestriction {
    private String moduleName;
    private ModuleRestriction.ConcurrencyType type;

    public ModuleRestriction(String moduleName, ModuleRestriction.ConcurrencyType type) {
        this.moduleName = moduleName;
        this.type = type;
    }

    public String getModuleName() {
        return this.moduleName;
    }

    public ModuleRestriction.ConcurrencyType getType() {
        return this.type;
    }

    public String toString() {
        return "[" + this.moduleName + " - " + this.type.toString() + "]";
    }

    public static enum ConcurrencyType {
        Mutex,
        Parallel;

        private ConcurrencyType() {
        }
    }
}
