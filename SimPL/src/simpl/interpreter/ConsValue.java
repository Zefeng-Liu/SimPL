package simpl.interpreter;

public class ConsValue extends Value {

    public final Value v1, v2;

    public ConsValue(Value v1, Value v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    public String toString() {
        return "list@"+this.getLen();
    }
    
    public int getLen() {
        if (v2 instanceof NilValue) return 1;
        else if (v2 instanceof ConsValue) {
            return 1+((ConsValue)v2).getLen();
        }
        else return 0;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof ConsValue) {
            return v1.equals(((ConsValue) other).v1) && v2.equals(((ConsValue) other).v2);
        }
        return false;
    }
}
