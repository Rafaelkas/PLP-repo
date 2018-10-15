package conditions;

import effects.AndEffect;
import effects.Effect;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import plpEtc.ParamHolder;

public class BitwiseOperation implements Condition {
    private List<Condition> conditions;
    private BitwiseOperation.Operation operation;

    public BitwiseOperation(BitwiseOperation.Operation op) {
        this.conditions = new LinkedList();
        this.operation = op;
    }

    public BitwiseOperation(BitwiseOperation.Operation op, List<Condition> conditions) {
        this.conditions = conditions;
        this.operation = op;
    }

    public void addCondition(Condition c) {
        this.conditions.add(c);
    }

    public List<Condition> getConditions() {
        return this.conditions;
    }

    public BitwiseOperation.Operation getOperation() {
        return this.operation;
    }

    public boolean containsParam(String paramName) {
        Iterator var2 = this.conditions.iterator();

        Condition c;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            c = (Condition)var2.next();
        } while(!c.containsParam(paramName));

        return true;
    }

    public boolean sharesParams(ParamHolder c) {
        Iterator var2 = this.conditions.iterator();

        Condition condition;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            condition = (Condition)var2.next();
        } while(!condition.sharesParams(c));

        return true;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(this.operation).append(" ");
        Iterator var2 = this.conditions.iterator();

        while(var2.hasNext()) {
            Condition c = (Condition)var2.next();
            sb.append(c.toString()).append(" ");
        }

        sb.deleteCharAt(sb.length() - 1);
        sb.append("]");
        return sb.toString();
    }

    public Effect createProperEffect() {
        if (this.operation.equals(BitwiseOperation.Operation.OR)) {
            throw new UnsupportedOperationException("Can't treat condition " + this.toString() + " as an action effect");
        } else {
            AndEffect andEffect = new AndEffect();
            Iterator var2 = this.conditions.iterator();

            while(var2.hasNext()) {
                Condition c = (Condition)var2.next();
                andEffect.addEffect(c.createProperEffect());
            }

            return andEffect;
        }
    }

    public String simpleString() {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < this.conditions.size(); ++i) {
            sb.append(((Condition)this.conditions.get(i)).simpleString());
            if (i < this.conditions.size() - 1) {
                sb.append(this.operation);
            }
        }

        return sb.toString();
    }

    public boolean equals(Object obj) {
        if (this.getClass().isInstance(obj)) {
            BitwiseOperation bObj = (BitwiseOperation)obj;
            if (this.operation == bObj.operation && this.conditions.size() == bObj.conditions.size()) {
                for(int i = 0; i < this.conditions.size(); ++i) {
                    if (!((Condition)this.conditions.get(i)).equals(bObj.conditions.get(i))) {
                        return false;
                    }
                }

                return true;
            }
        }

        return false;
    }

    public static enum Operation {
        AND,
        OR;

        private Operation() {
        }
    }
}
