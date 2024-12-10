package co.m16mb.secco.advent2024;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class Advent10 {

	private static final String filenamePath = "files/file10.txt";

	public static void main(String[] args) throws Exception {

		// reading the input file
		String fileContents = readFileAsString(filenamePath);

		HashMap<Point, Integer> numberMap = new HashMap<>();

		int y = 0;
		int x = 0;
		for (String line : fileContents.split("\\r?\\n")) {
			String[] chars = line.split("");
			x = 0;
			for (String s : chars) {
				int val = ".".equals(s) ? -1 : Integer.parseInt(s);
				numberMap.put(new Point(x, y), val);
				x++;
			}
			y++;
		}

		System.out.println("ANSWER1: " + part1(numberMap));
		// 629
		System.out.println("ANSWER2: " + part2(numberMap));
		//

	}

	private static String part1(HashMap<Point, Integer> numberMap) {

		Queue<Path> queue = new LinkedList<>();
		HashSet<Path> scoresPath = new HashSet<>();

		// get all starting points
		for (Map.Entry<Point, Integer> entry : numberMap.entrySet()) {
			Point point = entry.getKey();
			Integer val = entry.getValue();

			if (val == 0) {
				queue.add(new Path(point, point, val));
			}
		}

		while (!queue.isEmpty()) {
			Path path = queue.poll();
			if (path.currentValue == 9) {
				// the end
				scoresPath.add(path);
			} else {
				// not yet the end
				for (Point neighbour : path.currentPoint.getNeighbours()) {
					Integer neighbourValue = numberMap.get(neighbour);
					if (neighbourValue != null && neighbourValue == path.currentValue + 1) {
						queue.add(new Path(path.startingPoint, neighbour, neighbourValue));
					}
				}
			}

		}
		return scoresPath.size() + "";

	}

	private static String part2(HashMap<Point, Integer> numberMap) {

		Queue<Path> queue = new LinkedList<>();
		HashMap<Point, Integer> scores = new HashMap<>();

		// get all starting points
		for (Map.Entry<Point, Integer> entry : numberMap.entrySet()) {
			Point point = entry.getKey();
			Integer val = entry.getValue();

			if (val == 0) {
				queue.add(new Path(point, point, val));
				scores.put(point, 0);
			}
		}

		while (!queue.isEmpty()) {
			Path path = queue.poll();
			if (path.currentValue == 9) {
				// the end
				scores.put(path.startingPoint, scores.get(path.startingPoint) + 1);

			} else {
				// not yet the end
				for (Point neighbour : path.currentPoint.getNeighbours()) {
					Integer neighbourValue = numberMap.get(neighbour);
					if (neighbourValue != null && neighbourValue == path.currentValue + 1) {
						queue.add(new Path(path.startingPoint, neighbour, neighbourValue));
					}
				}
			}

		}

		long sum = 0;

		for (Map.Entry<Point, Integer> entry : scores.entrySet()) {

			Integer val = entry.getValue();
//			System.out.println(entry);
			sum += val;
		}

		return sum + "";

	}

	public static String readFileAsString(String fileName) throws Exception {
		String data = "";
		data = new String(Files.readAllBytes(Paths.get(fileName)));
		System.out.println("Filesize: " + data.length());
		return data;
	}

	private static record Point(int x, int y) {
		public List<Point> getNeighbours() {
			return Arrays.asList(new Point(x, y - 1), new Point(x, y + 1), new Point(x - 1, y), new Point(x + 1, y));
		}
	};

	private static record Path(Point startingPoint, Point currentPoint, int currentValue) {
	};

}
