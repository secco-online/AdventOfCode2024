package co.m16mb.secco.advent2024;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

public class Advent01 {

	private static final String filenamePath = "files/file01.txt";

	public static void main(String[] args) throws Exception {

		// reading the input file
		String fileContents = readFileAsString(filenamePath);

		String[] lines = fileContents.split("\\r?\\n");

		ArrayList<Integer> list1 = new ArrayList<>();
		ArrayList<Integer> list2 = new ArrayList<>();

		for (String line : lines) {
			String[] lineItem = line.split("   ");
			list1.add(Integer.parseInt(lineItem[0]));
			list2.add(Integer.parseInt(lineItem[1]));

		}

		// sorting
		Collections.sort(list1);
		Collections.sort(list2);

		System.out.println("ANSWER1: " + part1(list1, list2));
		// Part1 msoc: 1341714

		System.out.println("ANSWER2: " + part2(list1, list2));
		// Part1 msoc: 27384707

	}

	private static String part1(ArrayList<Integer> list1, ArrayList<Integer> list2) {

		int diff = 0;

		for (int i = 0; i < list1.size(); i++) {

			diff += Math.abs(list1.get(i) - list2.get(i));
		}

		return "" + diff;
	}

	private static String part2(ArrayList<Integer> list1, ArrayList<Integer> list2) {

		int similarity = 0;

		for (int i = 0; i < list1.size(); i++) {
			int occurrence = 0;
			for (int j = 0; j < list2.size(); j++) {
				if (list1.get(i).equals(list2.get(j)))
					occurrence++;
			}
			similarity += list1.get(i) * occurrence;
		}

		return "" + similarity;
	}

	private static String readFileAsString(String fileName) throws Exception {
		String data = "";
		data = new String(Files.readAllBytes(Paths.get(fileName)));
		System.out.println("Filesize: " + data.length());
		return data;
	}
}
