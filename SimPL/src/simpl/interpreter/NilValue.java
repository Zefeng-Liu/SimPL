package simpl.interpreter;

class NilValue extends Value {

    protected NilValue() {
    }

    public String toString() {
        return "nil";
    }
    
    public int getLen() {
        return 0;
    }
    
    @Override
    public boolean equals(Object other) {
        if (other instanceof NilValue) {
            return true;
        }
        return false;
    }
}
