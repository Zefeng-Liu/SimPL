package simpl.parser.ast;

import simpl.interpreter.Env;
import simpl.interpreter.FunValue;
import simpl.interpreter.RuntimeError;
import simpl.interpreter.State;
import simpl.interpreter.Value;
import simpl.parser.Symbol;
import simpl.typing.ArrowType;
import simpl.typing.Substitution;
import simpl.typing.Type;
import simpl.typing.TypeEnv;
import simpl.typing.TypeError;
import simpl.typing.TypeResult;
import simpl.typing.TypeVar;

public class App extends BinaryExpr {

    public App(Expr l, Expr r) {
        super(l, r);
    }

    public String toString() {
        return "(" + l + " " + r + ")";
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

        Type resultType;

        if(t1 instanceof ArrowType) {
            substitution = ((ArrowType) t1).t1.unify(t2).compose(substitution);//t1=t2->a
            t1 = substitution.apply(t1);
            resultType = ((ArrowType) t1).t2;
        }else if(t1 instanceof TypeVar){
            TypeVar tv = new TypeVar(true);//new type a
            substitution = t1.unify(new ArrowType(t2,tv)).compose(substitution);
            resultType = substitution.apply(tv);
        }else{
            throw new TypeError("no function found");
        }

        return TypeResult.of(substitution,resultType);//return a
    }

    @Override
    public Value eval(State s) throws RuntimeError {
        // according to the rule E-App,
        // first evaluate the function,
        // then evaluate the parameter, using the original state.
        // then evaluate the expression in fun, using a new Env.

        Value v = l.eval(s);
        if(!(v instanceof FunValue))throw new RuntimeError("not a function");
        FunValue fun = (FunValue)v;

        Value param = r.eval(s);
        State newState = State.of(new Env(fun.E,fun.x,param),s.M,s.p);
        return fun.e.eval(newState);
    }
}
