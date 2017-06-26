package org.egordorichev.mf100.lua;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.LibFunction;

public abstract class FourArgFunction extends LibFunction {
	abstract public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3, LuaValue arg4);

	public FourArgFunction() {

	}

	public final LuaValue call() {
		return call(NIL, NIL, NIL, NIL);
	}

	public final LuaValue call(LuaValue arg) {
		return call(arg, NIL, NIL, NIL);
	}

	public LuaValue call(LuaValue arg1, LuaValue arg2) {
		return call(arg1, arg2, NIL, NIL);
	}

	public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
		return call(arg1, arg2, arg3, NIL);
	}

	public Varargs invoke(Varargs varargs) {
		return call(varargs.arg1(), varargs.arg(2), varargs.arg(3), varargs.arg(4));
	}
}