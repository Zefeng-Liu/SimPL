package simpl.parser.ast;

import simpl.interpreter.RefValue;
import simpl.interpreter.RuntimeError;
import simpl.interpreter.State;
import simpl.interpreter.Value;
import simpl.typing.RefType;
import simpl.typing.Substitution;
import simpl.typing.Type;
import simpl.typing.TypeEnv;
import simpl.typing.TypeError;
import simpl.typing.TypeResult;
import simpl.typing.TypeVar;

public class Assign extends BinaryExpr {

    public Assign(Expr l, Expr r) {
        super(l, r);
    }

    public String toString() {
        return l + " := " + r;
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

        Type tv = new TypeVar(false);

        substitution = t1.unify(new RefType(tv)).compose(substitution);

        tv = substitution.apply(tv);

        substitution = t2.unify(tv).compose(substitution);

        TypeResult result = TypeResult.of(substitution,Type.UNIT);

        return result;
    }

    @Override
    public Value eval(State s) throws RuntimeError {
        RefValue l_v = (RefValue)l.eval(s);
        Value r_v = r.eval(s);
        l_v.v = r_v;
        s.M.put(l_v.p, r_v);
        return Value.UNIT;
    }
}
