package co.m16mb.secco.advent2024;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Advent14 {

	private static final String filenamePath = "files/file14.txt";

	public static void main(String[] args) throws Exception {

		// reading the input file
		String fileContents = readFileAsString(filenamePath);

		ArrayList<Robot> robotList = new ArrayList<>();

		Pattern pattern = Pattern.compile("p=([0-9]*),([0-9]*) v=(-?[0-9]*),(-?[0-9]*)");

		for (String row : fileContents.split("\\r?\\n")) {

			long positionX = 0;
			long positionY = 0;
			long velocityX = 0;
			long velocityY = 0;

			Matcher matcher = pattern.matcher(row);
			if (matcher.find()) {
				positionX = Long.parseLong(matcher.group(1));
				positionY = Long.parseLong(matcher.group(2));
				velocityX = Long.parseLong(matcher.group(3));
				velocityY = Long.parseLong(matcher.group(4));

			}
			Robot robot = new Robot(positionX, positionY, velocityX, velocityY);
			robotList.add(robot);
		}
		final int AREA_X = 101;// 11; // 101
		final int AREA_Y = 103;// 7; // 103

		System.out.println("ANSWER1: " + part1(robotList, AREA_X, AREA_Y));
		// 231782040

		System.out.println("ANSWER2: " + part2(robotList, AREA_X, AREA_Y));
		// 6475

	}

	private static String part1(ArrayList<Robot> robotList, int AREA_X, int AREA_Y) {

		int topLeft = 0;
		int topRight = 0;
		int bottomLeft = 0;
		int bottomRight = 0;

		int seconds = 100;
		for (Robot robot : robotList) {
			long newX = robot.velocityX * seconds + robot.positionX;
			newX = newX % AREA_X;
			if (newX < 0)
				newX += AREA_X;

			long newY = robot.velocityY * seconds + robot.positionY;
			newY = newY % AREA_Y;
			if (newY < 0)
				newY += AREA_Y;

			if (newX < (AREA_X - 1) / 2 && newY < (AREA_Y - 1) / 2)
				topLeft++;
			else if (newX > (AREA_X - 1) / 2 && newY < (AREA_Y - 1) / 2)
				topRight++;
			else if (newX < (AREA_X - 1) / 2 && newY > (AREA_Y - 1) / 2)
				bottomLeft++;
			else if (newX > (AREA_X - 1) / 2 && newY > (AREA_Y - 1) / 2)
				bottomRight++;

		}

		System.out.println("TL: " + topLeft);
		System.out.println("TR: " + topRight);
		System.out.println("BL: " + bottomLeft);
		System.out.println("BR: " + bottomRight);

		long sum = topLeft * topRight * bottomLeft * bottomRight;
		return sum + "";
	}

	private static String part2(ArrayList<Robot> robotList, int AREA_X, int AREA_Y) {

		for (int seconds = 0; seconds < 10000; seconds++) {

			HashMap<Point, Integer> map = new HashMap<>();
			for (int j = 0; j < AREA_Y; j++) {
				for (int i = 0; i < AREA_X; i++) {
					map.put(new Point(i, j), 0);
				}
			}

			boolean noOverlappingRobots = true;
			for (Robot robot : robotList) {
				long newX = robot.velocityX * seconds + robot.positionX;
				newX = newX % AREA_X;
				if (newX < 0)
					newX += AREA_X;

				long newY = robot.velocityY * seconds + robot.positionY;
				newY = newY % AREA_Y;
				if (newY < 0)
					newY += AREA_Y;

				Point position = new Point(newX, newY);
				int currentValue = map.get(position);
				if (currentValue > 0)
					noOverlappingRobots = false;
				map.put(position, currentValue + 1);

			}

			if (noOverlappingRobots) {
				System.out.println("NO OVERLAP SECONDS " + seconds);

				for (int j = 0; j < AREA_Y; j++) {
					for (int i = 0; i < AREA_X; i++) {
						System.out.print(map.get(new Point(i, j)));
					}
					System.out.println();
				}
				return seconds + "";
			}
		}
		return "NO RESULT";
	}

	private static record Robot(long positionX, long positionY, long velocityX, long velocityY) {
	};

	public static String readFileAsString(String fileName) throws Exception {
		String data = "";
		data = new String(Files.readAllBytes(Paths.get(fileName)));
		System.out.println("Filesize: " + data.length());
		return data;
	}

	private static record Point(long x, long y) {

	};

}
