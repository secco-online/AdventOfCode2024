package co.m16mb.secco.advent2024;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

public class Advent07 {

	private static final String filenamePath = "files/file07.txt";

	public static void main(String[] args) throws Exception {

		// reading the input file
		String fileContents = readFileAsString(filenamePath);

		System.out.println("ANSWER1: " + part1(fileContents));
		// 2299996598890
		System.out.println("ANSWER2: " + part2(fileContents));
		// 362646859298554

	}

	private static String part1(String fileContents) {
		long sum = 0;

		for (String line : fileContents.split("\\r?\\n")) {
			String[] lineParts = line.split(": ");
			long expectedResult = Long.parseLong(lineParts[0]);
			// System.out.println(expectedResult);
			List<Long> numberList = Arrays.stream(lineParts[1].split(" ")).map(Long::valueOf)
					.collect(Collectors.toList());
			// System.out.println(numberList);
			if (isResultMatching(expectedResult, numberList, false) > 0)
				sum += expectedResult;

		}

		return sum + "";

	}

	private static String part2(String fileContents) {
		long sum = 0;

		for (String line : fileContents.split("\\r?\\n")) {
			String[] lineParts = line.split(": ");
			long expectedResult = Long.parseLong(lineParts[0]);
			// System.out.println(expectedResult);
			List<Long> numberList = Arrays.stream(lineParts[1].split(" ")).map(Long::valueOf)
					.collect(Collectors.toList());
			// System.out.println(numberList);
			if (isResultMatching(expectedResult, numberList, true) > 0)
				sum += expectedResult;

		}

		return sum + "";

	}

	private static int isResultMatching(long expectedResult, List<Long> numberList, boolean withConcatenation) {

		HashMap<String, Long> pastResults = new HashMap<>();
		Queue<Step> queue = new LinkedList<>();

		queue.add(new Step(expectedResult, numberList.get(0), numberList.get(0) + "", "+", 1, numberList));
		queue.add(new Step(expectedResult, numberList.get(0), numberList.get(0) + "", "*", 1, numberList));
		if (withConcatenation)
			queue.add(new Step(expectedResult, numberList.get(0), numberList.get(0) + "", "||", 1, numberList));

		pastResults.put(numberList.get(0) + "", numberList.get(0));

		int possibilitiesFound = 0;
		while (!queue.isEmpty() && queue.peek().numberList().size() > queue.peek().nextNumberOffset) {
			Step step = queue.poll();

			long thisResult = Long.MIN_VALUE;

			if (step.nextOperation.equals("+")) {
				thisResult = pastResults.get(step.equationTillNow) + step.numberList.get(step.nextNumberOffset);
			} else if (step.nextOperation.equals("*")) {
				thisResult = pastResults.get(step.equationTillNow) * step.numberList.get(step.nextNumberOffset);
			} else if (step.nextOperation.equals("||")) {
				thisResult = Long.parseLong(
						pastResults.get(step.equationTillNow) + "" + step.numberList.get(step.nextNumberOffset));
			}

			if (thisResult == step.expectedResult && step.nextNumberOffset + 1 == step.numberList.size()) {
				// System.out.println("HAVE IT");
				possibilitiesFound++;
			} else if (step.nextNumberOffset + 1 < step.numberList.size()) {

				String equationTillNow = step.equationTillNow + step.nextOperation
						+ step.numberList.get(step.nextNumberOffset);
				pastResults.put(equationTillNow, thisResult);
				queue.add(new Step(expectedResult, thisResult, equationTillNow, "+", step.nextNumberOffset + 1,
						numberList));
				queue.add(new Step(expectedResult, thisResult, equationTillNow, "*", step.nextNumberOffset + 1,
						numberList));
				if (withConcatenation)
					queue.add(new Step(expectedResult, thisResult, equationTillNow, "||", step.nextNumberOffset + 1,
							numberList));
			}

		}

		return possibilitiesFound;
	}

	public static String readFileAsString(String fileName) throws Exception {
		String data = "";
		data = new String(Files.readAllBytes(Paths.get(fileName)));
		System.out.println("Filesize: " + data.length());
		return data;
	}

	private static record Step(long expectedResult, long currentValue, String equationTillNow, String nextOperation,
			int nextNumberOffset, List<Long> numberList) {
	};

}
