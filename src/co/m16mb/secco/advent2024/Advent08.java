package co.m16mb.secco.advent2024;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

public class Advent08 {

	private static final String filenamePath = "files/file08.txt";

	public static void main(String[] args) throws Exception {

		// reading the input file
		String fileContents = readFileAsString(filenamePath);

		HashMap<Point, String> numberMap = new HashMap<>();

		int y = 0;
		int x = 0;
		for (String line : fileContents.split("\\r?\\n")) {
			String[] chars = line.split("");
			x = 0;
			for (String s : chars) {
				numberMap.put(new Point(x, y), s);
				x++;
			}
			y++;
		}

		System.out.println("ANSWER1: " + part1(numberMap, x, y));
		// 369
		System.out.println("ANSWER2: " + part2(numberMap, x, y));
		// 1169

	}

	private static String part1(HashMap<Point, String> antennaMap, int maxX, int maxY) {

		return getAntinodes(antennaMap, maxX, maxY, false) + "";

	}

	private static String part2(HashMap<Point, String> antennaMap, int maxX, int maxY) {
		return getAntinodes(antennaMap, maxX, maxY, true) + "";

	}

	private static String getAntinodes(HashMap<Point, String> antennaMap, int maxX, int maxY,
			boolean allowRepetitions) {

		HashSet<Point> antinodes = new HashSet<>();

		for (Map.Entry<Point, String> entry : antennaMap.entrySet()) {
			Point point = entry.getKey();
			String val = entry.getValue();

			if (!val.equals(".")) {
				Map<Point, String> filtered = antennaMap.entrySet().stream()
						.filter(a -> a.getValue().equals(val) && a.getKey() != point)
						.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));

				for (Map.Entry<Point, String> pairEntry : filtered.entrySet()) {
					Point pairPoint = pairEntry.getKey();
					
					int distanceX = Math.abs(pairPoint.x - point.x);
					int distanceY = Math.abs(pairPoint.y - point.y);

					Point antinodePoint = new Point(point.x, point.y);

					// add self in case of other antennas present
					if (allowRepetitions)
						antinodes.add(antinodePoint);

					int multiplicator = 1;
					do {

						int antinodeX = -1;
						int antinodeY = -1;

						if (pairPoint.x > point.x) {
							antinodeX = point.x - distanceX * multiplicator;
						} else {
							antinodeX = point.x + distanceX * multiplicator;
						}

						if (pairPoint.y > point.y) {
							antinodeY = point.y - distanceY * multiplicator;
						} else {
							antinodeY = point.y + distanceY * multiplicator;
						}

						antinodePoint = new Point(antinodeX, antinodeY);

						if (antennaMap.containsKey(antinodePoint)) {
							// if within the map
							antinodes.add(antinodePoint);
						} else {
							// point outside of the map
						}
						multiplicator++;
					} while (antennaMap.containsKey(antinodePoint) && allowRepetitions);
				}
			}
		}
		return antinodes.size() + "";
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
