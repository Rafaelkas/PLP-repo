package modules;

import conditions.Condition;
import effects.Effect;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import plpFields.Constant;
import plpFields.ModuleRestriction;
import plpFields.PLPParameter;
import plpFields.ProgressMeasure;
import plpFields.RequiredResource;
import plpFields.Variable;

public class PLP {
    protected String name;
    protected double version;
    protected String glueFile;
    protected List<PLPParameter> inputParams;
    protected List<PLPParameter> execParams;
    protected List<PLPParameter> outputParams;
    protected List<PLPParameter> unobservableParams;
    protected List<Variable> variables;
    protected List<Constant> constants;
    protected List<RequiredResource> requiredResources;
    protected List<Condition> preConditions;
    protected List<Condition> concurrencyConditions;
    protected List<ModuleRestriction> concurrentModules;
    protected boolean completelyMutex;
    protected List<Effect> sideEffects;
    List<ProgressMeasure> progressMeasures;

    public PLP(String name) {
        this.name = name;
        this.inputParams = new LinkedList();
        this.execParams = new LinkedList();
        this.outputParams = new LinkedList();
        this.unobservableParams = new LinkedList();
        this.variables = new LinkedList();
        this.constants = new LinkedList();
        this.requiredResources = new LinkedList();
        this.preConditions = new LinkedList();
        this.concurrencyConditions = new LinkedList();
        this.concurrentModules = new LinkedList();
        this.sideEffects = new LinkedList();
        this.progressMeasures = new LinkedList();
    }

    public String getGlueFile() {
        return this.glueFile;
    }

    public double getVersion() {
        return this.version;
    }

    public void setGlueFile(String glueFile) {
        this.glueFile = glueFile;
    }

    public void setVersion(double version) {
        this.version = version;
    }

    public String getBaseName() {
        return this.name;
    }

    public List<PLPParameter> getInputParams() {
        return this.inputParams;
    }

    public List<PLPParameter> getExecParams() {
        return this.execParams;
    }

    public List<PLPParameter> getOutputParams() {
        return this.outputParams;
    }

    public List<Variable> getVariables() {
        return this.variables;
    }

    public List<Constant> getConstants() {
        return this.constants;
    }

    public List<String> getConstantsNames() {
        List<String> res = (List)this.getConstants().stream().map(Constant::getName).collect(Collectors.toCollection(LinkedList::new));
        return res;
    }

    public List<RequiredResource> getRequiredResources() {
        return this.requiredResources;
    }

    public List<ModuleRestriction> getConcurrentModules() {
        return this.concurrentModules;
    }

    public List<ProgressMeasure> getProgressMeasures() {
        return this.progressMeasures;
    }

    public List<PLPParameter> getUnobservableParams() {
        return this.unobservableParams;
    }

    public List<Condition> getPreConditions() {
        return this.preConditions;
    }

    public List<Condition> getConcurrencyConditions() {
        return this.concurrencyConditions;
    }

    public List<Effect> getSideEffects() {
        return this.sideEffects;
    }

    public boolean isCompletelyMutex() {
        return this.completelyMutex;
    }

    public void addInputParam(PLPParameter p) {
        this.inputParams.add(p);
    }

    public void addExecParam(PLPParameter p) {
        this.execParams.add(p);
    }

    public void addOutputParam(PLPParameter p) {
        this.outputParams.add(p);
    }

    public void addInputParam(String p) {
        this.inputParams.add(new PLPParameter(p));
    }

    public void addExecParam(String p) {
        this.execParams.add(new PLPParameter(p));
    }

    public void addOutputParam(String p) {
        this.outputParams.add(new PLPParameter(p));
    }

    public void addUnobservableParam(PLPParameter p) {
        this.unobservableParams.add(p);
    }

    public void addVariable(Variable v) {
        this.variables.add(v);
    }

    public void addConstant(Constant c) {
        this.constants.add(c);
    }

    public void addRequiredResource(RequiredResource r) {
        this.requiredResources.add(r);
    }

    public void addPreCondition(Condition c) {
        this.preConditions.add(c);
    }

    public void addConcurrencyCondition(Condition c) {
        this.concurrencyConditions.add(c);
    }

    public void addSideEffect(Effect c) {
        this.sideEffects.add(c);
    }

    public void addModuleRestriction(ModuleRestriction mr) {
        this.concurrentModules.add(mr);
    }

    public void setCompletelyMutex() {
        this.completelyMutex = true;
    }

    public void addProgressMeasure(ProgressMeasure pm) {
        this.progressMeasures.add(pm);
    }

    public String toString() {
        return "PLP: " + this.getBaseName() + "\n" + " - Execution Parameters: " + Arrays.toString(this.execParams.toArray()) + "\n" + " - Input Parameters: " + Arrays.toString(this.inputParams.toArray()) + "\n" + " - Output Params: " + Arrays.toString(this.outputParams.toArray()) + "\n" + " - Unobservable Params: " + Arrays.toString(this.unobservableParams.toArray()) + "\n" + " - Variables: " + Arrays.toString(this.variables.toArray()) + "\n" + " - Constants: " + Arrays.toString(this.constants.toArray()) + "\n" + " - Required Resources: " + Arrays.toString(this.requiredResources.toArray()) + "\n" + " - Preconditions: " + Arrays.toString(this.preConditions.toArray()) + "\n" + " - Concurrency Conditions: " + Arrays.toString(this.concurrencyConditions.toArray()) + "\n" + " - Concurrent Modules: " + (this.completelyMutex ? "Completeley Mutex" : Arrays.toString(this.concurrentModules.toArray())) + "\n" + " - Side Effects: " + Arrays.toString(this.sideEffects.toArray()) + "\n" + " - Progress Measures: " + Arrays.toString(this.progressMeasures.toArray());
    }
}
