package co.m16mb.secco.advent2024;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class Advent05 {

	private static final String filenamePath = "files/file05.txt";

	public static void main(String[] args) throws Exception {

		// reading the input file
		String fileContents = readFileAsString(filenamePath);

		String[] groups = fileContents.split("\\r?\\n\\r?\\n");

		String[] pageOrderingRules = groups[0].split("\\r?\\n");

		HashSet<String> pageOrderingRulesSet = new HashSet<>();
		for (String rule : pageOrderingRules) {
			pageOrderingRulesSet.add(rule);
		}

		String[] updates = groups[1].split("\\r?\\n");

		List<List<Integer>> updateList = new ArrayList<>();

		for (int i = 0; i < updates.length; i++) {
			List<Integer> collect = Arrays.stream(updates[i].split(",")).map(String::trim).filter(s -> !s.isEmpty())
					.map(Integer::valueOf).collect(Collectors.toList());
			updateList.add(collect);

		}

		// take input until a `#` is typed on a new line.
		System.out.println("ANSWER1: " + part1(pageOrderingRulesSet, updateList));
		// 4924
		System.out.println("ANSWER2: " + part2(pageOrderingRulesSet, updateList));
		// 6085

	}

	private static String part1(HashSet<String> pageOrderingRulesSet, List<List<Integer>> updateList) {

		long sumOfMiddleNumbersForCorrectUpdates = 0;

		for (List<Integer> updateLine : updateList) {

			boolean ruleCorrect = true;
			for (int i = 0; i < updateLine.size(); i++) {

				for (int j = i + 1; j < updateLine.size(); j++) {
					if (!pageOrderingRulesSet.contains(updateLine.get(i) + "|" + updateLine.get(j)))
						ruleCorrect = false;
				}
			}

			if (ruleCorrect) {
				int middleOfTheList = (updateLine.size() - 1) / 2;
				sumOfMiddleNumbersForCorrectUpdates += updateLine.get(middleOfTheList);
			}

		}
		return sumOfMiddleNumbersForCorrectUpdates + "";

	}

	private static String part2(HashSet<String> pageOrderingRulesSet, List<List<Integer>> updateList) {

		long sumOfMiddleNumbersForCorrectUpdates = 0;

		for (List<Integer> updateLine : updateList) {

			boolean ruleCorrect = true;
			for (int i = 0; i < updateLine.size(); i++) {
				for (int j = i + 1; j < updateLine.size(); j++) {
					if (!pageOrderingRulesSet.contains(updateLine.get(i) + "|" + updateLine.get(j))) {
						ruleCorrect = false;
						Collections.swap(updateLine, i, j);
					}
				}
			}
			if (!ruleCorrect) {
				int middleOfTheList = (updateLine.size() - 1) / 2;
				sumOfMiddleNumbersForCorrectUpdates += updateLine.get(middleOfTheList);
			}
		}
		return sumOfMiddleNumbersForCorrectUpdates + "";

	}

	public static String readFileAsString(String fileName) throws Exception {
		String data = "";
		data = new String(Files.readAllBytes(Paths.get(fileName)));
		System.out.println("Filesize: " + data.length());
		return data;
	}

}
