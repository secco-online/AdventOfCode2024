package co.m16mb.secco.advent2024;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Advent13 {

	private static final String filenamePath = "files/file13.txt";

	public static void main(String[] args) throws Exception {

		// reading the input file
		String fileContents = readFileAsString(filenamePath);

		ArrayList<Game> gameList = new ArrayList<>();

		Pattern pattern = Pattern.compile("X.?([0-9]*), Y.?([0-9]*)");

		for (String game : fileContents.split("\\r?\\n\\r?\\n")) {

			int buttonAX = 0;
			int buttonAY = 0;
			int buttonAtokens = 3;
			int buttonBX = 0;
			int buttonBY = 0;
			int buttonBtokens = 1;
			int prizeX = 0;
			int prizeY = 0;

			int rowNumber = 1;
			for (String row : game.split("\\r?\\n")) {
				Matcher matcher = pattern.matcher(row);
				if (matcher.find()) {
					if (rowNumber == 1) {
						buttonAX = Integer.parseInt(matcher.group(1));
						buttonAY = Integer.parseInt(matcher.group(2));
					} else if (rowNumber == 2) {
						buttonBX = Integer.parseInt(matcher.group(1));
						buttonBY = Integer.parseInt(matcher.group(2));
					} else if (rowNumber == 3) {
						prizeX = Integer.parseInt(matcher.group(1));
						prizeY = Integer.parseInt(matcher.group(2));
					}
				}
				rowNumber++;
			}
			Game newGame = new Game(buttonAX, buttonAY, buttonAtokens, buttonBX, buttonBY, buttonBtokens, prizeX,
					prizeY);
//			System.out.println(newGame);
			gameList.add(newGame);
		}

		System.out.println("ANSWER1: " + part1(gameList));
		// 35255
		System.out.println("ANSWER2: " + part2(gameList, 0));
		// TODO 13 p2: 34371 is too low, there must be some rounding error

	}

	private static String part1(ArrayList<Game> gameList) {

		HashSet<Step> winningSteps = new HashSet<>();

		for (Game game : gameList) {

			Queue<Step> queue = new LinkedList<>();
			queue.add(new Step(0, 0, 0, 0, 'A'));
			queue.add(new Step(0, 0, 0, 0, 'B'));
			HashSet<Step> combinationSteps = new HashSet<>();
			combinationSteps.add(new Step(0, 0, 0, 0, 'A'));
			combinationSteps.add(new Step(0, 0, 0, 0, 'B'));

			while (!queue.isEmpty()) {
				Step step = queue.poll();

				int xToUse = 0;
				int yToUse = 0;
				int increaseA = 0;
				int increaseB = 0;
				long prizeX = game.prizeX;
				long prizeY = game.prizeY;

				if (step.buttonToPress == 'A') {
					xToUse = game.buttonAX;
					yToUse = game.buttonAY;
					increaseA = 1;
				} else if (step.buttonToPress == 'B') {
					xToUse = game.buttonBX;
					yToUse = game.buttonBY;
					increaseB = 1;
				}

				if (step.currentX + xToUse < prizeX && step.currentY + yToUse < prizeY) {
					// not arrived yet
					Step stepNewA = new Step(step.currentX + xToUse, step.currentY + yToUse,
							step.numberPressedA + increaseA, step.numberPressedB + increaseB, 'A');
					if (!combinationSteps.contains(stepNewA)) {
						// only add if not yet such a combination
						combinationSteps.add(stepNewA);
						queue.add(stepNewA);
					} else {
						// already such a combination, no need
						// System.out.println("Ignoring " + stepNewA);
					}

					Step stepNewB = new Step(step.currentX + xToUse, step.currentY + yToUse,
							step.numberPressedA + increaseA, step.numberPressedB + increaseB, 'B');
					if (!combinationSteps.contains(stepNewB)) {
						// only add if not yet such a combination
						combinationSteps.add(stepNewB);
						queue.add(stepNewB);
					} else {
						// already such a combination, no need
						// System.out.println("Ignoring " + stepNewB);
					}

				} else if (step.currentX + xToUse == prizeX && step.currentY + yToUse == prizeY) {
					// not arrived yet
					Step stepWinning = new Step(step.currentX + xToUse, step.currentY + yToUse,
							step.numberPressedA + increaseA, step.numberPressedB + increaseB, 'A');
					winningSteps.add(stepWinning);
					// System.out.println("Winning " + stepWinning);
				}

			}
		}
		long sum = 0;
		for (Step step : winningSteps) {
			long cost = step.numberPressedA * 3 + step.numberPressedB;
			if (step.numberPressedA > 100 || step.numberPressedB > 100) {
				System.out.println("OVER");
			}
			sum += cost;
		}
		return sum + "";

	}

	private static String part2(ArrayList<Game> gameList, long correction) {
		HashSet<WinCombination> winningSteps = new HashSet<>();

		for (Game game : gameList) {

			long prizeX = (correction + game.prizeX);
			long prizeY = (correction + game.prizeY);

			
			long upperB =(prizeX * game.buttonAY - game.buttonAX * prizeY);
			long lowerB =(game.buttonBX * game.buttonAY - game.buttonBY * game.buttonAX);
			
			double b = (double)upperB/ lowerB;
			//System.out.println("b: " + b) ;
			double a = (prizeY - game.buttonBY * b) / game.buttonAY;
			//System.out.println("a: " + a) ;

			long longA = (long) a;
			long longB = (long) b;

			// matching solution if an integer number pressed, no fractions
			if (b == longB && a == longA) {
				
				winningSteps.add(new WinCombination(longA, longB));
			}
		}
		long sum = 0;
		for (WinCombination win : winningSteps) {
			long cost = win.numberPressedA * 3 + win.numberPressedB;
			sum += cost;
		}
		return sum + "";
	}

	private static record Game(int buttonAX, int buttonAY, int buttonAtokens, int buttonBX, int buttonBY,
			int buttonBtokens, long prizeX, long prizeY) {
	};

	private static record Step(long currentX, long currentY, long numberPressedA, long numberPressedB,
			char buttonToPress) {
	};

	private static record WinCombination(long numberPressedA, long numberPressedB) {
	};

	public static String readFileAsString(String fileName) throws Exception {
		String data = "";
		data = new String(Files.readAllBytes(Paths.get(fileName)));
		System.out.println("Filesize: " + data.length());
		return data;
	}

}
