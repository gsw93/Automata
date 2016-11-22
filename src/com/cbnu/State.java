package com.cbnu;

/**
 * Created by Han on 2016-11-21.
 */
public class State implements Cloneable {
	private int stateName;
	private boolean isFinalState;

	private State a;
	private State b;
	private State lambda1;
	private State lambda2;

	private State f;
	private State c;


	/**
	 * MK_State
	 */
	public State(int i, int p) {
		this.stateName = -1;
		this.isFinalState = (p == 1);

		this.a = null;
		this.b = null;
		this.lambda1 = null;
		this.lambda2 = null;

		this.f = null;
		this.c = null;

		if (i < 1) {
			this.c = Global.getInstance().getF_FA();
			Global.getInstance().setF_FA(this);
		}

		if (i >= 1) {
			State temp = null;
			try {
				temp = (State) Global.getInstance().getF_FA().clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			if (temp != null) {
				while (temp.c != null)
					temp = temp.c;
				temp.c = this;
			} else Global.getInstance().setF_FA(this);
		}
	}

	/**
	 * MKb_State
	 */
	public State(char arg) {
		this(1, 0);

		if (arg == 'a') {
			this.a = new State(1, 1);
			this.f = this.a;
		} else {
			this.b = new State(1, 1);
			this.f = this.b;
		}
	}

//	public char intToChar() {
//		if (stateName == -1) return 'X';
//		return (char) (65 + stateName);
//	}

	public int getStateName() {
		return stateName;
	}

	public boolean isFinalState() {
		return isFinalState;
	}

	public State getA() {
		return a;
	}

	public State getB() {
		return b;
	}

	public State getLambda1() {
		return lambda1;
	}

	public State getLambda2() {
		return lambda2;
	}

	public State getF() {
		return f;
	}

	public State getC() {
		return c;
	}

	public void setStateName(int stateName) {
		this.stateName = stateName;
	}

	public void setFinalState(boolean finalState) {
		isFinalState = finalState;
	}

	public void setA(State a) {
		this.a = a;
	}

	public void setB(State b) {
		this.b = b;
	}

	public void setLambda1(State lambda1) {
		this.lambda1 = lambda1;
	}

	public void setLambda2(State lambda2) {
		this.lambda2 = lambda2;
	}

	public void setF(State f) {
		this.f = f;
	}

	public void setC(State c) {
		this.c = c;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return (State) super.clone();
	}
}
