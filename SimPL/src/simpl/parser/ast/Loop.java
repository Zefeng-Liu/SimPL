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

public class Loop extends Expr {

    public Expr e1, e2;

    public Loop(Expr e1, Expr e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    public String toString() {
        return "(while " + e1 + " do " + e2 + ")";
    }

    @Override
    public TypeResult typecheck(TypeEnv E) throws TypeError {
        TypeResult tr1 = e1.typecheck(E);
        TypeResult tr2 = e2.typecheck(E);

        Type t1 = tr1.t;

        Substitution substitution = tr2.s.compose(tr1.s);

        t1 = substitution.apply(t1);

        substitution = t1.unify(Type.BOOL).compose(substitution);

        TypeResult result = TypeResult.of(substitution,Type.UNIT);

        return result;
    }

    @Override
    public Value eval(State s) throws RuntimeError {
        Value v1 = e1.eval(s);
        if(!(v1 instanceof BoolValue))throw new RuntimeError("not a bool value");
        if(((BoolValue)v1).b){
            return new Seq(e2,this).eval(s);
        }else{
            return Value.UNIT;
        }
    }
}
