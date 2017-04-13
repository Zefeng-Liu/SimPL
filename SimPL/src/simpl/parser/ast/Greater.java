package simpl.parser.ast;

import simpl.interpreter.BoolValue;
import simpl.interpreter.IntValue;
import simpl.interpreter.RuntimeError;
import simpl.interpreter.State;
import simpl.interpreter.Value;

public class Greater extends RelExpr {

    public Greater(Expr l, Expr r) {
        super(l, r);
    }

    public String toString() {
        return "(" + l + " > " + r + ")";
    }

    @Override
    public Value eval(State s) throws RuntimeError {
        Value l_v = l.eval(s);
        Value r_v = r.eval(s);
        if(!(l_v instanceof IntValue&& r_v instanceof IntValue)){
            throw new RuntimeError("must be 2 int values");
        }
        return new BoolValue(((IntValue) l_v).n > ((IntValue) r_v).n);
    }
}
