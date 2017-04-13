package simpl.parser.ast;

import simpl.interpreter.IntValue;
import simpl.interpreter.RuntimeError;
import simpl.interpreter.State;
import simpl.interpreter.Value;
import simpl.typing.Substitution;
import simpl.typing.Type;
import simpl.typing.TypeEnv;
import simpl.typing.TypeError;
import simpl.typing.TypeResult;

public class Neg extends UnaryExpr {

    public Neg(Expr e) {
        super(e);
    }

    public String toString() {
        return "~" + e;
    }

    @Override
    public TypeResult typecheck(TypeEnv E) throws TypeError {
        TypeResult tr = e.typecheck(E);

        Type t1 = tr.t;
        Substitution substitution = tr.s;
        t1 = substitution.apply(t1);

        substitution = t1.unify(Type.INT).compose(substitution);
        return TypeResult.of(substitution,Type.INT);
    }

    @Override
    public Value eval(State s) throws RuntimeError {
        Value v = e.eval(s);
        if(!(v instanceof IntValue)){
            throw new RuntimeError("must be 2 int values");
        }
        return new IntValue(-((IntValue)v).n);
    }
}
