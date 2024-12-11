package co.m16mb.secco.advent2024;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class Advent11 {

	private static final String filenamePath = "files/file11.txt";

	public static void main(String[] args) throws Exception {

		// reading the input file
		String fileContents = readFileAsString(filenamePath);

		System.out.println("ANSWER1: " + blinkAndCalculateStones(fileContents, 25));
		// 218956
		System.out.println("ANSWER2: " + blinkAndCalculateStones(fileContents, 75));
		// 259593838049805
	}

	private static long blinkAndCalculateStones(String fileContents, int blinkNumber) {

		HashMap<Step, Long> stoneOccurrences = new HashMap<>();
		Queue<Step> stones = new LinkedList<>();

		for (String oneChar : fileContents.split(" ")) {
			stones.add(new Step(0, Long.parseLong(oneChar)));
			stoneOccurrences.put(new Step(0, Long.parseLong(oneChar)), 1L);
		}

		while (!stones.isEmpty()) {
			Step step = stones.poll();

			if (step.blinkedNumber == blinkNumber) {
				if (stoneOccurrences.get(step) == null) {
					stoneOccurrences.put(step, 1L);
				} else {
					stoneOccurrences.put(step, stoneOccurrences.get(step) + 1);
				}

			} else {
				int numberOfÍDigits = (step.value + "").length();
				long currentStepOccurrences = stoneOccurrences.get(step);

				if (step.value == 0) {
					Step newStep = new Step(step.blinkedNumber + 1, 1);
					if (stoneOccurrences.get(newStep) == null) {
						stones.add(newStep);
						stoneOccurrences.put(newStep, currentStepOccurrences);
					} else {
						stoneOccurrences.put(newStep, stoneOccurrences.get(newStep) + currentStepOccurrences);
					}

				} else if (numberOfÍDigits % 2 == 0) {
					long part1 = step.value / (long) Math.pow(10, numberOfÍDigits / 2);
					long part2 = step.value % (long) Math.pow(10, numberOfÍDigits / 2);
					Step newStep1 = new Step(step.blinkedNumber + 1, part1);
					Step newStep2 = new Step(step.blinkedNumber + 1, part2);
					if (stoneOccurrences.get(newStep1) == null) {
						stones.add(newStep1);
						stoneOccurrences.put(newStep1, currentStepOccurrences);
					} else {
						stoneOccurrences.put(newStep1, stoneOccurrences.get(newStep1) + currentStepOccurrences);
					}
					if (stoneOccurrences.get(newStep2) == null) {
						stones.add(newStep2);
						stoneOccurrences.put(newStep2, currentStepOccurrences);
					} else {
						stoneOccurrences.put(newStep2, stoneOccurrences.get(newStep2) + currentStepOccurrences);
					}

				} else {
					Step newStep = new Step(step.blinkedNumber + 1, step.value * 2024);
					if (stoneOccurrences.get(newStep) == null) {
						stones.add(newStep);
						stoneOccurrences.put(newStep, currentStepOccurrences);
					} else {
						stoneOccurrences.put(newStep, stoneOccurrences.get(newStep) + currentStepOccurrences);
					}
				}
			}
		}

		long sum = 0;
		for (Map.Entry<Step, Long> entry : stoneOccurrences.entrySet()) {
			Step step = entry.getKey();
			Long val = entry.getValue();
			if (step.blinkedNumber == blinkNumber) {
				sum += val - 1;
			}
		}
		return sum;
	}

	private static record Step(int blinkedNumber, long value) {
	}

	public static String readFileAsString(String fileName) throws Exception {
		String data = "";
		data = new String(Files.readAllBytes(Paths.get(fileName)));
		System.out.println("Filesize: " + data.length());
		return data;
	}

}
