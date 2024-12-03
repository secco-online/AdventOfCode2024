package co.m16mb.secco.advent2024;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class Advent02 {

	private static final String filenamePath = "files/file02.txt";

	public static void main(String[] args) throws Exception {

		// reading the input file
		String fileContents = readFileAsString(filenamePath);

		System.out.println("ANSWER1: " + part1(fileContents));
		// 686
		System.out.println("ANSWER2: " + part2(fileContents));
		// 717

	}

	private static int part1(String fileContents) {

		int safeCount = 0;
		for (String line : fileContents.split("\\r?\\n")) {

			String[] levels = line.split(" ");

			ArrayList<String> levelList = new ArrayList<>(Arrays.asList(levels));

			if (isSafe(levelList))
				safeCount++;
		}
		return safeCount;

	}

	//
	private static boolean isSafe(ArrayList<String> levelList) {
		int DIFF_MIN = 1;
		int DIFF_MAX = 3;

		boolean increasing = Integer.parseInt(levelList.get(1)) > Integer.parseInt(levelList.get(0));

		boolean isSafe = true;
		for (int i = 1; i < levelList.size() && isSafe; i++) {

			if (increasing) {
				if (Integer.parseInt(levelList.get(i)) - Integer.parseInt(levelList.get(i - 1)) >= DIFF_MIN
						&& Integer.parseInt(levelList.get(i)) - Integer.parseInt(levelList.get(i - 1)) <= DIFF_MAX)
					isSafe = true;
				else
					isSafe = false;
			} else {
				if (Integer.parseInt(levelList.get(i - 1)) - Integer.parseInt(levelList.get(i)) >= DIFF_MIN
						&& Integer.parseInt(levelList.get(i - 1)) - Integer.parseInt(levelList.get(i)) <= DIFF_MAX)
					isSafe = true;
				else
					isSafe = false;
			}
		}
		return isSafe;
	}

	private static int part2(String fileContents) {

		int safeCount = 0;
		for (String line : fileContents.split("\\r?\\n")) {

			String[] levels = line.split(" ");

			ArrayList<String> levelList = new ArrayList<>(Arrays.asList(levels));

			if (isSafe(levelList)) {
				safeCount++;
			} else {
				boolean isSafeAbónferRemove = false;

				for (int i = 0; i < levelList.size() && !isSafeAbónferRemove; i++) {
					ArrayList<String> newList = new ArrayList<>(levelList);
					newList.remove(i);
					isSafeAbónferRemove = isSafe(newList);
					if (isSafeAbónferRemove) {
						safeCount++;
					}
				}
			}
		}
		return safeCount;
	}

	public static String readFileAsString(String fileName) throws Exception {
		String data = "";
		data = new String(Files.readAllBytes(Paths.get(fileName)));
		System.out.println("Filesize: " + data.length());
		return data;
	}
}
