package co.m16mb.secco.advent2024;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class Advent04 {

	private static final String filenamePath = "files/file04.txt";

	public static void main(String[] args) throws Exception {

		// reading the input file
		String fileContents = readFileAsString(filenamePath);

		HashMap<Point, String> letterMap = new HashMap<>();

		int y = 0;
		int x = 0;
		for (String line : fileContents.split("\\r?\\n")) {

			String[] chars = line.split("");

			x = 0;
			for (String s : chars) {
				letterMap.put(new Point(x, y), s);
				x++;
			}
			y++;
		}

		System.out.println("ANSWER1: " + part1(letterMap, x, y));
		// 2414
		System.out.println("ANSWER2: " + part2(letterMap, x, y));
		// 1871

	}

	private static String part1(HashMap<Point, String> letterMap, int maxX, int maxY) {
		long sum = 0;

		for (int j = 0; j < maxY; j++) {
			for (int i = 0; i < maxX; i++) {
				if (isLeftToRightXMAS(letterMap, i, j))
					sum++;
				if (isRightToLeftXMAS(letterMap, i, j))
					sum++;
				if (isTopToBottomXMAS(letterMap, i, j))
					sum++;
				if (isBottomToTopXMAS(letterMap, i, j))
					sum++;
				if (isDiagonalTowardsRightBottomXMAS(letterMap, i, j))
					sum++;
				if (isDiagonalTowardsLeftBottomXMAS(letterMap, i, j))
					sum++;
				if (isDiagonalTowardsRightTopXMAS(letterMap, i, j))
					sum++;
				if (isDiagonalTowardsLeftTopXMAS(letterMap, i, j))
					sum++;
			}
		}

		return sum + "";

	}

	private static String part2(HashMap<Point, String> letterMap, int maxX, int maxY) {
		long sum = 0;

		for (int j = 0; j < maxY; j++) {
			for (int i = 0; i < maxX; i++) {
				if (isX_MASright(letterMap, i, j))
					sum++;
				if (isX_MASleft(letterMap, i, j))
					sum++;
				if (isX_MAStop(letterMap, i, j))
					sum++;
				if (isX_MASbottom(letterMap, i, j))
					sum++;
			}
		}
		return sum + "";
	}

	private static boolean isLeftToRightXMAS(HashMap<Point, String> letterMap, int startX, int startY) {
		if ("X".equals(letterMap.get(new Point(startX, startY)))
				&& "M".equals(letterMap.get(new Point(startX + 1, startY)))
				&& "A".equals(letterMap.get(new Point(startX + 2, startY)))
				&& "S".equals(letterMap.get(new Point(startX + 3, startY))))
			return true;
		else
			return false;
	}

	private static boolean isRightToLeftXMAS(HashMap<Point, String> letterMap, int startX, int startY) {
		if ("X".equals(letterMap.get(new Point(startX, startY)))
				&& "M".equals(letterMap.get(new Point(startX - 1, startY)))
				&& "A".equals(letterMap.get(new Point(startX - 2, startY)))
				&& "S".equals(letterMap.get(new Point(startX - 3, startY))))
			return true;
		else
			return false;
	}

	private static boolean isTopToBottomXMAS(HashMap<Point, String> letterMap, int startX, int startY) {
		if ("X".equals(letterMap.get(new Point(startX, startY)))
				&& "M".equals(letterMap.get(new Point(startX, startY + 1)))
				&& "A".equals(letterMap.get(new Point(startX, startY + 2)))
				&& "S".equals(letterMap.get(new Point(startX, startY + 3))))
			return true;
		else
			return false;
	}

	private static boolean isBottomToTopXMAS(HashMap<Point, String> letterMap, int startX, int startY) {
		if ("X".equals(letterMap.get(new Point(startX, startY)))
				&& "M".equals(letterMap.get(new Point(startX, startY - 1)))
				&& "A".equals(letterMap.get(new Point(startX, startY - 2)))
				&& "S".equals(letterMap.get(new Point(startX, startY - 3))))
			return true;
		else
			return false;
	}

	private static boolean isDiagonalTowardsRightBottomXMAS(HashMap<Point, String> letterMap, int startX, int startY) {
		if ("X".equals(letterMap.get(new Point(startX, startY)))
				&& "M".equals(letterMap.get(new Point(startX + 1, startY + 1)))
				&& "A".equals(letterMap.get(new Point(startX + 2, startY + 2)))
				&& "S".equals(letterMap.get(new Point(startX + 3, startY + 3))))
			return true;
		else
			return false;
	}

	private static boolean isDiagonalTowardsLeftBottomXMAS(HashMap<Point, String> letterMap, int startX, int startY) {
		if ("X".equals(letterMap.get(new Point(startX, startY)))
				&& "M".equals(letterMap.get(new Point(startX - 1, startY + 1)))
				&& "A".equals(letterMap.get(new Point(startX - 2, startY + 2)))
				&& "S".equals(letterMap.get(new Point(startX - 3, startY + 3))))
			return true;
		else
			return false;
	}

	private static boolean isDiagonalTowardsRightTopXMAS(HashMap<Point, String> letterMap, int startX, int startY) {
		if ("X".equals(letterMap.get(new Point(startX, startY)))
				&& "M".equals(letterMap.get(new Point(startX + 1, startY - 1)))
				&& "A".equals(letterMap.get(new Point(startX + 2, startY - 2)))
				&& "S".equals(letterMap.get(new Point(startX + 3, startY - 3))))
			return true;
		else
			return false;
	}

	private static boolean isDiagonalTowardsLeftTopXMAS(HashMap<Point, String> letterMap, int startX, int startY) {
		if ("X".equals(letterMap.get(new Point(startX, startY)))
				&& "M".equals(letterMap.get(new Point(startX - 1, startY - 1)))
				&& "A".equals(letterMap.get(new Point(startX - 2, startY - 2)))
				&& "S".equals(letterMap.get(new Point(startX - 3, startY - 3))))
			return true;
		else
			return false;
	}

	private static boolean isX_MASright(HashMap<Point, String> letterMap, int startX, int startY) {
		if ("A".equals(letterMap.get(new Point(startX, startY)))
				&& "M".equals(letterMap.get(new Point(startX - 1, startY - 1)))
				&& "M".equals(letterMap.get(new Point(startX - 1, startY + 1)))
				&& "S".equals(letterMap.get(new Point(startX + 1, startY - 1)))
				&& "S".equals(letterMap.get(new Point(startX + 1, startY + 1))))
			return true;
		else
			return false;
	}

	private static boolean isX_MASleft(HashMap<Point, String> letterMap, int startX, int startY) {
		if ("A".equals(letterMap.get(new Point(startX, startY)))
				&& "S".equals(letterMap.get(new Point(startX - 1, startY - 1)))
				&& "S".equals(letterMap.get(new Point(startX - 1, startY + 1)))
				&& "M".equals(letterMap.get(new Point(startX + 1, startY - 1)))
				&& "M".equals(letterMap.get(new Point(startX + 1, startY + 1))))
			return true;
		else
			return false;
	}

	private static boolean isX_MAStop(HashMap<Point, String> letterMap, int startX, int startY) {
		if ("A".equals(letterMap.get(new Point(startX, startY)))
				&& "S".equals(letterMap.get(new Point(startX - 1, startY - 1)))
				&& "M".equals(letterMap.get(new Point(startX - 1, startY + 1)))
				&& "S".equals(letterMap.get(new Point(startX + 1, startY - 1)))
				&& "M".equals(letterMap.get(new Point(startX + 1, startY + 1))))
			return true;
		else
			return false;
	}

	private static boolean isX_MASbottom(HashMap<Point, String> letterMap, int startX, int startY) {
		if ("A".equals(letterMap.get(new Point(startX, startY)))
				&& "M".equals(letterMap.get(new Point(startX - 1, startY - 1)))
				&& "S".equals(letterMap.get(new Point(startX - 1, startY + 1)))
				&& "M".equals(letterMap.get(new Point(startX + 1, startY - 1)))
				&& "S".equals(letterMap.get(new Point(startX + 1, startY + 1))))
			return true;
		else
			return false;
	}

	public static String readFileAsString(String fileName) throws Exception {
		String data = "";
		data = new String(Files.readAllBytes(Paths.get(fileName)));
		System.out.println("Filesize: " + data.length());
		return data;
	}

	private static record Point(int x, int y) {
	};

}
