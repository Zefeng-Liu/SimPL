package simpl.parser.ast;

import simpl.interpreter.Env;
import simpl.interpreter.RecValue;
import simpl.interpreter.RuntimeError;
import simpl.interpreter.State;
import simpl.interpreter.Value;
import simpl.parser.Symbol;
import simpl.typing.Substitution;
import simpl.typing.Type;
import simpl.typing.TypeEnv;
import simpl.typing.TypeError;
import simpl.typing.TypeResult;
import simpl.typing.TypeVar;

public class Rec extends Expr {

    public Symbol x;
    public Expr e;

    public Rec(Symbol x, Expr e) {
        this.x = x;
        this.e = e;
    }

    public String toString() {
        return "(rec " + x + "." + e + ")";
    }

    @Override
    public TypeResult typecheck(TypeEnv E) throws TypeError {
        TypeVar a = new TypeVar(false);
        TypeResult tr = e.typecheck(TypeEnv.of(E,x,a));

        Type t1 = a;
        Type t2 = tr.t;

        Substitution substitution = tr.s;

        t1 = substitution.apply(t1);
        t2 = substitution.apply(t2);

        substitution = t1.unify(t2).compose(substitution);

        Type resultType = substitution.apply(t1);

        return TypeResult.of(substitution,resultType);
    }

    @Override
    public Value eval(State s) throws RuntimeError {
        return e.eval(State.of(new Env(s.E, x, new RecValue(s.E, x, e)), s.M, s.p));
    }
}
