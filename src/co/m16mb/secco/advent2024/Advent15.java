package co.m16mb.secco.advent2024;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Advent15 {

	private static final String filenamePath = "files/file15.txt";

	public static void main(String[] args) throws Exception {

		// reading the input file
		String fileContents = readFileAsString(filenamePath);

		HashMap<Point, String> regionMap = new HashMap<>();

		String[] parts = fileContents.split("\\r?\\n\\r?\\n");

		int y = 0;
		int x = 0;
		Point startingPoint = null;
		for (String line : parts[0].split("\\r?\\n")) {
			String[] chars = line.split("");
			x = 0;
			for (String s : chars) {
				if ("@".equals(s)) {
					startingPoint = new Point(x, y);
					regionMap.put(new Point(x, y), ".");
				} else {
					regionMap.put(new Point(x, y), s);
				}
				x++;
			}
			y++;
		}

		String movements = parts[1].replaceAll("\\r?\\n", "");

		System.out.println("ANSWER1: " + part1(regionMap, x, y, startingPoint, movements));
		// 1430536

		HashMap<Point, String> regionMap2 = new HashMap<>();

		y = 0;
		for (String line : parts[0].split("\\r?\\n")) {
			String[] chars = line.split("");
			x = 0;
			for (String s : chars) {
				if ("@".equals(s)) {
					startingPoint = new Point(x * 2, y);
					regionMap2.put(new Point(x * 2, y), ".");
					regionMap2.put(new Point(x * 2 + 1, y), ".");
				} else if ("O".equals(s)) {
					regionMap2.put(new Point(x * 2, y), "[");
					regionMap2.put(new Point(x * 2 + 1, y), "]");
				} else {
					regionMap2.put(new Point(x * 2, y), s);
					regionMap2.put(new Point(x * 2 + 1, y), s);
				}
				x++;
			}
			y++;
		}

		System.out.println("ANSWER2: " + part2(regionMap2, x * 2, y, startingPoint, movements));
		// TODO 15 p2: fully implement double boxes moving

	}

	private static String part1(HashMap<Point, String> regionMap, int maxX, int maxY, Point currentPoint,
			String movements) {

		/*
		 * for (int j = 0; j <maxY; j++) { for (int i = 0; i < maxX; i++) { if(new
		 * Point(i,j).equals(currentPoint)) { System.out.print("@");
		 * 
		 * } else System.out.print(regionMap.get(new Point(i,j)));
		 * 
		 * } System.out.println(); }-
		 */

		for (String move : movements.split("")) {
			Point nextPoint = null;
			// >^<v

			switch (move) {

			case ">":
				nextPoint = new Point(currentPoint.x + 1, currentPoint.y);
				break;
			case "<":
				nextPoint = new Point(currentPoint.x - 1, currentPoint.y);
				break;
			case "^":
				nextPoint = new Point(currentPoint.x, currentPoint.y - 1);
				break;
			case "v":
				nextPoint = new Point(currentPoint.x, currentPoint.y + 1);
				break;
			default:
				System.err.println("WRONG MOVE " + move);
				break;
			}

			int xDiff = nextPoint.x - currentPoint.x;
			int yDiff = nextPoint.y - currentPoint.y;

			if (regionMap.get(nextPoint).equals(".")) {
				// System.out.println(currentPoint + " moving to " + nextPoint);
				currentPoint = nextPoint;
			} else if (regionMap.get(nextPoint).equals("#")) {
				// System.out.println(currentPoint + " FALL NOT moving to " + nextPoint);
				continue;
			} else {
				// System.out.println(currentPoint + " OBSTACLE towards " + nextPoint);

				int firstFreeSpaceX = Integer.MIN_VALUE;
				int firstFreeSpaceY = Integer.MIN_VALUE;
				Point tempPoint = null;
				for (int i = currentPoint.x + xDiff, j = currentPoint.y + yDiff; i >= 0 && j >= 0 && i < maxX
						&& j < maxY; i += xDiff, j += yDiff) {
					// System.out.println(currentPoint + " i " + i + " j " + j);
					tempPoint = new Point(i, j);
					if (regionMap.get(tempPoint).equals("#")) {
						// not movable brick wall
						break;
					} else if (regionMap.get(tempPoint).equals(".")) {
						// found a free slot
						firstFreeSpaceX = i;
						firstFreeSpaceY = j;
						break;
					}
				}

				if (firstFreeSpaceX != Integer.MIN_VALUE && firstFreeSpaceY != Integer.MIN_VALUE) {
					// System.out.println("CAN MOVE");
					regionMap.put(tempPoint, "O");
					regionMap.put(nextPoint, ".");
					currentPoint = nextPoint;
				}
				// else System.out.println("CANNOT MOVE");

			}
		}

		long sum = 0;
		for (int j = 0; j < maxY; j++) {
			for (int i = 0; i < maxX; i++) {
				if (regionMap.get(new Point(i, j)).equals("O")) {
					sum += j * 100;
					sum += i;
				}
			}
		}

		return sum + "";

	}

	private static String part2(HashMap<Point, String> regionMap, int maxX, int maxY, Point currentPoint,
			String movements) {

		for (int j = 0; j < maxY; j++) {
			for (int i = 0; i < maxX; i++) {
				if (new Point(i, j).equals(currentPoint)) {
					System.out.print("@");
				} else
					System.out.print(regionMap.get(new Point(i, j)));
			}
			System.out.println();
		}

		long sum = 0;
		return sum + "";

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
