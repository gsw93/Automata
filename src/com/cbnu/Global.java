package com.cbnu;

/**
 * Created by Han on 2016-11-22.
 */
public class Global {
	private static Global ourInstance = new Global();

	public static Global getInstance() {
		return ourInstance;
	}

	private Global() {
	}

	private State F_FA;

	public State getF_FA() {
		return F_FA;
	}

	public void setF_FA(State f_FA) {
		F_FA = f_FA;
	}
}
