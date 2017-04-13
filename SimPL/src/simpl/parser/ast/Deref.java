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

public class Deref extends UnaryExpr {

    public Deref(Expr e) {
        super(e);
    }

    public String toString() {
        return "!" + e;
    }

    @Override
    public TypeResult typecheck(TypeEnv E) throws TypeError {
      TypeResult tr = e.typecheck(E);

      Type t = tr.t;
      Substitution substitution = tr.s;

      t = substitution.apply(t);

      TypeResult result;

      if(t instanceof RefType){
          result = TypeResult.of(substitution,((RefType)t).t);
      }else if(t instanceof TypeVar){
          Type tv = new TypeVar(false);
          substitution = t.unify(new RefType(tv)).compose(substitution);
          tv = substitution.apply(tv);
          result = TypeResult.of(substitution,tv);
      }else{
          throw new TypeError("no ref type found");
      }

      return result;
    }

    @Override
    public Value eval(State s) throws RuntimeError {
        Value v = e.eval(s);
        if(!(v instanceof RefValue))throw new RuntimeError("not a reference");
        return s.M.get(((RefValue)v).p);
    }
}
