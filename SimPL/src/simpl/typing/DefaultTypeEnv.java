package simpl.typing;

import simpl.parser.Symbol;

public class DefaultTypeEnv extends TypeEnv {

    private TypeEnv E;

    public DefaultTypeEnv() {
        TypeVar tv1 = new TypeVar(true), tv2 = new TypeVar(true), tv3 = new TypeVar(true), tv4 = new TypeVar(true), 
            tv5 = new TypeVar(true), tv6 = new TypeVar(true);
        E = TypeEnv.empty;
        E = TypeEnv.of(E, Symbol.symbol("iszero"), new ArrowType(Type.INT, Type.BOOL));
        E = TypeEnv.of(E, Symbol.symbol("pred"), new ArrowType(Type.INT, Type.INT));
        E = TypeEnv.of(E, Symbol.symbol("succ"), new ArrowType(Type.INT, Type.INT));
        E = TypeEnv.of(E, Symbol.symbol("fst"), new ArrowType(new PairType(tv1,tv2),tv1));
        E = TypeEnv.of(E, Symbol.symbol("snd"), new ArrowType(new PairType(tv3,tv4),tv4));
        E = TypeEnv.of(E, Symbol.symbol("hd"), new ArrowType(new ListType(tv5),tv5));
        E = TypeEnv.of(E, Symbol.symbol("tl"), new ArrowType(new ListType(tv6),new ListType(tv6)));
    }

    @Override
    public Type get(Symbol x) {
        return E.get(x);
    }
}
