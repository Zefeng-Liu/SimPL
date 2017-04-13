package simpl.parser.ast;

import simpl.typing.ListType;
import simpl.typing.PairType;
import simpl.typing.RefType;
import simpl.typing.Substitution;
import simpl.typing.Type;
import simpl.typing.TypeEnv;
import simpl.typing.TypeError;
import simpl.typing.TypeResult;
import simpl.typing.TypeVar;

public abstract class EqExpr extends BinaryExpr {

    public EqExpr(Expr l, Expr r) {
        super(l, r);
    }

    @Override
    public TypeResult typecheck(TypeEnv E) throws TypeError {
        TypeResult tr1 = l.typecheck(E);
        TypeResult tr2 = r.typecheck(E);

        Substitution substitution = tr2.s.compose(tr1.s);

        Type t1 = substitution.apply(tr1.t);
        Type t2 = substitution.apply(tr2.t);

        TypeResult result;

        if(t1 instanceof ListType||t2 instanceof ListType){
            Type tv = new TypeVar(false);
            substitution = t1.unify(new ListType(tv)).compose(substitution);
            tv = substitution.apply(tv);
            substitution = t2.unify(new ListType(tv)).compose(substitution);
        }else if(t1 instanceof PairType||t2 instanceof PairType){
            Type tv1 = new TypeVar(false);
            Type tv2 = new TypeVar(false);
            substitution = t1.unify(new PairType(tv1,tv2)).compose(substitution);
            tv1 = substitution.apply(tv1);
            tv2 = substitution.apply(tv2);
            substitution = t2.unify(new PairType(tv1,tv2)).compose(substitution);
        }else if(t1 instanceof RefType||t2 instanceof RefType){
            Type tv = new TypeVar(false);
            substitution = t1.unify(new RefType(tv)).compose(substitution);
            tv = substitution.apply(tv);
            substitution = t2.unify(new RefType(tv)).compose(substitution);
        }
        else if(t1 instanceof TypeVar&&t2 instanceof TypeVar){
            Type tv = new TypeVar(false);
            substitution = t1.unify(tv).compose(substitution);
            tv = substitution.apply(tv);
            substitution = t2.unify(tv).compose(substitution);
        } else if(t1.equals(Type.INT)||t2.equals(Type.INT)){
            substitution = t2.unify(Type.INT).compose(t1.unify(Type.INT)).compose(substitution);
        }else if(t1.equals(Type.BOOL)||t2.equals(Type.BOOL)){
            substitution = t2.unify(Type.BOOL).compose(t1.unify(Type.BOOL)).compose(substitution);
        }
        result = TypeResult.of(substitution,Type.BOOL);

        return result;
    }
}
