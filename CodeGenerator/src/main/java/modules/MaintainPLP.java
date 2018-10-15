package modules;

import conditions.Condition;
import distributions.ConditionalDist;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import plpEtc.Predicate;
import plpFields.ConditionalProb;
import plpFields.FailureMode;

public class MaintainPLP extends PLP {
    private Condition maintainedCondition = new Predicate("empty-condition");
    private boolean initiallyTrue;
    private List<ConditionalDist> timeUntilTrue;
    private Condition successTerminationCondition = new Predicate("empty-condition");
    private List<Condition> failureTerminationConditions = new LinkedList();
    private List<ConditionalProb> successProb = new LinkedList();
    private List<FailureMode> failureModes = new LinkedList();
    private List<ConditionalProb> generalFailureProb = new LinkedList();
    private List<ConditionalDist> successRuntime = new LinkedList();
    private List<ConditionalDist> failRuntime = new LinkedList();

    public MaintainPLP(String baseName) {
        super(baseName);
    }

    public void setMaintainedCondition(Condition maintainedCondition) {
        this.maintainedCondition = maintainedCondition;
    }

    public void setInitiallyTrue(boolean initiallyTrue) {
        this.initiallyTrue = initiallyTrue;
    }

    public void setSuccessTerminationCondition(Condition successTerminationCondition) {
        this.successTerminationCondition = successTerminationCondition;
    }

    public void addSuccessProb(ConditionalProb prob) {
        this.successProb.add(prob);
    }

    public void addFailureMode(FailureMode fm) {
        this.failureModes.add(fm);
    }

    public void addGeneralFailureProb(ConditionalProb prob) {
        this.generalFailureProb.add(prob);
    }

    public void addSuccessRuntime(ConditionalDist dist) {
        this.successRuntime.add(dist);
    }

    public void addFailureRuntime(ConditionalDist dist) {
        this.failRuntime.add(dist);
    }

    public void addFailureTerminationConditions(Condition c) {
        this.failureTerminationConditions.add(c);
    }

    public String getName() {
        return "Achieve '" + this.name + "'";
    }

    public Condition getMaintainedCondition() {
        return this.maintainedCondition;
    }

    public boolean isInitiallyTrue() {
        return this.initiallyTrue;
    }

    public Condition getSuccessTerminationCondition() {
        return this.successTerminationCondition;
    }

    public List<Condition> getFailureTerminationConditions() {
        return this.failureTerminationConditions;
    }

    public List<ConditionalProb> getSuccessProb() {
        return this.successProb;
    }

    public List<FailureMode> getFailureModes() {
        return this.failureModes;
    }

    public List<ConditionalProb> getGeneralFailureProb() {
        return this.generalFailureProb;
    }

    public List<ConditionalDist> getSuccessRuntime() {
        return this.successRuntime;
    }

    public List<ConditionalDist> getFailRuntime() {
        return this.failRuntime;
    }

    public List<ConditionalDist> getTimeUntilTrue() {
        return this.timeUntilTrue;
    }

    public void setTimeUntilTrue(List<ConditionalDist> timeUntilTrue) {
        this.timeUntilTrue = timeUntilTrue;
    }

    public boolean hasTimeUntilTrue() {
        return this.timeUntilTrue != null;
    }

    public String toString() {
        return super.toString() + "\n" + " - Maintained Condition (initially " + this.initiallyTrue + "): " + this.maintainedCondition.toString() + "\n" + (this.hasTimeUntilTrue() ? " - Runtime Until True: " + Arrays.toString(this.timeUntilTrue.toArray()) + "\n" : "") + " - Success Termination Condition: " + this.successTerminationCondition.toString() + "\n" + " - Failure Termination Conditions: " + Arrays.toString(this.failureTerminationConditions.toArray()) + "\n" + " - Success Prob: " + Arrays.toString(this.successProb.toArray()) + "\n" + " - Failure Modes: " + Arrays.toString(this.failureModes.toArray()) + "\n" + (this.failureModes.isEmpty() ? " - Failure Prob: " + Arrays.toString(this.generalFailureProb.toArray()) : "") + " - Runtime Given Success: " + Arrays.toString(this.successRuntime.toArray()) + "\n" + " - Runtime Given Failure: " + Arrays.toString(this.failRuntime.toArray());
    }
}
