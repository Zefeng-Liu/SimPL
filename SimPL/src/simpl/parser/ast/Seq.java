package simpl.parser.ast;

import simpl.interpreter.RuntimeError;
import simpl.interpreter.State;
import simpl.interpreter.Value;
import simpl.typing.Substitution;
import simpl.typing.TypeEnv;
import simpl.typing.TypeError;
import simpl.typing.TypeResult;
import simpl.typing.*;

public class Seq extends BinaryExpr {

    public Seq(Expr l, Expr r) {
        super(l, r);
    }

    public String toString() {
        return "(" + l + " ; " + r + ")";
    }

    @Override
    public TypeResult typecheck(TypeEnv E) throws TypeError {
        TypeResult tr1 = l.typecheck(E);
        TypeResult tr2 = r.typecheck(E);

        Type t2 = tr2.t;

        Substitution substitution = tr2.s.compose(tr1.s);

        t2 = substitution.apply(t2);

        return TypeResult.of(substitution, t2);
    }

    @Override
    public Value eval(State s) throws RuntimeError {
        l.eval(s);
        return r.eval(s);
    }
}
