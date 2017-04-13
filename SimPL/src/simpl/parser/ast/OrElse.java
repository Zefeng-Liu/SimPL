package simpl.parser.ast;

import simpl.interpreter.BoolValue;
import simpl.interpreter.RuntimeError;
import simpl.interpreter.State;
import simpl.interpreter.Value;
import simpl.typing.Substitution;
import simpl.typing.Type;
import simpl.typing.TypeEnv;
import simpl.typing.TypeError;
import simpl.typing.TypeResult;

public class OrElse extends BinaryExpr {

    public OrElse(Expr l, Expr r) {
        super(l, r);
    }

    public String toString() {
        return "(" + l + " orelse " + r + ")";
    }

    @Override
    public TypeResult typecheck(TypeEnv E) throws TypeError {
        TypeResult tr1 = l.typecheck(E);
        TypeResult tr2 = r.typecheck(E);

        Type t1 = tr1.t;
        Type t2 = tr2.t;

        Substitution substitution = tr2.s.compose(tr1.s);

        t1 = substitution.apply(t1);
        t2 = substitution.apply(t2);

        substitution = t2.unify(Type.BOOL)
                .compose(t1.unify(Type.BOOL))
                .compose(substitution);

        return TypeResult.of(substitution,Type.BOOL);
    }

    @Override
    public Value eval(State s) throws RuntimeError {
        Value l_v = l.eval(s);
        if(!(l_v instanceof BoolValue))throw new RuntimeError("not a bool value");
        if (((BoolValue) l_v).b) return new BoolValue(true);
        Value r_v = r.eval(s);
        if(!(r_v instanceof BoolValue))throw new RuntimeError("not a bool value");
        if (((BoolValue) r_v).b) return new BoolValue(true);
        return new BoolValue(false);
    }
}
