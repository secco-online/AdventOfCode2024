package co.m16mb.secco.advent2024;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Advent17 {

	private static final String filenamePath = "files/file17.txt";

	public static void main(String[] args) throws Exception {

		// reading the input file
		String fileContents = readFileAsString(filenamePath);

		int lineNumber = 0;
		
		int registerA = 0;
		int registerB = 0;
		int registerC = 0;
		int registerApart2 = 0;
		int registerBpart2 = 0;
		int registerCpart2 = 0;
		String program = "";

		for (String line : fileContents.split("\\r?\\n")) {
			if (lineNumber != 3) {
				String data = line.substring(line.indexOf(": ") + 2);
				switch (lineNumber) {
				case 0:
					registerA = Integer.parseInt(data);
					registerApart2 = registerA;
					break;
				case 1:
					registerB = Integer.parseInt(data);
					registerBpart2 = registerB;
					break;
				case 2:
					registerC = Integer.parseInt(data);
					registerCpart2 = registerC;
					break;
				case 4:
					program = data;
					break;
				}
			}
			lineNumber++;
		}

		System.out.println("ANSWER1: " + part1(registerA, registerB, registerC, program));
		// 3,1,5,3,7,4,2,7,5

		//System.out.println("ANSWER2: " + part2(registerApart2, registerBpart2, registerCpart2, program));
		// TODO checked for all positive integer values and does not work.....

	}

	private static String part1(int registerA, int registerB, int registerC, String program) {
		return runPRogram(registerA, registerB, registerC, program, false);
	}

	private static String part2(int registerA, int registerB, int registerC, String program) {
		int regA = 0;
		while (true) {
			if (regA % 1000000 == 0)
				System.out.println("regA " + regA);
			if (program.equals(runPRogram(regA, registerB, registerC, program, true))) {
				System.out.println(program);
				return regA + "";

			}
			regA++;
		}
	}

	private static String runPRogram(int registerA, int registerB, int registerC, String program, boolean part2) {
		String programOutput = "";

		if(registerB!= 0 || registerC!=0) System.err.println(registerB + " " + registerC);
		
		String[] instructions = program.split(",");

		for (int i = 0; i < instructions.length; i += 2) {

			int instruction = Integer.parseInt(instructions[i]);
			int operand = Integer.parseInt(instructions[i + 1]);
			// System.out.println("Instr: " + instruction + " operand " + operand);

			int operandValue = Integer.MIN_VALUE;
			switch (operand) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 7:
				operandValue = operand;
				break;
			case 4:
				operandValue = registerA;
				break;
			case 5:
				operandValue = registerB;
				break;
			case 6:
				operandValue = registerC;
				break;
			case 8:
				System.err.println("ERROR 8");
				break;
			case 9:
				System.err.println("ERROR 9");
				break;
			}
			// System.out.println("operandValue " + operandValue);

			// long instructionResult = Long.MIN_VALUE;

			switch (instruction) {
			case 0:
				registerA = (int) (registerA / Math.pow(2, operandValue));
				break;
			case 1:
				// XOR
				registerB = registerB ^ operand;
				break;
			case 2:
				registerB = operandValue % 8;
				break;
			case 3:
				if (registerA != 0) {
					// jump but do not increase afterwards
					i = operand - 2;
				}
				//System.out.println("Op 3 " + registerA + " new i " + i );
				break;
			case 4:
				// XOR
				registerB = registerB ^ registerC;
				break;
			case 5:
				programOutput += (operandValue % 8) + ",";
				if (part2 && !(program + ",").startsWith(programOutput)) {
					// output is already different, do not continue for this input of register A
					return "";
				}
				break;
			case 6:
				registerB = (int) (registerA / Math.pow(2, operandValue));
				break;
			case 7:
				registerC = (int) (registerA / Math.pow(2, operandValue));
				break;
			case 8:
				System.err.println("ERROR 8");
				break;
			case 9:
				System.err.println("ERROR 9");
				break;
			}

		}
		return programOutput.substring(0, programOutput.length() - 1);
	}

	public static String readFileAsString(String fileName) throws Exception {
		String data = "";
		data = new String(Files.readAllBytes(Paths.get(fileName)));
		System.out.println("Filesize: " + data.length());
		return data;
	}

}
