package modules;

import conditions.Condition;
import distributions.ConditionalDist;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import plpEtc.ConfidenceInterval;
import plpEtc.Predicate;
import plpFields.ConditionalProb;
import plpFields.ObservationGoal;
import plpFields.PLPParameter;

public class ObservePLP extends PLP {
    private ObservationGoal goal = new Predicate("empty-goal");
    private String resultParameter;
    private List<ConditionalProb> failureToObserveProb = new LinkedList();
    private List<ConditionalProb> correctObservationProb = new LinkedList();
    private ConfidenceInterval correctObservationConfidence;
    private List<ConditionalDist> successRuntime = new LinkedList();
    private List<ConditionalDist> failureRuntime = new LinkedList();
    private Condition failTerminationCond;

    public ObservePLP(String baseName) {
        super(baseName);
    }

    public ObservationGoal getGoal() {
        return this.goal;
    }

    public List<ConditionalProb> getFailureToObserveProb() {
        return this.failureToObserveProb;
    }

    public List<ConditionalProb> getCorrectObservationProb() {
        return this.correctObservationProb;
    }

    public ConfidenceInterval getCorrectObservationConfidence() {
        return this.correctObservationConfidence;
    }

    public List<ConditionalDist> getSuccessRuntime() {
        return this.successRuntime;
    }

    public List<ConditionalDist> getFailureRuntime() {
        return this.failureRuntime;
    }

    public void setGoal(ObservationGoal og) {
        this.goal = og;
    }

    public void addFailureToObserveProb(ConditionalProb cp) {
        this.failureToObserveProb.add(cp);
    }

    public void addCorrectObservationProb(ConditionalProb cp) {
        this.correctObservationProb.add(cp);
    }

    public void setCorrectObservationConfidence(ConfidenceInterval correctObservationConfidence) {
        this.correctObservationConfidence = correctObservationConfidence;
    }

    public void addSuccessRuntime(ConditionalDist cd) {
        this.successRuntime.add(cd);
    }

    public void addFailureRuntime(ConditionalDist cd) {
        this.failureRuntime.add(cd);
    }

    public boolean isGoalParameter() {
        return this.goal.getClass().isAssignableFrom(PLPParameter.class);
    }

    public String getName() {
        return "Observe '" + this.name + "'";
    }

    public void setResultParameterName(String name) {
        this.resultParameter = name;
    }

    public PLPParameter getResultParameter() {
        Iterator var1 = this.getOutputParams().iterator();

        PLPParameter outputParam;
        do {
            if (!var1.hasNext()) {
                return null;
            }

            outputParam = (PLPParameter)var1.next();
        } while(!outputParam.simpleString().equals(this.resultParameter));

        return outputParam;
    }

    public Condition getFailTerminationCond() {
        return this.failTerminationCond;
    }

    public void setFailTerminationCond(Condition failTerminationCond) {
        this.failTerminationCond = failTerminationCond;
    }

    public boolean hasFailTerminationCond() {
        return this.failTerminationCond != null;
    }

    public String toString() {
        return super.toString() + "\n" + " - Observation Goal: " + this.goal.toString() + "\n" + " - Failure to Observe Probability: " + Arrays.toString(this.failureToObserveProb.toArray()) + "\n" + (this.correctObservationConfidence == null ? " - Correct Observation Probability: " + Arrays.toString(this.correctObservationProb.toArray()) + "\n" : " - Correct Observation Confidence Interval: " + this.correctObservationConfidence.toString() + "\n") + " - Runtime Given Success: " + Arrays.toString(this.successRuntime.toArray()) + "\n" + " - Runtime Given Failure: " + Arrays.toString(this.failureRuntime.toArray());
    }
}
