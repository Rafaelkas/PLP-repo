package cs.bgu.maorash.plps.modules;

import cs.bgu.maorash.plps.conditions.Condition;
import cs.bgu.maorash.plps.distributions.ConditionalDist;
import cs.bgu.maorash.plps.etc.Predicate;
import cs.bgu.maorash.plps.plpFields.ConditionalProb;
import cs.bgu.maorash.plps.plpFields.FailureMode;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by maorash
 * maorash@cs.bgu.ac.il
 */
public class MaintainPLP extends PLP {

    private Condition maintainedCondition;
    private boolean initiallyTrue;

    private Condition successTerminationCondition;
    private List<Condition> failureTerminationConditions;

    private List<ConditionalProb> successProb;

    private List<FailureMode> failureModes;
    private List<ConditionalProb> generalFailureProb;

    private List<ConditionalDist> successRuntime;
    private List<ConditionalDist> failRuntime;

    public MaintainPLP(String baseName) {
        super(baseName);
        this.failureTerminationConditions = new LinkedList<>();
        this.successProb = new LinkedList<>();
        this.failureModes = new LinkedList<>();
        this.generalFailureProb = new LinkedList<>();
        this.successRuntime = new LinkedList<>();
        this.failRuntime = new LinkedList<>();
        this.maintainedCondition = new Predicate("empty-condition");
        this.successTerminationCondition = new Predicate("empty-condition");
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
        successProb.add(prob);
    }

    public void addFailureMode(FailureMode fm) {
        failureModes.add(fm);
    }

    public void addGeneralFailureProb(ConditionalProb prob) {
        generalFailureProb.add(prob);
    }

    public void addSuccessRuntime(ConditionalDist dist) {
        successRuntime.add(dist);
    }

    public void addFailureRuntime(ConditionalDist dist) {
        failRuntime.add(dist);
    }

    public void addFailureTerminationConditions(Condition c) {
        failureTerminationConditions.add(c);
    }

    public String getName() {
        return "Achieve '"+name+"'";
    }

    public Condition getMaintainedCondition() {
        return maintainedCondition;
    }

    public boolean isInitiallyTrue() {
        return initiallyTrue;
    }

    public Condition getSuccessTerminationCondition() {
        return successTerminationCondition;
    }

    public List<Condition> getFailureTerminationConditions() {
        return failureTerminationConditions;
    }

    public List<ConditionalProb> getSuccessProb() {
        return successProb;
    }

    public List<FailureMode> getFailureModes() {
        return failureModes;
    }

    public List<ConditionalProb> getGeneralFailureProb() {
        return generalFailureProb;
    }

    public List<ConditionalDist> getSuccessRuntime() {
        return successRuntime;
    }

    public List<ConditionalDist> getFailRuntime() {
        return failRuntime;
    }

    @Override
    public String toString() {
        return super.toString()  + "\n" +
                " - Maintained Condition: " + maintainedCondition.toString() + "(initially "+initiallyTrue+")\n" +
                " - Success Termination Condition: " + successTerminationCondition.toString() + "\n" +
                " - Failure Termination Conditions: " + Arrays.toString(failureTerminationConditions.toArray()) + "\n" +
                " - Success Prob: " + Arrays.toString(successProb.toArray()) + "\n" +
                " - Failure Modes: " + Arrays.toString(failureModes.toArray()) + "\n" +
                (failureModes.isEmpty() ? " - Failure Prob: " + Arrays.toString(generalFailureProb.toArray()) : "") +
                " - Runtime Given Success: " + Arrays.toString(successRuntime.toArray()) + "\n" +
                " - Runtime Given Failure: " + Arrays.toString(failRuntime.toArray()) ;
    }
}
