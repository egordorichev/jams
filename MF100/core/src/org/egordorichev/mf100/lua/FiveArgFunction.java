package org.egordorichev.mf100.lua;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.LibFunction;

public abstract class FiveArgFunction extends LibFunction {
	abstract public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3, LuaValue arg4, LuaValue arg5);

	public FiveArgFunction() {

	}

	public final LuaValue call() {
		return call(NIL, NIL, NIL, NIL, NIL);
	}

	public final LuaValue call(LuaValue arg) {
		return call(arg, NIL, NIL, NIL, NIL);
	}

	public LuaValue call(LuaValue arg1, LuaValue arg2) {
		return call(arg1, arg2, NIL, NIL, NIL);
	}

	public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
		return call(arg1, arg2, arg3, NIL, NIL);
	}

	public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3, LuaValue arg5) {
		return call(arg1, arg2, arg3, arg5, NIL);
	}

	public Varargs invoke(Varargs varargs) {
		return call(varargs.arg1(), varargs.arg(2), varargs.arg(3), varargs.arg(4), varargs.arg(5));
	}
}