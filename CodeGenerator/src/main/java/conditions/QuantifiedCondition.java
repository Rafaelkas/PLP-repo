package conditions;

import effects.Effect;
import effects.ForAllEffect;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import plpEtc.ParamHolder;

public class QuantifiedCondition implements Condition {
    private List<String> params = new LinkedList();
    private Condition condition;
    private QuantifiedCondition.Quantifier quantifier;

    public QuantifiedCondition(Condition c, QuantifiedCondition.Quantifier quantifier) {
        this.condition = c;
        this.quantifier = quantifier;
    }

    public QuantifiedCondition.Quantifier getQuantifier() {
        return this.quantifier;
    }

    public Condition getCondition() {
        return this.condition;
    }

    public void addParam(String param) {
        this.params.add(param);
    }

    public String toString() {
        return (this.quantifier.equals(QuantifiedCondition.Quantifier.FORALL) ? "[forall " : "[exists ") + Arrays.toString(this.params.toArray()) + "->" + this.condition.toString() + "]";
    }

    public String simpleString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.quantifier.toString().toLowerCase());
        Iterator var2 = this.params.iterator();

        while(var2.hasNext()) {
            String s = (String)var2.next();
            sb.append("_").append(s);
        }

        sb.append("_").append(this.condition.simpleString());
        return sb.toString();
    }

    public List<String> getParams() {
        return this.params;
    }

    public boolean containsParam(String paramName) {
        return this.condition.containsParam(paramName);
    }

    public boolean sharesParams(ParamHolder c) {
        return this.condition.sharesParams(c);
    }

    public Effect createProperEffect() {
        if (this.quantifier.equals(QuantifiedCondition.Quantifier.EXISTS)) {
            throw new UnsupportedOperationException("Can't treat condition " + this.toString() + " as an action effect");
        } else {
            ForAllEffect feEffect = new ForAllEffect(this.condition.createProperEffect());
            Iterator var2 = this.params.iterator();

            while(var2.hasNext()) {
                String param = (String)var2.next();
                feEffect.addParam(param);
            }

            return feEffect;
        }
    }

    public boolean equals(Object obj) {
        if (this.getClass().isInstance(obj)) {
            QuantifiedCondition qObj = (QuantifiedCondition)obj;
            if (this.quantifier == qObj.quantifier && this.condition.equals(qObj.condition) && this.params.size() == qObj.params.size()) {
                for(int i = 0; i < this.params.size(); ++i) {
                    if (!((String)this.params.get(i)).equals(qObj.params.get(i))) {
                        return false;
                    }
                }

                return true;
            }
        }

        return false;
    }

    public int hashCode() {
        return (this.quantifier.toString() + "_" + this.condition.hashCode()).hashCode();
    }

    public static enum Quantifier {
        EXISTS,
        FORALL;

        private Quantifier() {
        }
    }
}
