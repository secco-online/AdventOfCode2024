package co.m16mb.secco.advent2024;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;

public class Advent03 {

	private static final String filenamePath = "files/file03.txt";

	public static void main(String[] args) throws Exception {

		// reading the input file
		String fileContents = readFileAsString(filenamePath);

		System.out.println("ANSWER1: " + part1(fileContents));
		// 173419328
		System.out.println("ANSWER2: " + part2(fileContents));
		// 90669332

	}

	private static String part1(String fileContents) {
		long sum = 0;

		Pattern pattern = Pattern.compile("mul\\(([0-9]+),([0-9]+)\\)");
		// in case you would like to ignore case sensitivity,
		// you could use this statement:
		// Pattern pattern = Pattern.compile("\\s+", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(fileContents);
		// check all occurrence
		while (matcher.find()) {
			// System.out.print("Start index: " + matcher.start());
			// System.out.print(" End index: " + matcher.end() + " ");
			// System.out.println(matcher.group());

			long result = Long.parseLong(matcher.group(1)) * Long.parseLong(matcher.group(2));
			sum += result;
		}

		return sum + "";

	}

	private static String part2(String fileContents) {
		long sum = 0;

		Pattern enablePatern = Pattern.compile("do(n't)*\\(\\)");
		Matcher enableMatcher = enablePatern.matcher(fileContents);

		boolean enabled = true;
		int lastIndex = 0;
		HashMap<Integer, Boolean> enableMap = new HashMap<>();

		while (enableMatcher.find()) {
			// System.out.print("Start index: " + enableMatcher.start());
			// System.out.print(" End index: " + enableMatcher.end() + " ");
			// System.out.println(enableMatcher.group());

			for (int i = lastIndex; i < enableMatcher.start(); i++) {
				enableMap.put(i, enabled);
			}

			lastIndex = enableMatcher.start();
			enabled = enableMatcher.group().equals("do()");
		}
		for (int i = lastIndex; i < fileContents.length(); i++) {
			enableMap.put(i, enabled);
		}

		Pattern pattern = Pattern.compile("mul\\(([0-9]+),([0-9]+)\\)");
		// in case you would like to ignore case sensitivity,
		// you could use this statement:
		// Pattern pattern = Pattern.compile("\\s+", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(fileContents);
		// check all occurrence
		while (matcher.find()) {
			// System.out.print("Start index: " + matcher.start());
			// System.out.print(" End index: " + matcher.end() + " ");
			// System.out.println(matcher.group());

			long result = Long.parseLong(matcher.group(1)) * Long.parseLong(matcher.group(2));

			if (enableMap.get(matcher.start()))
				sum += result;
		}

		return sum + "";

	}

	public static String readFileAsString(String fileName) throws Exception {
		String data = "";
		data = new String(Files.readAllBytes(Paths.get(fileName)));
		System.out.println("Filesize: " + data.length());
		return data;
	}

}
