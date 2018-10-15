package plpFields;

import conditions.Condition;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class FailureMode {
    private Condition condition;
    private List<ConditionalProb> probList;

    public FailureMode(Condition condition) {
        this.condition = condition;
        this.probList = new LinkedList();
    }

    public void addProb(ConditionalProb prob) {
        this.probList.add(prob);
    }

    public Condition getCondition() {
        return this.condition;
    }

    public List<ConditionalProb> getProbList() {
        return this.probList;
    }

    public String toString() {
        StringBuilder probSB = new StringBuilder();
        Iterator var2 = this.probList.iterator();

        while(var2.hasNext()) {
            ConditionalProb prob = (ConditionalProb)var2.next();
            probSB.append(prob.toString()).append(" ");
        }

        probSB.deleteCharAt(probSB.length() - 1);
        return "[Fail condition - " + this.condition.toString() + " probability - " + probSB.toString() + "]";
    }
}
