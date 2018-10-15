package modules;

import conditions.Condition;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import plpEtc.Predicate;
import plpFields.ConditionalProb;
import plpFields.PLPParameter;

public class DetectPLP extends PLP {
    private Condition goal = new Predicate("empty-goal");
    private List<ConditionalProb> successProbGivenCondition = new LinkedList();
    private String resultParameter;
    private Condition failTerminationCond;

    public DetectPLP(String baseName) {
        super(baseName);
    }

    public Condition getGoal() {
        return this.goal;
    }

    public List<ConditionalProb> getSuccessProbGivenCondition() {
        return this.successProbGivenCondition;
    }

    public void setGoal(Condition goal) {
        this.goal = goal;
    }

    public void addSuccessProbGivenCond(ConditionalProb prob) {
        this.successProbGivenCondition.add(prob);
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
        return super.toString() + "\n" + " - Detection Goal: " + this.goal.toString() + "\n" + " - Success Prob Given Condition: " + Arrays.toString(this.successProbGivenCondition.toArray());
    }
}
