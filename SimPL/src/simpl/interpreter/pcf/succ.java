package simpl.interpreter.pcf;

import simpl.interpreter.Env;
import simpl.interpreter.FunValue;
import simpl.interpreter.IntValue;
import simpl.interpreter.RuntimeError;
import simpl.interpreter.State;
import simpl.interpreter.Value;
import simpl.parser.Symbol;
import simpl.parser.ast.Expr;
import simpl.typing.ArrowType;
import simpl.typing.IntType;
import simpl.typing.TypeEnv;
import simpl.typing.TypeError;
import simpl.typing.TypeResult;
import simpl.typing.Type;

public class succ extends FunValue {

    public succ() {
        super(Env.empty,Symbol.symbol("x"),new Expr(){

            @Override
            public TypeResult typecheck(TypeEnv E) throws TypeError {
                return TypeResult.of(E.get(Symbol.symbol("succ")));
            }

            @Override
            public Value eval(State s) throws RuntimeError {
                IntValue iv = (IntValue)(s.E.get(Symbol.symbol("x")));
                return new IntValue(iv.n+1);
            }
            
        });
    }
}
