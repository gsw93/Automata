package com.cbnu;

import java.util.Stack;

/**
 * Created by Han on 2016-11-21.
 */
public class Algo {

	/**
	 * 각 요소 문법 검사
	 * TODO 전선희 나중에 다시 확인해
	 */
	public boolean checkExpression(String ex) {
		boolean flag = false;
		if (ex.equals("quit") || ex.isEmpty()) flag = true;

		ex += "\0";

		for (int i = 0; i < ex.length() - 1 && !flag; i++) {
			char temp = ex.charAt(i);
			char temp_2 = ex.charAt(i + 1);

			if (temp != 'a' && temp != 'b' && temp != '(' && temp != ')' && temp != '*' && temp != '.' && temp != '+')
				flag = true;
			if (temp == 'a' && (temp_2 == '(' || temp_2 == 'a' || temp_2 == 'b'))
				flag = true;
			if (temp == 'b' && (temp_2 == '(' || temp_2 == 'a' || temp_2 == 'b'))
				flag = true;
			if (temp == '.' && (temp_2 == '.' || temp_2 == '+' || temp_2 == '*' || temp_2 == ')'))
				flag = true;
			if (temp == '+' && (temp_2 == '.' || temp_2 == '+' || temp_2 == '*' || temp_2 == ')'))
				flag = true;
			if (temp == '*' && (temp_2 == '(' || temp_2 == 'a' || temp_2 == 'b'))
				flag = true;
			if (temp == ')' && (temp_2 == '(' || temp_2 == 'a' || temp_2 == 'b'))
				flag = true;
			if (temp == '(' && (temp_2 == '.' || temp_2 == '+' || temp_2 == '*' || temp_2 == ')'))
				flag = true;
		}

		char temp = ex.charAt(ex.length() - 2);
		if (temp == '.' || temp == '+' || temp == '(') flag = true;

		int bracketCount = 0;
		for (int i = 0; i < ex.length() - 1 && !flag; i++) {
			temp = ex.charAt(i);
			if (temp == '(') bracketCount++;
			else if (temp == ')' && bracketCount > 0) bracketCount--;
		}

		flag = flag || (bracketCount == 0);

		return flag;
	}

	public State exToNFA(String ex) {
		State tmp1, tmp2;
		Stack<State> stack = new Stack<>();

		String post = exToPostFix(ex);

		for (int i = 0; i < post.length(); i++) {
			char temp = post.charAt(i);

			/**
			 * TODO 다시한번 확인해 보세요...
			 * */
			if (temp == 'a' || temp == 'b')
				stack.push(new State(temp));
			if (temp == '.') {
				tmp2 = stack.pop();
				tmp1 = stack.pop();
				stack.push(con(tmp1, tmp2));
			}
			if (temp == '+') {
				tmp2 = stack.pop();
				tmp1 = stack.pop();
				stack.push(tmp1);
//				stack.push(alt(tmp1, tmp2));
			}
			if (temp == '*') {
				stack.push(stack.pop());
//				stack.push(kleene(stack.pop()));
			}
		}

		return stack.pop();
	}

	public void outTable(State NFA) {

		State tmp = null;
		try {
			tmp = (State) NFA.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

		int i = 0;
		int t1, t2, t3, t4;

		while (tmp != null) {
			tmp.setStateName(i++);
			tmp = tmp.getC();
		}

		System.out.println("<NFA>");
		System.out.println("|| state ||  a  ||  b  ||  e  ||  e  || output ||\n");

		try {
			tmp = (State) NFA.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

		while (tmp != null) {
			t1 = -1;
			t2 = -1;
			t3 = -1;
			t4 = -1;
			if (tmp.getA() != null) t1 = tmp.getA().getStateName();
			if (tmp.getB() != null) t2 = tmp.getB().getStateName();
			if (tmp.getLambda1() != null) t3 = tmp.getLambda1().getStateName();
			if (tmp.getLambda2() != null) t4 = tmp.getLambda2().getStateName();

			System.out.println("||   " + Num_ch(tmp.getStateName()) + "   ||  " + Num_ch(t1) + "  ||  " + Num_ch(t2) + "  ||  " + Num_ch(t3) + "  ||  " + Num_ch(t4) + "  ||    " + tmp.isFinalState() + "   ||\n");
			tmp = tmp.getC();
		}
		System.out.println("Initial State : " + Num_ch(Global.getInstance().getF_FA().getStateName()));
		System.out.print("Final State : ");

		try {
			tmp = (State) NFA.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

		while (tmp != null) {
			if (tmp.isFinalState()) System.out.println(Num_ch(tmp.getStateName()));
			tmp = tmp.getC();
		}

		System.out.println();
		System.out.println();
	}

	private String exToPostFix(String ex) {
		StringBuilder builder = new StringBuilder();
		Stack<Character> stack = new Stack<>();

		for (int i = 0; i < ex.length(); i++) {
			char temp = ex.charAt(i);

			if (temp == 'a' || temp == 'b')
				builder.append(temp);
			else if (temp == '(')
				stack.push(temp);
			else if (temp == ')') {
				while (stack.peek() != '(')
					builder.append(stack.pop());
			} else if (temp == '.' || temp == '+' || temp == '*') {
				while (!stack.isEmpty() && getPriority(stack.peek()) >= getPriority(temp))
					builder.append(stack.pop());
				stack.push(temp);
			}
		}

		while (!stack.isEmpty())
			builder.append(stack.pop());

		return builder.toString();
	}

	private int getPriority(char arg) {
		if (arg == '(') return 0;
		if (arg == '+') return 1;
		if (arg == '*') return 2;
		else return 3;
	}

	private char Num_ch(int arg) {
		if (arg == -1) return 'X';
		return (char) (65 + arg);
	}

	private State con(State tmp1, State tmp2) {
		State temp = tmp1.getF();
		temp.setA(tmp2.getA());
		temp.setB(tmp2.getB());
		temp.setLambda1(tmp2.getLambda1());
		temp.setLambda2(tmp2.getLambda2());
		temp.setFinalState(false);
		temp.setF(tmp2.getF());
		tmp2.setF(null);

		return tmp1;
	}

//	private State alt(State s1, State s2) {
//		State temp1 = new State(0, 0);
//		State temp2 = new State(1, 1);
//
//		// TODO 아래 끝까지 내일 부터~
//	}
//
//	private State kleene(State tmp1, State tmp2) {
//
//	}
}
