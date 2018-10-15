package effects;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import plpEtc.ParamHolder;

public class ForAllEffect implements Effect {
    private Effect effect;
    private List<String> params;

    public ForAllEffect(Effect effect) {
        this.effect = effect;
        this.params = new LinkedList();
    }

    public void addParam(String paramName) {
        this.params.add(paramName);
    }

    public Effect getEffect() {
        return this.effect;
    }

    public List<String> getParams() {
        return this.params;
    }

    public boolean sharesParams(ParamHolder ph) {
        return this.effect.sharesParams(ph);
    }

    public boolean containsParam(String paramName) {
        return this.effect.containsParam(paramName);
    }

    public String toString() {
        return "[forall " + Arrays.toString(this.params.toArray()) + "->" + this.effect.toString() + "]";
    }

    public String simpleString() {
        StringBuilder sb = new StringBuilder();
        sb.append("forall");
        Iterator var2 = this.params.iterator();

        while(var2.hasNext()) {
            String s = (String)var2.next();
            sb.append("_").append(s);
        }

        sb.append("_").append(this.effect.simpleString());
        return sb.toString();
    }
}
