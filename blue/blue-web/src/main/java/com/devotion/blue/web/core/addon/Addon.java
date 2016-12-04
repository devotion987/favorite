package com.devotion.blue.web.core.addon;

public abstract class Addon {

	private ThreadLocal<Boolean> tl = new ThreadLocal<Boolean>();

	private final Hooks hooks;

	public Addon() {
		hooks = new Hooks(this);
	}

	public Hooks getHooks() {
		return hooks;
	}

	protected void nextInvoke() {
		tl.set(true);
	}

	/**
	 * 子类不要调用此方法
	 */
	public void hookInvokeFinished() {
		tl.remove();
	}

	/**
	 * 子类不要调用此方法
	 */
	public boolean letNextHookInvoke() {
		return tl.get() != null && tl.get() == true;
	}

	public abstract boolean onStart();

	public abstract boolean onStop();

}
