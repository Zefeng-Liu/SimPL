package simpl.interpreter;

public class RefValue extends Value {

    public final int p;
    public Value v;

    public RefValue(int p, Value v) {
        this.p = p;
        this.v = v;
    }

    public String toString() {
        return "ref@" + v;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof RefValue) {
            return p == ((RefValue) other).p;
        }
        return false;
    }
}
