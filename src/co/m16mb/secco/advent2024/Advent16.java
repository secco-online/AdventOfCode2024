package co.m16mb.secco.advent2024;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class Advent16 {

	private static final String filenamePath = "files/file16.txt";

	public static void main(String[] args) throws Exception {

		// reading the input file
		String fileContents = readFileAsString(filenamePath);

		HashMap<Point, String> regionMap = new HashMap<>();
		int x = 0;
		int y = 0;
		HashSet<Point> obstacleList = new HashSet<>();

		DirectionalPoint startingPoint = null;
		Point endPoint = null;
		for (String line : fileContents.split("\\r?\\n")) {
			String[] chars = line.split("");
			x = 0;
			for (String s : chars) {
				if ("S".equals(s)) {
					startingPoint = new DirectionalPoint(new Point(x, y), 'E');
				} else if ("E".equals(s)) {
					endPoint = new Point(x, y);
				} else if ("#".equals(s)) {
					obstacleList.add(new Point(x, y));
				}
				regionMap.put(new Point(x, y), s);
				x++;
			}
			y++;
		}

		System.out.println(" " + part1(regionMap, obstacleList, x, y, startingPoint, endPoint));
		// 133584
		// 622

	}

	private static String part1(HashMap<Point, String> regionMap, HashSet<Point> obstacleList, int maxX, int maxY,
			DirectionalPoint currentPoint, Point endPoint) {

		ArrayList<Step> possiblePaths = new ArrayList<>();

		Queue<Step> queue = new LinkedList<>();
		queue.add(new Step(currentPoint, 0, 0, Set.of(currentPoint.location)));

		HashMap<DirectionalPoint, Integer> pointCurrentValue = new HashMap<>();

		int minScore = Integer.MAX_VALUE;
		while (!queue.isEmpty()) {
			Step step = queue.poll();
			// System.out.println(queue.size());
			String value = regionMap.get(step.directionalPoint.location);

			// optimisation part. choose the best path till now
			if (pointCurrentValue.containsKey(step.directionalPoint)) {
				if (pointCurrentValue.get(step.directionalPoint) < step.getScore()) {
					// there is a better path that arrived to this point
					continue;
				} else {
					pointCurrentValue.put(step.directionalPoint, step.getScore());
				}
			} else {
				// first path arriving to this point
				pointCurrentValue.put(step.directionalPoint, step.getScore());
			}

			if ("E".equals(value)) {
				// ARRIVED
				//System.out.println("ARRIVED " + step.getScore());
				//System.out.println(step);

				possiblePaths.add(step);
				if(minScore>step.getScore()) {
					minScore = step.getScore();
				}
				
				// } else if ("#".equals(value)) {
			} else if (obstacleList.contains(step.directionalPoint.location)) {
				// IN A WALL, DO NOTHING
			} else if (".".equals(value) || "S".equals(value)) {
				// CAN WALK

				DirectionalPoint straight = step.getNeighbourAhead();
				if (!step.visitedPoints.contains(straight.location)) {
					// not yet visited
					Set<Point> visited = new HashSet<Point>();
					visited.addAll(step.visitedPoints);
					visited.add(straight.location);
					queue.add(new Step(straight, step.numSteps + 1, step.numTurns, visited));
				}

				for (DirectionalPoint turn : step.getNeighboursWithTurn()) {
					if (!step.visitedPoints.contains(turn.location)) {
						// not yet visited
						Set<Point> visited = new HashSet<Point>();
						visited.addAll(step.visitedPoints);
						visited.add(turn.location);
						queue.add(new Step(turn, step.numSteps + 1, step.numTurns + 1, visited));
					}
				}
			} else
				System.err.println("STRANGE VALUE " + value);
		}
/*
		for (int j = 0; j < maxY; j++) {
			for (int i = 0; i < maxX; i++) {
				String show = regionMap.get(new Point(i, j));
				if (pointsOnBestPath.contains(new Point(i, j)))
					show = "O";
				System.out.print(show);
			}
			System.out.println();
		}
*/
		//PART 2
		HashSet<Point> pointsOnBestPath = new HashSet<>();
		
		for (Step path : possiblePaths) {
			if (path.getScore() == minScore) {
				pointsOnBestPath.addAll(path.visitedPoints);
				//System.out.println("BEST PATH " + path.numSteps + " " + path.numTurns);
			}
		}
		System.out.println("ANSWER1 " + minScore);
		System.out.println("ANSWER2 " + pointsOnBestPath.size());

		return "";
	}

	private static record Step(DirectionalPoint directionalPoint, int numSteps, int numTurns,
			Set<Point> visitedPoints) {
		int getScore() {
			return numSteps + numTurns * 1000;
		}

		private List<DirectionalPoint> getNeighboursWithTurn() {
			switch (directionalPoint.direction) {
			case 'E':
			case 'W':
				return Arrays.asList(
						new DirectionalPoint(new Point(directionalPoint.location.x, directionalPoint.location.y - 1),
								'N'),
						new DirectionalPoint(new Point(directionalPoint.location.x, directionalPoint.location.y + 1),
								'S'));
			case 'N':
			case 'S':
				return Arrays.asList(
						new DirectionalPoint(new Point(directionalPoint.location.x - 1, directionalPoint.location.y),
								'W'),
						new DirectionalPoint(new Point(directionalPoint.location.x + 1, directionalPoint.location.y),
								'E'));
			default:
				return Arrays.asList();
			}
		}

		public DirectionalPoint getNeighbourAhead() {
			switch (directionalPoint.direction) {
			case 'E':
				return new DirectionalPoint(new Point(directionalPoint.location.x + 1, directionalPoint.location.y),
						directionalPoint.direction);
			case 'W':
				return new DirectionalPoint(new Point(directionalPoint.location.x - 1, directionalPoint.location.y),
						directionalPoint.direction);
			case 'N':
				return new DirectionalPoint(new Point(directionalPoint.location.x, directionalPoint.location.y - 1),
						directionalPoint.direction);
			case 'S':
				return new DirectionalPoint(new Point(directionalPoint.location.x, directionalPoint.location.y + 1),
						directionalPoint.direction);
			default:
				return null;
			}
		}

	};

	private static record Point(int x, int y) {
	};

	private static record DirectionalPoint(Point location, char direction) {
	};

	public static String readFileAsString(String fileName) throws Exception {
		String data = "";
		data = new String(Files.readAllBytes(Paths.get(fileName)));
		System.out.println("Filesize: " + data.length());
		return data;
	}

}
