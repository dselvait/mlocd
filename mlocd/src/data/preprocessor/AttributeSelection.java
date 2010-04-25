package data.preprocessor;
import java.io.File;

import weka.attributeSelection.BestFirst;
import weka.attributeSelection.CfsSubsetEval;
//import weka.attributeSelection.InfoGainAttributeEval;
import weka.core.*;
import weka.core.converters.*;

public class AttributeSelection {

	private Instances instances = null;
	
	public int[] recommandAttributes(String dataset) throws Exception{
		File file = new File(dataset);
		C45Loader loader = new C45Loader();
		loader.setSource(file);
		instances = loader.getDataSet();
		//GainRatioAttributeEval eval = new GainRatioAttributeEval();
		CfsSubsetEval eval = new CfsSubsetEval();
		eval.buildEvaluator(instances);
		BestFirst bf = new BestFirst();
		int[] output = bf.search(eval,instances);
		for(int i = 0; i < output.length; ++i)
			output[i] = output[i] + 1;
		return output;
	}
	
}
