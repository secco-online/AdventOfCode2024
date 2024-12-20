package co.m16mb.secco.advent2024;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public class Advent18 {

	private static final String filenamePath = "files/file18.txt";

	final static int MIN_X = 0;
	final static int MIN_Y = 0;
	final static int MAX_X = 70; // 6 70
	final static int MAX_Y = 70;// 6 70
	final static int NANOSECONDS = 1024; // 12 1024

	public static void main(String[] args) throws Exception {

		// reading the input file
		String fileContents = readFileAsString(filenamePath);

		Point startingPoint = new Point(MIN_X, MIN_Y);
		Point endPoint = new Point(MAX_X, MAX_Y);

		HashSet<Point> obstacleList = new HashSet<>();
		for (int nanosecond = 0; nanosecond < NANOSECONDS; nanosecond++) {
			String[] coordinates = fileContents.split("\\r?\\n")[nanosecond].split(",");
			obstacleList.add(new Point(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1])));
		}

		System.out.println("ANSWER1: " + part1(fileContents, obstacleList, startingPoint, endPoint));
		// 382
		System.out.println("ANSWER2: " + part2(fileContents, obstacleList, startingPoint, endPoint));
		// 6,36
	}

	private static String part1(String fileContents, HashSet<Point> obstacleList, Point currentPoint, Point endPoint) {
		return "" + findMinScoreOfPath(obstacleList, currentPoint, endPoint);
	}

	private static String part2(String fileContents, HashSet<Point> obstacleList, Point currentPoint, Point endPoint) {
		for (int nanosecond = NANOSECONDS; nanosecond < fileContents.split("\\r?\\n").length; nanosecond++) {

			String line = fileContents.split("\\r?\\n")[nanosecond];
			String[] coordinates = line.split(",");
			obstacleList.add(new Point(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1])));

			System.out.println("NANOSECOND " + nanosecond + " Obstacle number: " + obstacleList.size());
			if (findMinScoreOfPath(obstacleList, currentPoint, endPoint) == Integer.MAX_VALUE - 1) {
				// CANNOT PASS
				System.out.println("BLOCKED!!");
				return line;
			}
		}
		return null;
	}

	private static int findMinScoreOfPath(HashSet<Point> obstacleList, Point currentPoint, Point endPoint) {

		ArrayList<Step> possiblePaths = new ArrayList<>();

		PriorityQueue<Step> queue = new PriorityQueue<>();
		queue.add(new Step(currentPoint, Set.of(currentPoint), calculateDistanceFormEnd(currentPoint, endPoint)));

		HashMap<Point, Integer> pointCurrentValue = new HashMap<>();

		int minScore = Integer.MAX_VALUE;

		while (!queue.isEmpty()) {
			Step step = queue.poll();
			// System.out.println("QUEUE " + queue.size());
			// System.out.println("POINT " + pointCurrentValue.size());

			// optimisation part. choose the best path till now
			if (pointCurrentValue.containsKey(step.location)) {
				if (pointCurrentValue.get(step.location) <= step.visitedPoints.size()) {
					// there is a better path that arrived to this point
					// System.out.println("BETTER FOUND THAN THIS " + step.visitedPoints.size() + "
					// " + step.location);
					continue;
				} else {
					pointCurrentValue.put(step.location, step.visitedPoints.size());
				}
			} else {
				// first path arriving to this point
				pointCurrentValue.put(step.location, step.visitedPoints.size());
			}

			if (endPoint.equals(step.location)) {
				// ARRIVED
				// System.out.println("ARRIVED " + step.visitedPoints.size());
				// System.out.println(step);
				possiblePaths.add(step);
				if (minScore > step.visitedPoints.size())
					minScore = step.visitedPoints.size();

			} else if (obstacleList.contains(step.location)) {
				// IN A WALL, DO NOTHING
			} else {
				// CAN WALK
				List<Point> neighbours = step.location.getNeighbours();
				for (Point n : neighbours) {
					if (!step.visitedPoints.contains(n) && n.isValidLocation()) {
						// not yet visited
						Set<Point> visited = new HashSet<Point>();
						visited.addAll(step.visitedPoints);
						visited.add(n);
						queue.add(new Step(n, visited, calculateDistanceFormEnd(n, endPoint)));
					}

				}

			}
		}
		/*
		 * for (Step s : possiblePaths) { if (s.visitedPoints.size() == minScore) { //
		 * DO PRINT THE PATH for (int j = 0; j <= MAX_Y; j++) { for (int i = 0; i <=
		 * MAX_X; i++) {
		 * 
		 * if (obstacleList.contains(new Point(i, j))) System.out.print("#"); else if
		 * (s.visitedPoints.contains(new Point(i, j))) System.out.print("O"); else
		 * System.out.print("."); } System.out.println(); }
		 * 
		 * } }
		 */
		return minScore - 1;
	}

	private static double calculateDistanceFormEnd(Point p1, Point p2) {
		return Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2));

	}

	private static record Step(Point location, Set<Point> visitedPoints, double distanceFromEnd)
			implements Comparable<Step> {

		@Override
		public int compareTo(Step o) {
			return (int) (distanceFromEnd - o.distanceFromEnd);
			// keep it on diagonal
			// return Math.abs(o.location.x-o.location.y) + Math.abs(location.x-location.y);
		}

	};

	private static record Point(int x, int y) {
		public List<Point> getNeighbours() {
			return Arrays.asList(new Point(x, y - 1), new Point(x, y + 1), new Point(x - 1, y), new Point(x + 1, y));
		}

		boolean isValidLocation() {
			if (x >= MIN_X && y >= MIN_Y && x <= MAX_X && y <= MAX_Y) {
				return true;
			} else
				return false;
		}
	};

	public static String readFileAsString(String fileName) throws Exception {
		String data = "";
		data = new String(Files.readAllBytes(Paths.get(fileName)));
		System.out.println("Filesize: " + data.length());
		return data;
	}

}
