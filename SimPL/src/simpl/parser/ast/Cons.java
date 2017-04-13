package simpl.parser.ast;

import simpl.interpreter.ConsValue;
import simpl.interpreter.RuntimeError;
import simpl.interpreter.State;
import simpl.interpreter.Value;
import simpl.typing.ListType;
import simpl.typing.Substitution;
import simpl.typing.TypeEnv;
import simpl.typing.TypeError;
import simpl.typing.*;

public class Cons extends BinaryExpr {

    public Cons(Expr l, Expr r) {
        super(l, r);
    }

    public String toString() {
        return "(" + l + " :: " + r + ")";
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

      substitution = t2.unify(new ListType(t1)).compose(substitution);

      return TypeResult.of(substitution,substitution.apply(t2));
    }

    @Override
    public Value eval(State s) throws RuntimeError {
        Value l_v = l.eval(s);
        Value r_v = r.eval(s); 
        return new ConsValue(l_v, r_v);
    }
}
