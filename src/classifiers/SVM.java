package classifiers;

import java.io.BufferedReader;
//import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;
import utils.Utils;
import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;

public class SVM extends Classifier{

	svm_parameter param;
	private svm_model learner = null;
	//private String input_file_name; // set by parse_command_line

	public void setLoation(String location){
		this.model = location;
	}
	
	public void build(String dataset) throws IOException {
		// default values
		param = new svm_parameter();
		param.svm_type = svm_parameter.C_SVC;
		param.kernel_type = svm_parameter.RBF;
		param.degree = 3;
		param.gamma = 0; // 1/num_features
		param.coef0 = 0;
		param.nu = 0.5;
		param.cache_size = 100;
		param.C = 1;
		param.eps = 1e-3;
		param.p = 0.1;
		param.shrinking = 1;
		param.probability = 0;
		param.nr_weight = 0;
		param.weight_label = new int[0];
		param.weight = new double[0];
		
		if(this.model == null)
			this.model = dataset.split("\\.")[0];
		svm_problem prob = read_problem(dataset);		
		learner = svm.svm_train(prob, param);
	    model = dataset.split("\\.")[0] + ".svm"; 
		svm.svm_save_model(model, learner);
		
	}

	public String classifyInstance(String instance) {
		// predict_probability = 1
		// int svm_type=svm.svm_get_svm_type(learner);
		int nr_class = svm.svm_get_nr_class(learner);
		double[] prob_estimates = null;

		int[] labels = new int[nr_class];
		svm.svm_get_labels(learner, labels);
		prob_estimates = new double[nr_class];

		StringTokenizer st = new StringTokenizer(instance, " \t\n\r\f:");

	    double target = Double.parseDouble(st.nextToken());// Make the token move one
		int m = st.countTokens() / 2;
		svm_node[] x = new svm_node[m];
		for (int j = 0; j < m; j++) {
			x[j] = new svm_node();
			x[j].index = Integer.parseInt(st.nextToken());
			x[j].value = Double.parseDouble(st.nextToken());
		}

		double v = svm.svm_predict(learner, x);
		int iv = (int) v;
		String predicted_value = Integer.toString(iv);

		return predicted_value;
	}
	
	public String[] classifyData(String dataset) throws Exception {
		// int predict_probability = 0; // Sth will be different, if
		// predict_probability = 1
		BufferedReader input = new BufferedReader(new FileReader(dataset));
		try {
			// int svm_type=svm.svm_get_svm_type(learner);
			int svm_type=svm.svm_get_svm_type(learner);
			int nr_class=svm.svm_get_nr_class(learner);
			double[] prob_estimates = null;

			int[] labels = new int[nr_class];
			svm.svm_get_labels(learner, labels);
			prob_estimates = new double[nr_class];

			String[] predicted_values = new String[Utils
					.totalInstances(dataset)];
			int index = 0;

			while (input.ready()) {
				String line = input.readLine();
				StringTokenizer st = new StringTokenizer(line, " \t\n\r\f:");
				double target = Double.parseDouble(st.nextToken()); // Make the token move one 
				int m = st.countTokens() / 2;
				svm_node[] x = new svm_node[m];
				for (int j = 0; j < m; j++) {
					x[j] = new svm_node();
					x[j].index = Integer.parseInt(st.nextToken());
					x[j].value = Double.parseDouble(st.nextToken());
				}

				double v = svm.svm_predict(learner, x);
				int iv = (int) v;
//				System.out.println(v); // debug
				predicted_values[index++] = Integer.toString(iv);
			}
			return predicted_values;
		} finally {
			input.close();
		}
	}

	public SVM copy(){
		SVM copy = new SVM();	
		copy.param = this.param;
		copy.learner = this.learner;
		copy.model = this.model;
		return copy;
	}
	
	
	// read in a problem (in svmlight format)
	private svm_problem read_problem(String dataset) throws IOException {
		svm_problem prob = null;
		BufferedReader fp = new BufferedReader(new FileReader(dataset));
		Vector<Double> vy = new Vector<Double>();
		Vector<svm_node[]> vx = new Vector<svm_node[]>();
		int max_index = 0;

		while (true) {
			String line = fp.readLine();
			if (line == null)
				break;

			StringTokenizer st = new StringTokenizer(line, " \t\n\r\f:");

			vy.addElement(Double.valueOf(st.nextToken()));
			int m = st.countTokens() / 2;
			svm_node[] x = new svm_node[m];
			for (int j = 0; j < m; j++) {
				x[j] = new svm_node();
				x[j].index = Integer.parseInt(st.nextToken());
				x[j].value = Double.valueOf(st.nextToken());
			}
			if (m > 0)
				max_index = Math.max(max_index, x[m - 1].index);
			vx.addElement(x);
		}

		prob = new svm_problem();
		prob.l = vy.size();
		prob.x = new svm_node[prob.l][];
		for (int i = 0; i < prob.l; i++)
			prob.x[i] = vx.elementAt(i);
		prob.y = new double[prob.l];
		for (int i = 0; i < prob.l; i++)
			prob.y[i] = vy.elementAt(i);

		if (param.gamma == 0 && max_index > 0)
			param.gamma = 1.0 / max_index;

		if (param.kernel_type == svm_parameter.PRECOMPUTED)
			for (int i = 0; i < prob.l; i++) {
				if (prob.x[i][0].index != 0) {
					System.err
							.print("Wrong kernel matrix: first column must be 0:sample_serial_number\n");
					System.exit(1);
				}
				if ((int) prob.x[i][0].value <= 0
						|| (int) prob.x[i][0].value > max_index) {
					System.err
							.print("Wrong input format: sample_serial_number out of range\n");
					System.exit(1);
				}
			}

		fp.close();
		
		return prob;
	}
}
