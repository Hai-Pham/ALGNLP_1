package edu.berkeley.nlp.assignments.assign1.student;

import java.util.ArrayList;
import java.util.List;

import edu.berkeley.nlp.langmodel.EnglishWordIndexer;
import edu.berkeley.nlp.langmodel.LanguageModelFactory;
import edu.berkeley.nlp.langmodel.NgramLanguageModel;
import edu.berkeley.nlp.util.CollectionUtils;

public class LmFactory implements LanguageModelFactory
{

  /**
   * Returns a new NgramLanguageModel; this should be an instance of a class that you implement.
   * Please see edu.berkeley.nlp.langmodel.NgramLanguageModel for the interface specification.
   * 
   * @param trainingData
   */
	public NgramLanguageModel newLanguageModel(Iterable<List<String>> trainingData) {
//		return new NaiveTrigramLanguageModel(trainingData);
//		return new KNNaiveTrigramLanguageModel(trainingData);
//		return new KNTrigramLanguageModel(trainingData);
		return new KNTrigramLanguageModel2(trainingData);
	}
}
