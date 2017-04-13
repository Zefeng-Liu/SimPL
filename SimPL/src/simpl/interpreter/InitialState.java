package simpl.interpreter;

import static simpl.parser.Symbol.symbol;
import simpl.interpreter.lib.hd;
import simpl.interpreter.lib.tl;
import simpl.interpreter.lib.fst;
import simpl.interpreter.lib.snd;
import simpl.interpreter.pcf.iszero;
import simpl.interpreter.pcf.pred;
import simpl.interpreter.pcf.succ;

public class InitialState extends State {

    public InitialState() {
        super(initialEnv(Env.empty), new Mem(), new Int(0));
    }

    private static Env initialEnv(Env E) {
        Env succ_env = new Env(E, symbol("succ"), new succ());
        Env iszero_env = new Env(succ_env, symbol("iszero"), new iszero());
        Env pred_env = new Env(iszero_env, symbol("pred"), new pred());
        Env fst_env = new Env(pred_env, symbol("fst"), new fst());
        Env snd_env = new Env(fst_env, symbol("snd"), new snd());
        Env hd_env = new Env(snd_env, symbol("hd"), new hd());
        Env tl_env = new Env(hd_env, symbol("tl"), new tl());
        return tl_env;
    }
}
