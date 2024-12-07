package co.m16mb.secco.advent2024;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;

public class Advent06 {

	private static final String filenamePath = "files/file06.txt";

	public static void main(String[] args) throws Exception {

		// reading the input file
		String fileContents = readFileAsString(filenamePath);
		HashMap<Point, String> letterMap = new HashMap<>();

		int y = 0;
		int x = 0;
		int startX = 0;
		int startY = 0;
		String direction = "^";

		for (String line : fileContents.split("\\r?\\n")) {

			String[] chars = line.split("");

			x = 0;
			for (String s : chars) {
				letterMap.put(new Point(x, y), s);
				if (direction.equals(s)) {
					startX = x;
					startY = y;
				}
				x++;
			}
			y++;
		}

		System.out.println("ANSWER1: " + part1(letterMap, x, y, startX, startY, direction));
		// 5269
		System.out.println("ANSWER2: " + part2(letterMap, x, y, startX, startY, direction));
		// 1957
	}

	private static String part1(HashMap<Point, String> letterMap, int maxX, int maxY, int currentPositionX,
			int currentPositionY, String direction) {

		HashSet<Point> stepSet = new HashSet<>();
		Point currentPoint = new Point(currentPositionX, currentPositionY);

		while (currentPoint.x >= 0 && currentPoint.x < maxX && currentPoint.y >= 0 && currentPoint.y < maxY) {

			stepSet.add(currentPoint);

			Point tempPoint = null;
			if ("^".equals(direction)) {
				tempPoint = new Point(currentPoint.x, currentPoint.y - 1);
			} else if (">".equals(direction)) {
				tempPoint = new Point(currentPoint.x + 1, currentPoint.y);
			} else if ("v".equals(direction)) {
				tempPoint = new Point(currentPoint.x, currentPoint.y + 1);
			} else if ("<".equals(direction)) {
				tempPoint = new Point(currentPoint.x - 1, currentPoint.y);
			}
			if (".".equals(letterMap.get(tempPoint))) {
				// free to go
				currentPoint = tempPoint;
			} else if ("#".equals(letterMap.get(tempPoint))) {
				// obstacle, turn 90 degrees right
				if ("^".equals(direction)) {
					direction = ">";
				} else if (">".equals(direction)) {
					direction = "v";
				} else if ("v".equals(direction)) {
					direction = "<";
				} else if ("<".equals(direction)) {
					direction = "^";
				}

			} else {
				// let it go out of the board
				currentPoint = tempPoint;

			}
		}

		return stepSet.size() + "";

	}

	private static String part2(HashMap<Point, String> letterMap, int maxX, int maxY, int startPositionX,
			int startPositionY, String startDirection) {

		int possibleObstacleCount = 0;

		for (int j = 0; j < maxY; j++) {
			for (int i = 0; i < maxX; i++) {
				if (0 > checkThePathWithAdditionalBlock(letterMap, maxX, maxY, startPositionX, startPositionY,
						startDirection, i, j)) {
					// found one
					//System.out.println("FOUND for " + i + " y " + j);
					possibleObstacleCount++;
				}
			}

		}

		return possibleObstacleCount + "";
	}

	private static int checkThePathWithAdditionalBlock(HashMap<Point, String> letterMap, int maxX, int maxY,
			int startPositionX, int startPositionY, String startDirection, int blockX, int blockY) {

		HashSet<Point> stepSet = new HashSet<>();
		Point currentPoint = new Point(startPositionX, startPositionY);
		String direction = new String(startDirection);

		HashSet<String> stepSetWithDirection = new HashSet<>();

		while (currentPoint.x >= 0 && currentPoint.x < maxX && currentPoint.y >= 0 && currentPoint.y < maxY) {

			if (stepSetWithDirection.contains(currentPoint + direction)) {
				//System.out.println("LOOP ");
				return -1;
			} else {
				stepSetWithDirection.add(currentPoint + direction);
			}

			Point tempPoint = null;
			if ("^".equals(direction)) {
				tempPoint = new Point(currentPoint.x, currentPoint.y - 1);
			} else if (">".equals(direction)) {
				tempPoint = new Point(currentPoint.x + 1, currentPoint.y);
			} else if ("v".equals(direction)) {
				tempPoint = new Point(currentPoint.x, currentPoint.y + 1);
			} else if ("<".equals(direction)) {
				tempPoint = new Point(currentPoint.x - 1, currentPoint.y);
			}

			if ("#".equals(letterMap.get(tempPoint)) || (blockX == tempPoint.x && blockY == tempPoint.y)) {
				// obstacle, turn 90 degrees right
				if ("^".equals(direction)) {
					direction = ">";
				} else if (">".equals(direction)) {
					direction = "v";
				} else if ("v".equals(direction)) {
					direction = "<";
				} else if ("<".equals(direction)) {
					direction = "^";
				}
			} else if (".".equals(letterMap.get(tempPoint))) {
				// free to go
				currentPoint = tempPoint;
			} else {
				// let it go out of the board
				currentPoint = tempPoint;

			}
		}

		return stepSet.size();
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
