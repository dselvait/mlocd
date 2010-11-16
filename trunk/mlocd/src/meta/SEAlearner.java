package meta;

import classifiers.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import utils.*;

import java.io.FileNotFoundException;
import java.io.IOException;

public class SEAlearner extends Classifier {

	private HashMap<Classifier, Double> classifierMap = new HashMap<Classifier, Double>();
	private String algorithm;
	private String location = null;

	public boolean surfixName() {
		return false;
	}

	public String getSurfixName() {
		return null;
	}

	public SEAlearner(Classifier learner) {
		this.algorithm = learner.getClass().getName();
	}

	public void setClassifierWeight(Classifier classifier, Double weight) {
		classifierMap.put(classifier, weight);
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public double getClassifierWeight(Classifier classifier) {
		Double d = (Double) classifierMap.get(classifier);
		return d;
	}

	public void addClassifier(Classifier classifier) {
		classifierMap.put(classifier, Double.valueOf(0.1));
	}

	public void listClassifiers() {
		Iterator i = classifierMap.entrySet().iterator();
		while (i.hasNext()) {
			Map.Entry me = (Map.Entry) i.next();
			System.out.println(me.getKey() + " : " + me.getValue());
		}
	}

	/**
	 * It need to generate a couple of instances of a specified classifier.
	 * Classifier.build
	 * 
	 */
	public void build(String[] datasets) throws Exception {
		try {
			initEnvironment();
			// int index = 0;
			for (String dataset : datasets) {
				// int num = Utils.totalInstances(dataset);
				// double[] weights = initDataWeights(num);
				// Process
				// Utils.resampleWithWeights(dataset, weights, sampled);
				Iterator<Map.Entry<Classifier, Double>> i = classifierMap
						.entrySet().iterator();
				double min = Double.MAX_VALUE;
				Classifier outdate = null;
				while (i.hasNext()) {
					Map.Entry<Classifier, Double> me = (Map.Entry<Classifier, Double>) i
							.next();
					Classifier blearner = me.getKey();
					boolean[] eval = utils.Evaluation.testClassifier(blearner,
							dataset);
					double weight = updateClassifierWeight(eval);
					classifierMap.put(blearner, weight);
					if( weight < min ) {
						min = weight;
						outdate = blearner;
					}
				}
					// Remove the classifiers give low performance
					if (classifierMap.size() > 3 && min < 0.20) {
								classifierMap.remove(outdate);
					}
					Classifier learner = getInstance();
					learner.build(dataset);
					boolean[] evaluations = utils.Evaluation.testClassifier(
							learner, dataset);
					double cweight = updateClassifierWeight(evaluations);
					classifierMap.put(learner, cweight);
				}
		} catch (Exception e) {
			cleanup();
			throw e;
		}
	}

	public void build(String dataset) {
		try {
			int times = 3;
			initEnvironment();
			String name = dataset.split("\\.")[0];
			boolean c45TypeFile = new File(name + ".names").exists();
			if (c45TypeFile)
				FileWorker.cp(name + ".names", this.location
						+ "/sampleData.names");
			int num = Utils.totalInstances(dataset);
			double[] weights = initDataWeights(num);
			// Processing
			for (int i = 1; i < times + 1; i++) {
				String sampled = this.location + "/sampleData.data";
				Utils.sampleTo(dataset, weights, sampled);
				Classifier learner = getInstance();
				learner.build(sampled);
				boolean[] evaluations = utils.Evaluation.testClassifier(
						learner, dataset);
				Classifier trained = learner.copy();
				// rename the trained weak learner
				String newname = this.location + "/classifier"
						+ Integer.toString(i);
				rename(trained, newname);
				updateDataWeights(weights, evaluations);
				// if(i==3) // debug code
				// for(double d:weights) System.out.println(d); //debug code
				double cweight = updateClassifierWeight(evaluations);
				classifierMap.put(trained, cweight);
			}
		} catch (Exception e) {
			e.printStackTrace();
			cleanup();
			System.exit(1);
		}
	}

	public String classifyInstance(String instance) throws Exception {
		String hypothesis = ""; // The return value

		HashMap<String, Double> listResults = new HashMap<String, Double>();

		Iterator classifierIterator = classifierMap.entrySet().iterator();

		while (classifierIterator.hasNext()) {
			Map.Entry me = (Map.Entry) classifierIterator.next();
			Classifier aLearner = (Classifier) me.getKey();
			String result = aLearner.classifyInstance(instance);
			if (!listResults.containsKey(result)) {
				double value = (Double) me.getValue();
				listResults.put(result, value);
			} else {
				double newValue = listResults.get(result)
						+ (Double) me.getValue();
				listResults.put(result, newValue);
			}
		}

		Iterator iterator = listResults.entrySet().iterator();
		double max = 0;
		Map.Entry me = (Map.Entry) iterator.next();
		max = (Double) me.getValue();
		hypothesis = (String) me.getKey();

		while (iterator.hasNext()) {
			me = (Map.Entry) iterator.next();
			double cur = (Double) me.getValue();
			if (max < cur) {
				cur = max;
				hypothesis = (String) me.getKey();
			}
		}

		return hypothesis;
	}

	public String[] classifyData(String dataset) throws Exception {
		String[] results = null;
		BufferedReader in = new BufferedReader(new FileReader(dataset));
		try {
			results = new String[Utils.totalInstances(dataset)];
			int i = 0;
			while (in.ready()) {
				String instance = in.readLine();
				results[i++] = classifyInstance(instance);
			}
		} finally {
			in.close();
		}
		return results;
	}

	public SEAlearner copy() {
		Classifier learner = getInstance();
		SEAlearner cp = new SEAlearner(learner);
		cp.location = this.location;
		return cp;
	}

	private Classifier getInstance() {
		try {
			Classifier learner = (Classifier) Class.forName(this.algorithm)
					.newInstance();
			return learner;
		} catch (Exception e) {
			System.err.println("Can't retrieve the given algorithm.");
			cleanup();
			System.exit(1);
		}
		return null;
	}

	private void initEnvironment() {
		if (this.location == null)
			this.location = System.getProperty("user.dir") + "/"
					+ "BoostingClassifier";
		else
			this.location = this.location + "/" + "BoostingClassifier";
		File dir = new File(this.location);
		String location = this.location;
		int i = 1;
		while (dir.exists()) {
			this.location = location + Integer.toString(i++);
			dir = new File(this.location);
		}
		dir.mkdir();
	}

	private double[] initDataWeights(int size) {
		double weight = (double) 1 / size;
		double[] weights = new double[size];
		for (int i = 0; i < weights.length; ++i)
			weights[i] = weight;
		return weights;
	}

	private void updateDataWeights(double[] weights, boolean[] list) {
		try {
			int i = 0;
			double sumError = 0.0;
			for (boolean b : list) {
				if (!b)
					sumError += weights[i];
				i++;
			}
			double m = sumError / (1 - sumError);
			i = 0;
			for (boolean b : list) {
				if (b)
					weights[i] = m * weights[i];
				i++;
			}
		} catch (IndexOutOfBoundsException e) {
			// throw new
			// IllegalArgumentException("The size of weights vctor is incorrect");
			e.printStackTrace();
		}
	}

	private double updateClassifierWeight(boolean[] list) {
		int i = 0;
		double sumError = 0.0;
		for (boolean b : list) {
			if (!b)
				sumError++;
		}
		double we = sumError / list.length;
		double weight = Math.log((double) 1 - we / we);
		return weight;
	}

	static private void rename(Classifier classifier, String newname)
			throws FileNotFoundException {
		int i = 0;
		boolean success = false;
		while (i++ < 1000) {
			try {
				classifier.reLocation(newname);
				success = true;
				break; // If the FileNotFoundException is not thrown, then the
				// skip the loop
			} catch (FileNotFoundException e) {
				try {
					System.out.println("Waiting for gernating a weak learner");
					Thread.sleep(1000); // sleep for 1 seconds to give time to
					// gernerate the classifier
					continue;
				} catch (InterruptedException ie) {
				}
			}
		}
		if (!success)
			throw new FileNotFoundException("Fail to generate the weak learner");
	}

	// static void rename(Classifier classifier, String newname)
	// throws FileNotFoundException{
	// classifier.reLocation(newname);
	// }

	private void cleanup() {
//		try {
//			File dir = new File(this.location);
//			for (File f : dir.listFiles())
//				f.delete();
//			dir.delete();
//			System.out.println("The Boosting environment has been cleaned up.");
//		} catch (Exception e) {
//			System.err
//					.println("Fail to clean up the Boosting classifier environment.");
//			e.printStackTrace();
//			// System.exit(1);
//		}
	}

}
