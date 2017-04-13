package simpl.parser.ast;

import simpl.interpreter.RefValue;
import simpl.interpreter.RuntimeError;
import simpl.interpreter.State;
import simpl.interpreter.Value;
import simpl.typing.RefType;
import simpl.typing.TypeEnv;
import simpl.typing.TypeError;
import simpl.typing.*;

public class Ref extends UnaryExpr {

    public Ref(Expr e) {
        super(e);
    }

    public String toString() {
        return "(ref " + e + ")";
    }

    @Override
    public TypeResult typecheck(TypeEnv E) throws TypeError {
      TypeResult tr = e.typecheck(E);

      Type t = tr.t;
      Substitution substitution = tr.s;

      t = substitution.apply(t);

      TypeResult result = TypeResult.of(substitution,new RefType(t));

      return result;
    }

    @Override
    public Value eval(State s) throws RuntimeError {
        int oldPointer = s.p.get();
        s.p.set(oldPointer+1);
        Value v = e.eval(s);
        v.rc += 1;
        s.M.put(oldPointer, v);
//        System.out.println("--filling memory["+oldPointer+"]");
//        System.out.println("memory["+oldPointer+"] = "+v+"\t memory["+oldPointer+"].rc = "+v.rc);
        return new RefValue(oldPointer, v);
    }
}
