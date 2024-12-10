package co.m16mb.secco.advent2024;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Advent09 {

	private static final String filenamePath = "files/file09.txt";

	public static void main(String[] args) throws Exception {

		// reading the input file
		String fileContents = readFileAsString(filenamePath);

		System.out.println("ANSWER1: " + part1(fileContents));
		// 6399153661894
		System.out.println("ANSWER2: " + part2(fileContents));
		// 6421724645083
	}

	private static long part1(String fileContents) {
		// read the list to ArrayList
		ArrayList<Integer> fileAndSpaceList = readFileContentsToArray(fileContents);

		// perform moving
		int offOfLastElement = fileAndSpaceList.size() - 1;
		for (int i = 0; i < offOfLastElement; i++) {
			int objectID = fileAndSpaceList.get(i);
			if (objectID == -1) {
				int lastElementValue = fileAndSpaceList.get(offOfLastElement);
				while (lastElementValue < 0) {
					lastElementValue = fileAndSpaceList.get(offOfLastElement);
					// System.out.println("WHILE " + lastElementValue + " offOfLastElement " +
					// offOfLastElement);
					if (lastElementValue == -1)
						offOfLastElement--;
				}
				fileAndSpaceList.set(i, lastElementValue);
				offOfLastElement--;
			}
		}

		// remove already moved elements
		for (int i = fileAndSpaceList.size() - 1; i > offOfLastElement; i--) {
			fileAndSpaceList.remove(i);
		}

		return calculateCheckSum(fileAndSpaceList);
	}

	private static long part2(String fileContents) {
		// read the list to ArrayList
		ArrayList<Integer> fileAndSpaceList = readFileContentsToArray(fileContents);

		int lastGroupID = fileAndSpaceList.get(fileAndSpaceList.size() - 1);

		// perform moving
		for (int id = lastGroupID; id >= 0; id--) {
			int startOffset = findStartingOffsetOfGroupID(fileAndSpaceList, id);
			int length = findLengthOfGroupID(fileAndSpaceList, id);
			int startOfEmptySpace = findStartingOffsetEmptySpaceOfMinimumLength(fileAndSpaceList, length, startOffset);
			// System.out.println("ID: " + id + " S: " + startOffset + " L: " + length + "
			// E: " + startOfEmptySpace);

			if (startOfEmptySpace != Integer.MIN_VALUE) {
				// THERE IS SPACE
				for (int i = 0; i < length; i++) {
					// move group
					fileAndSpaceList.set(startOfEmptySpace + i, id);
					// free old space
					fileAndSpaceList.set(startOffset + i, -1);
				}
			}
		}
		return calculateCheckSum(fileAndSpaceList);
	}

	private static int findStartingOffsetOfGroupID(ArrayList<Integer> fileAndSpaceList, int id) {
		for (int i = 0; i < fileAndSpaceList.size(); i++) {
			if (fileAndSpaceList.get(i) == id)
				return i;
		}
		return Integer.MIN_VALUE;
	}

	private static int findLengthOfGroupID(ArrayList<Integer> fileAndSpaceList, int id) {
		int groupLength = 0;
		for (int i = 0; i < fileAndSpaceList.size(); i++) {
			if (fileAndSpaceList.get(i) == id)
				groupLength++;
		}
		return groupLength;
	}

	private static int findStartingOffsetEmptySpaceOfMinimumLength(ArrayList<Integer> fileAndSpaceList, int minLength,
			int beforeOffset) {
		int tempStartingPoint = Integer.MIN_VALUE;
		int tempLength = 0;
		for (int i = 0; i < beforeOffset; i++) {
			if (fileAndSpaceList.get(i) == -1) {
				// an empty space
				if (tempStartingPoint == Integer.MIN_VALUE) {
					tempStartingPoint = i;

				}
				tempLength++;
				if (tempLength >= minLength)
					return tempStartingPoint;
			} else {
				// occupied space
				tempStartingPoint = Integer.MIN_VALUE;
				tempLength = 0;
			}
		}
		return Integer.MIN_VALUE;
	}

	private static ArrayList<Integer> readFileContentsToArray(String fileContents) {
		ArrayList<Integer> fileAndSpaceList = new ArrayList<>();

		int offset = 0;
		for (String oneChar : fileContents.split("")) {
			int number = Integer.parseInt(oneChar);
			if (offset % 2 == 0) {
				// a file with ID
				int fileID = offset / 2;
				for (int i = 0; i < number; i++) {
					fileAndSpaceList.add(fileID);
				}
			} else {
				// free space
				for (int i = 0; i < number; i++) {
					fileAndSpaceList.add(-1);
				}
			}
			offset++;
		}
		return fileAndSpaceList;
	}

	private static long calculateCheckSum(ArrayList<Integer> fileAndSpaceList) {
		// calculate checksum
		long checkSum = 0;
		for (int i = 0; i < fileAndSpaceList.size(); i++) {
			int value = fileAndSpaceList.get(i);

			if (value != -1)
				checkSum += i * value;
		}
		return checkSum;
	}

	public static String readFileAsString(String fileName) throws Exception {
		String data = "";
		data = new String(Files.readAllBytes(Paths.get(fileName)));
		System.out.println("Filesize: " + data.length());
		return data;
	}

}
