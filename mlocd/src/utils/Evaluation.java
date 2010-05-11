package utils;

import classifiers.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Evaluation {

	private static String seperator = ",";
	// If 1 then get from class at end, else if -1 get at head
	private static int class_index = 1;

	public static boolean esitmate(Classifier classifier, String instance)
			throws Exception {
		config(classifier);
		String hypothesis = classifier.classifyInstance(instance);
		String label = (class_index == 1) ? getClass(instance)
				: getClassAtHead(instance);
		return hypothesis.equals(label);
	}

	public static boolean[] testClassifier(Classifier classifier, String testset)
			throws IOException {
		config(classifier);
		boolean[] evaluations = null;
		BufferedReader in = new BufferedReader(new FileReader(testset));

		try {
			String[] results = classifier.classifyData(testset);
			evaluations = new boolean[Utils.totalInstances(testset)];
			int i = 0;
			while (in.ready()) {
				String instance = in.readLine();
				String label = (class_index == 1) ? getClass(instance)
						: getClassAtHead(instance);
				evaluations[i] = (results[i].equals(label));
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
			// System.exit(1);
		} finally {
			in.close();
		}
		return evaluations;
	}

	public static double accuracy(Classifier classifier, String testset)
			throws Exception {
		config(classifier);
		BufferedReader in = new BufferedReader(new FileReader(testset));
		// double accuracy = 0.0;
		int correct = 0;
		int incorrect = 0;

		try {
			String[] results = classifier.classifyData(testset);
			int i = 0;
			while (in.ready()) {
				String instance = in.readLine();
				String label = (class_index == 1) ? getClass(instance)
						: getClassAtHead(instance);
				if (results[i++].equals(label))
					correct++;
				else
					incorrect++;
			}
		} finally {
			in.close();
		}
		return (double) correct / (correct + incorrect);
	}

	public static double accuracy(String testset, String[] labels)
			throws IOException {

		BufferedReader in = new BufferedReader(new FileReader(testset));
		int correct = 0;
		int incorrect = 0;

		try {
			int i = 0;
			while (in.ready()) {
				String instance = in.readLine();
				String label = (class_index == 1) ? getClass(instance)
						: getClassAtHead(instance);
				if (labels[i++].equals(label))
					correct++;
				else
					incorrect++;
			}
		} finally {
			in.close();
		}
		return (double) correct / (correct + incorrect);
	}

	public static double errorRate(String testset, String[] labels)
			throws IOException {

		BufferedReader in = new BufferedReader(new FileReader(testset));
		int correct = 0;
		int incorrect = 0;

		try {
			int i = 0;
			while (in.ready()) {
				String instance = in.readLine();
				String label = (class_index == 1) ? getClass(instance)
						: getClassAtHead(instance);
				if (labels[i++].equals(label))
					correct++;
				else
					incorrect++;
			}
		} finally {
			in.close();
		}
		return (double) incorrect / (correct + incorrect);
	}

	public static boolean[] check(String[] labels, String dataset)
			throws Exception {

		BufferedReader in = new BufferedReader(new FileReader(dataset));
		boolean[] evaluation = new boolean[Utils.totalInstances(dataset)];
		int i = 0;
		while (in.ready()) {
			String instance = in.readLine();
			String label = (class_index == 1) ? getClass(instance)
					: getClassAtHead(instance);
			evaluation[i] = (labels[i].equals(label));
			i++;
		}

		return evaluation;
	}

	public static double jacobCoeffcient(boolean[] estimates1,
			boolean[] estimates2) {
		double value = 0.0;
		if (estimates1.length != estimates2.length)
			throw new IllegalArgumentException();
		int f1 = 0;
		int f11 = 0;
		for (int i = 0; i < estimates1.length; i++) {
			if (estimates2[i] == true) {
				f1++;
				if (estimates1[i] == true) // Given both the estimates are equal
											// to true, then f11 add 1
					f11++;
			}
		}
		value = (double) f11 / f1;
		return value;
	}

	public static void config(Classifier aClassifier) {
		if (aClassifier instanceof classifiers.SVM) {
			System.out.println("Testing SVM classifier.");
			seperator = " ";
			class_index = -1;
		} else {
			seperator = ",";
			class_index = 1;
		}
	}

	private static String getClass(String line) {
		String[] values = line.split(seperator);
		String label = values[values.length - 1];
		return label;
	}

	private static String getClassAtHead(String line) {
		String[] values = line.split(seperator);
		String label = values[0];
		return label;
	}

	private static String getClass(String line, String delimiter, int index) {
		String[] values = line.split(delimiter);
		String label = values[index + 1];
		return label;
	}

}
