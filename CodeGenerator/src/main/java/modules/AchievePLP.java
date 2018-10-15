package modules;

import conditions.Condition;
import distributions.ConditionalDist;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import plpEtc.Predicate;
import plpFields.ConditionalProb;
import plpFields.FailureMode;

public class AchievePLP extends PLP {
    private Condition goal = new Predicate("empty-goal");
    private List<ConditionalProb> successProb = new LinkedList();
    private List<FailureMode> failureModes = new LinkedList();
    private List<ConditionalProb> generalFailureProb = new LinkedList();
    private List<ConditionalDist> successRuntime = new LinkedList();
    private List<ConditionalDist> failRuntime = new LinkedList();
    private Condition successTerminationCond;
    private Condition failTerminationCond;

    public AchievePLP(String baseName) {
        super(baseName);
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

    public Condition getGoal() {
        return this.goal;
    }

    public void setGoal(Condition c) {
        this.goal = c;
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

    public String getName() {
        return "Achieve '" + this.name + "'";
    }

    public Condition getFailTerminationCond() {
        return this.failTerminationCond;
    }

    public void setFailTerminationCond(Condition failTerminationCond) {
        this.failTerminationCond = failTerminationCond;
    }

    public Condition getSuccessTerminationCond() {
        return this.successTerminationCond;
    }

    public void setSuccessTerminationCond(Condition successTerminationCond) {
        this.successTerminationCond = successTerminationCond;
    }

    public boolean hasSuccessTerminationCond() {
        return this.successTerminationCond != null;
    }

    public boolean hasFailTerminationCond() {
        return this.failTerminationCond != null;
    }

    public String toString() {
        return super.toString() + "\n" + " - Achievement Goal: " + this.goal.toString() + "\n" + " - Success Prob: " + Arrays.toString(this.successProb.toArray()) + "\n" + " - Failure Modes: " + Arrays.toString(this.failureModes.toArray()) + "\n" + (this.failureModes.isEmpty() ? " - Failure Prob: " + Arrays.toString(this.generalFailureProb.toArray()) + "\n" : "") + " - Success Runtime: " + Arrays.toString(this.successRuntime.toArray()) + "\n" + " - Failure Runtime: " + Arrays.toString(this.failRuntime.toArray()) + (this.successTerminationCond == null ? "" : "\n - Success Termination Condition: " + this.successTerminationCond.toString()) + (this.failTerminationCond == null ? "" : "\n - Fail Termination Condition: " + this.failTerminationCond.toString());
    }
}
