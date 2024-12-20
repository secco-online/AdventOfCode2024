package co.m16mb.secco.advent2024;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class Advent12 {

	private static final String filenamePath = "files/file12.txt";

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

		HashMap<Point, HashSet<Point>> allGroups = getAllGroups(letterMap, x, y);

		System.out.println("ANSWER1: " + part1(allGroups));
		// 1374934
		System.out.println("ANSWER2: " + part2(allGroups, letterMap, x, y));
		// 841078
	}

	private static String part1(HashMap<Point, HashSet<Point>> allGroups) {

		int totalPrice = 0;
		for (Map.Entry<Point, HashSet<Point>> entry : allGroups.entrySet()) {
			HashSet<Point> pointsInTheGroup = entry.getValue();

			int area = pointsInTheGroup.size();
			int perimeter = 0;
			for (Point p : pointsInTheGroup) {
				int adjacent = 0;

				for (Point n : p.getNeighbours()) {
					if (pointsInTheGroup.contains(n))
						adjacent++;
				}
				perimeter += (4 - adjacent);
			}
			totalPrice += area * perimeter;
		}

		return totalPrice + "";
	}

	private static String part2(HashMap<Point, HashSet<Point>> allGroups, HashMap<Point, String> letterMap, int maxX,
			int maxY) {

		int totalPrice = 0;

		for (Map.Entry<Point, HashSet<Point>> entry : allGroups.entrySet()) {
			HashSet<Point> pointsInTheGroup = entry.getValue();
			Point point = entry.getKey();

			int area = pointsInTheGroup.size();

			int countEdges = 0;

			// TOP
			for (int j = 0; j <= maxY; j++) {
				boolean isEdge = false;
				for (int i = 0; i <= maxX; i++) {
					// start counting all the horizontal edges

					if (pointsInTheGroup.contains(new Point(i, j))) {
						if (pointsInTheGroup.contains(new Point(i, j - 1))) {
							// no edge
							if (isEdge) {
								countEdges++;
								isEdge = false;
							}

						} else {
							// edge TOP
							isEdge = true;
						}

					} else {
						if (isEdge) {
							countEdges++;
							isEdge = false;
						}
					}
				}
			}

			// LEFT
			for (int i = 0; i <= maxX; i++) {
				boolean isEdge = false;
				for (int j = 0; j <= maxY; j++) {
					// start counting all the vertical edges

					if (pointsInTheGroup.contains(new Point(i, j))) {
						if (pointsInTheGroup.contains(new Point(i - 1, j))) {
							// no edge
							if (isEdge) {
								countEdges++;
								isEdge = false;
							}

						} else {
							// edge TOP
							isEdge = true;
						}

					} else {
						if (isEdge) {
							countEdges++;
							isEdge = false;
						}
					}
				}
			}

			// BOTTOM
			for (int j = maxY; j >= 0; j--) {
				boolean isEdge = false;
				for (int i = 0; i <= maxX; i++) {
					// start counting all the horizontal edges

					if (pointsInTheGroup.contains(new Point(i, j))) {
						if (pointsInTheGroup.contains(new Point(i, j + 1))) {
							// no edge
							if (isEdge) {
								countEdges++;
								isEdge = false;
							}

						} else {
							// edge TOP
							isEdge = true;
						}

					} else {
						if (isEdge) {
							countEdges++;
							isEdge = false;
						}
					}
				}
			}

			// RIGHT
			for (int i = maxX; i >= 0; i--) {
				boolean isEdge = false;
				for (int j = 0; j <= maxY; j++) {
					// start counting all the vertical edges

					if (pointsInTheGroup.contains(new Point(i, j))) {
						if (pointsInTheGroup.contains(new Point(i + 1, j))) {
							// no edge
							if (isEdge) {
								countEdges++;
								isEdge = false;
							}

						} else {
							// edge TOP
							isEdge = true;
						}

					} else {
						if (isEdge) {
							countEdges++;
							isEdge = false;
						}
					}
				}
			}

			System.out.println(letterMap.get(point) + " edges: " + countEdges);

			totalPrice += area * countEdges;
			// System.out.println(" area: " + area + " perimeter: " + perimeter);
		}

		return totalPrice + "";
	}

	private static HashMap<Point, HashSet<Point>> getAllGroups(HashMap<Point, String> letterMap, int maxX, int maxY) {

		HashSet<Point> allVisitedPoints = new HashSet<>();
		HashMap<Point, HashSet<Point>> allGroups = new HashMap<>();

		for (int j = 0; j < maxY; j++) {
			for (int i = 0; i < maxX; i++) {
				Point StartingPoint = new Point(i, j);

				if (!allVisitedPoints.contains(StartingPoint)) {
					allVisitedPoints.add(StartingPoint);

					Queue<Point> queue = new LinkedList<>();
					queue.add(StartingPoint);
					HashSet<Point> pointsInTheGroup = new HashSet<>();
					pointsInTheGroup.add(StartingPoint);

					while (!queue.isEmpty()) {
						Point p = queue.poll();
						for (Point n : p.getNeighbours()) {
							if (letterMap.get(p).equals(letterMap.get(n)) && !pointsInTheGroup.contains(n)) {
								// letter is the same and was not visited yet
								queue.add(n);
								pointsInTheGroup.add(n);
								allVisitedPoints.add(n);

							}
						}
					}
					allGroups.put(StartingPoint, pointsInTheGroup);
				}
			}
		}

		return allGroups;
	}

	public static String readFileAsString(String fileName) throws Exception {
		String data = "";
		data = new String(Files.readAllBytes(Paths.get(fileName)));
		System.out.println("Filesize: " + data.length());
		return data;
	}

	private static record Point(int x, int y) {
		List<Point> getNeighbours() {
			return List.of(new Point(x - 1, y), new Point(x + 1, y), new Point(x, y - 1), new Point(x, y + 1));
		};
	};

}
