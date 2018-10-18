package plpFields;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import plpEtc.FieldType;
import plpEtc.Range;

public class Variable {
    private String name;
    List<Range> possibleRanges;
    List<String> possibleValues;
    private FieldType type;
    private String input;

    public Variable(String name, FieldType type, String input) {
        this.name = name;
        this.type = type;
        this.input = input;
        this.possibleRanges = new LinkedList();
        this.possibleValues = new LinkedList();
    }

    public void addRange(String minValue, boolean minInclusive, String maxValue, boolean maxInclusive) {
        this.possibleRanges.add(new Range(minValue, minInclusive, maxValue, maxInclusive));
    }

    public void addRange(Range range) {
        this.possibleRanges.add(range);
    }

    public void addPossibleValue(String value) {
        this.possibleValues.add(value);
    }

    public String getName() {
        return this.name;
    }

    public String getInput() {
        return this.input;
    }

    public List<Range> getPossibleRanges() {
        return this.possibleRanges;
    }

    public List<String> getPossibleValues() {
        return this.possibleValues;
    }

    public FieldType getType() {
        return this.type;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(this.name).append(" - ").append(this.type.toString());
        if (this.possibleRanges.size() > 0 || this.possibleValues.size() > 0) {
            sb.append(", in ");
        }

        Iterator var2;
        if (this.possibleRanges.size() > 0) {
            var2 = this.possibleRanges.iterator();

            while(var2.hasNext()) {
                Range range = (Range)var2.next();
                sb.append(range.toString()).append(" ");
            }

            sb.deleteCharAt(sb.length() - 1);
        }

        if (this.possibleValues.size() > 0) {
            var2 = this.possibleValues.iterator();

            while(var2.hasNext()) {
                String value = (String)var2.next();
                sb.append("[").append(value).append(", ");
            }

            sb.delete(sb.length() - 2, sb.length());
        }

        sb.append("]");
        return sb.toString();
    }
}
