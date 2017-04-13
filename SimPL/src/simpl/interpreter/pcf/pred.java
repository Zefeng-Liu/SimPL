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
import simpl.typing.Type;
import simpl.typing.TypeEnv;
import simpl.typing.TypeError;
import simpl.typing.TypeResult;

public class pred extends FunValue {

    public pred() {
        super(Env.empty,Symbol.symbol("x"),new Expr(){
            @Override
            public TypeResult typecheck(TypeEnv E) throws TypeError{
                return TypeResult.of(new ArrowType(Type.INT, Type.INT));
            }
            @Override
            public Value eval(State s) throws RuntimeError{
                IntValue iv = (IntValue)(s.E.get(Symbol.symbol("x")));
                return new IntValue(iv.n-1 == 0?0:iv.n-1);
            }
        });
    }
}
