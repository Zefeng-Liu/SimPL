package simpl.typing;

import simpl.parser.Symbol;

public class TypeVar extends Type {

    private static int tvcnt = 0;

    private boolean equalityType;
    private Symbol name;

    public TypeVar(boolean equalityType) {
        this.equalityType = equalityType;
        name = Symbol.symbol("tv" + ++tvcnt);
    }

    @Override
    public boolean isEqualityType() {
        return equalityType;
    }

    @Override
    public Substitution unify(Type t) throws TypeCircularityError {
        if (t instanceof TypeVar) {
            if(((TypeVar)t).name == name) {          
                return Substitution.IDENTITY;
            }
            else {
                return Substitution.of(this, t);
            }
                
        }
        else if(t.contains(this)) {
            if (t instanceof ArrowType) {
                TypeVar tmp = new TypeVar(true);
                Type t1, t2;
                if (((ArrowType) t).t1.contains(this)) 
                    t1 = tmp;
                else 
                    t1 = ((ArrowType) t).t1;
                if (((ArrowType) t).t2.contains(this)) 
                    t2 = tmp;
                else 
                    t2 = ((ArrowType) t).t2;
                return Substitution.of(this, new ArrowType (t1, t2));
            }
            throw new TypeCircularityError();
        }
        else {
            return Substitution.of(this, t);
        }
    }

    public String toString() {
        return "" + name;
    }

    @Override
    public boolean contains(TypeVar tv) {
        return tv.name == name;
    }

    @Override
    public Type replace(TypeVar a, Type t) {
        if (a.name == name) {
            return t;
        }
        return this;
    }
}
