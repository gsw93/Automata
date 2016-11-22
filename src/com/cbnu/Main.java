package com.cbnu;

import java.util.Scanner;

/**
 * Created by Han on 2016-11-21.
 */
public class Main {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		Algo algo = new Algo();

		while (true) {
			Global.getInstance().setF_FA(new State(0, 0));
			String expression = scanner.next();

			if (!algo.checkExpression(expression)) {
				System.out.println("정규 표현 ERROR");
				continue;
			}

			State NFA = algo.exToNFA(expression);
			algo.outTable(NFA);
		}
	}
}
