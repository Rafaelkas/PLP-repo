package effects;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import plpEtc.ParamHolder;

public class AndEffect implements Effect {
    private List<Effect> effects = new LinkedList();

    public AndEffect() {
    }

    public void addEffect(Effect effect) {
        this.effects.add(effect);
    }

    public List<Effect> getEffects() {
        return this.effects;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[AND [");
        Iterator var2 = this.effects.iterator();

        while(var2.hasNext()) {
            Effect e = (Effect)var2.next();
            sb.append(e.toString()).append(" ");
        }

        sb.deleteCharAt(sb.length() - 1);
        sb.append("]");
        return sb.toString();
    }

    public boolean sharesParams(ParamHolder ph) {
        Iterator var2 = this.effects.iterator();

        Effect effect;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            effect = (Effect)var2.next();
        } while(!effect.sharesParams(ph));

        return true;
    }

    public boolean containsParam(String paramName) {
        Iterator var2 = this.effects.iterator();

        Effect effect;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            effect = (Effect)var2.next();
        } while(!effect.containsParam(paramName));

        return true;
    }

    public String simpleString() {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < this.effects.size(); ++i) {
            sb.append(((Effect)this.effects.get(i)).simpleString());
            if (i < this.effects.size() - 1) {
                sb.append(".AND.");
            }
        }

        return sb.toString();
    }
}
