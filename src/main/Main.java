package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;
/**
 * Trying out Git for the first time and I am 
 * adding this comment for fun
 * /
public class Main {

	HashMap<String, Long> numberList = new HashMap<String, Long>();

	public static void main(String[] args) {
		new Main();
	}

	public Main() {

		populateNumberList();

		try {
			File inputFile = new File("src/main/input.txt");
			Scanner myScanner = new Scanner(inputFile);
			// ' ' OR '-'. Helps to seperate forty-five thirty-two etc.
			myScanner.useDelimiter("\\s+|-");

			long sum = 0;
			String currentNumber = "";

			while (myScanner.hasNext()) {
				// remove punctuation so forty. five, etc are removed
				String currentWord = removePunctuation(myScanner.next().toLowerCase());
				currentNumber = "";
				if (isStoredNumber(currentWord)) { // inside a number sentence
					currentNumber += currentWord + " ";
					while (myScanner.hasNext()) {
						// remove all punctuation
						currentWord = removePunctuation(myScanner.next().toLowerCase());
						if (isStoredNumber(currentWord) || currentWord.equals("and")) {
							currentNumber += currentWord + " ";
						} else {
							break;
						}
					}
				}

				if (currentNumber.length() > 0) {
					sum += getValue(currentNumber);
				}
			}
			System.out.println("total sum is " + sum);
			myScanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private long getValue(String currentNumber) {
		long sum = 0;
		Stack<String> numberStack = new Stack<String>();
		Scanner numberScanner = new Scanner(currentNumber);

		// populate the stack
		while (numberScanner.hasNext()) {
			String next = numberScanner.next();
			if (!next.equals("and")) {
				numberStack.push(next);
			}
		}

		long multiple = 1;
		while (!numberStack.isEmpty()) {
			String next = numberStack.pop();

			if (next.equals("hundred")) {
				if (numberStack.isEmpty()) {
					sum += 100 * multiple;
				} else {
					sum += numberList.get(numberStack.pop()) * multiple * 100;
				}
			} else if (isBaseNumber(next)) {
				if (numberStack.isEmpty()) {
					sum += numberList.get(next);
				} else {
					multiple = numberList.get(next);
				}
			} else {
				sum += numberList.get(next) * multiple;
			}
		}
		return sum;
	}

	/*
	 * A base number is 1,000 1,000,000 1,000,000,000 etc.
	 */
	private boolean isBaseNumber(String number) {
		return number.equals("thousand") || number.equals("million") || number.equals("billion");
	}

	private void populateNumberList() {
		try {
			File numberFile = new File("src/main/numberList.txt");
			Scanner numberScanner = new Scanner(numberFile);

			while (numberScanner.hasNextLine()) {
				String nextNumber = numberScanner.next();
				int value = numberScanner.nextInt();
				numberList.put(nextNumber, (long) value);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	private boolean isStoredNumber(String currentWord) {
		return numberList.containsKey(currentWord);
	}

	private String removePunctuation(String x) {
		return x.replaceAll("[\\[\\](){},.;!?<>%]", "");
	}

}
