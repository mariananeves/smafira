package dataMining;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import db.AssessmentDBWriter;

import properties.PropertiesHandler;
import properties.PropertiesHandler.PropertiesHanderMode;
import properties.PropertiesHandler.PropertiesIDs;

import tools.MapValueComparator;
import tools.ToolsUtil;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.SMO;
import weka.classifiers.lazy.LWL;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.rules.JRip;
import weka.classifiers.trees.J48;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.experiment.CrossValidationResultProducer;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class WekaClassifier {
//	source: https://weka.wikispaces.com/Use+WEKA+in+your+Java+code
	
	private String arffPath;
	private String resultPath;
	private String relevanceMappingPath;
	private String similarityMappingPath;
	private String animalTestMappingPath;
	private HashMap<String, String> relevanceMapping;
	private HashMap<String, String> similarityMapping;
	private HashMap<String, String> animalTestMapping;

	public WekaClassifier(){
		this.setArffPath();
	}
	
	public enum ClassifierType{
		SMO("rankSMO"), NAIVE_BAYES("rankBayes"), BAYES_NET("rankBayesNet"), J48("rankJ48"), JRIP("rankJRIP"), LWL("rankLWL");

		public String name;

		private ClassifierType(String name){
			this.name = name;
		}

		public ClassifierType getType(String name){
			if(name.equals(SMO.name))
				return SMO;
			else if(name.equals(NAIVE_BAYES.name))
				return NAIVE_BAYES;
			else if(name.equals(BAYES_NET.name))
				return BAYES_NET;
			else if(name.equals(J48.name))
				return J48;
			else if(name.equals(JRIP.name))
				return JRIP;
			else if(name.equals(LWL.name))
				return LWL;
			else
				return null;
		}
	};
	public enum ResultVariants{ALL_CLASSES, REL_AND_SIM, REL_OR_SIM, NEGATIVE};
	
	private void setArffPath(){
		PropertiesHandler ph = new PropertiesHandler(PropertiesHanderMode.ENTREZ);
		this.arffPath = ph.getPropertyValue(PropertiesIDs.PROJECT_PATH) + ph.getPropertyValue(PropertiesIDs.DATA_ARFF_PATH);
		this.resultPath = ph.getPropertyValue(PropertiesIDs.PROJECT_PATH) + ph.getPropertyValue(PropertiesIDs.DATA_RESULT_PATH);
	}
	
	private void setMappingPaths(String refDocPmid){
		this.relevanceMappingPath = this.arffPath + refDocPmid + "_classify_relevance_mapping.txt";
		this.similarityMappingPath = this.arffPath + refDocPmid + "_classify_similarity_mapping.txt";
		this.animalTestMappingPath = this.arffPath + refDocPmid + "_classify_animalTest_mapping.txt";
	}
	
	public Instances getData(String filename){
		Instances data = null;
		try {
			DataSource source = new DataSource(filename);
			data = source.getDataSet();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//			index of class attribute (set to last attribute)
		if(data.classIndex() == -1)
			data.setClassIndex(data.numAttributes()-1);
				
		return data;
	}
	
	
	public HashMap<String, WekaClassificationResultInstance> classifyWithFilter(Instances train, Instances test, Classifier classifier, Filter filter, String targetFilePath, HashMap<String, String> mapping){		
		HashMap<String, WekaClassificationResultInstance> resultMap = new HashMap<String, WekaClassificationResultInstance>();
		WekaClassificationResultInstance resultInstance;
		
		FilteredClassifier fc = new FilteredClassifier();
		fc.setFilter(filter);
		fc.setClassifier(classifier);
		String id, prediction, confidence;
		String output = "";
		
		
		try {
			fc.buildClassifier(train);
			for (int i = 0; i < test.numInstances(); i++) {
				   double pred = fc.classifyInstance(test.instance(i));
				   double[] dist = fc.distributionForInstance(test.instance(i));
				   id = "ID: " + test.instance(i).value(0);
//				   System.out.print(", actual: " + test.classAttribute().value((int) test.instance(i).classValue()));
				   prediction = "predicted: " + test.classAttribute().value((int) pred);
				   confidence = "confidence: ";
				   for(double distValue: dist){
					   confidence += distValue + ",";
				   }
				   output += id + "; " + prediction + "; " + confidence + "\n";				   

				   
				   resultInstance = new WekaClassificationResultInstance();
				   resultInstance.pmid = mapping.get(String.valueOf(test.instance(i).value(0)));
				   resultInstance.predictedClass = test.classAttribute().value((int) pred);
				   resultInstance.confidenceNegativeClass = dist[0];
				   resultInstance.confidencePositiveClass = dist[1];
				   resultMap.put(resultInstance.pmid, resultInstance);				   
			 }
			 ToolsUtil.saveText(targetFilePath, output.trim());
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		return resultMap;
		
	}
	
	public ArrayList<Integer> getSimilarDocsByRank(Instances train, Instances test, Classifier classifier, Filter filter){
		String similar = "1";
		FilteredClassifier fc = new FilteredClassifier();
		fc.setFilter(filter);
		fc.setClassifier(classifier);
		String prediction;
		Double id;
		ArrayList<Integer> similarDocsRanks = new ArrayList<Integer>();
		
		try {
			fc.buildClassifier(train);
			
			for (int i = 0; i < test.numInstances(); i++) {
				   double pred = fc.classifyInstance(test.instance(i));
				   id = test.instance(i).value(0);
//				   System.out.print(", actual: " + test.classAttribute().value((int) test.instance(i).classValue()));
				   prediction = test.classAttribute().value((int) pred);

				   if(prediction.equals(similar))
					   similarDocsRanks.add(id.intValue());
					   
			 }
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		FastVector rules = ((JRip)fc.getClassifier()).getRuleset();
		System.out.println("rules: " + rules.firstElement());
		
//		System.out.println("rules: \n\n");
//		while(rules.hasMoreElements()){
//			System.out.println(((JRip.RipperRule)rules.nextElement()).toString());
//		}
		
		return similarDocsRanks;
	}
	
	public void evaluationWithFilter(Instances train, Instances test, Classifier classifier, Filter filter, String targetFilePath){
		try {
			FilteredClassifier fc = new FilteredClassifier();
			fc.setFilter(filter);
			fc.setClassifier(classifier);
			
			fc.buildClassifier(train);
			
//			evaluation
			Evaluation eval = new Evaluation(train);
			 eval.evaluateModel(fc, test);
//			eval.crossValidateModel(fc, train, 10, new Random(1));
			 System.out.println(eval.toSummaryString("\nResults\n======\n", false));
			 System.out.println("precision: " + eval.precision(1));
			ToolsUtil.saveText(targetFilePath, eval.toSummaryString("\nResults\n======\n", false) +
					eval.toClassDetailsString("\nClassDetails\n======\n") +
					eval.toMatrixString("\n\nMatrix\n======\n"));
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	
	
//	public static void test(){
//		WekaClassifier wcl = new WekaClassifier();
////		String useCaseArffPath = "C:/Users/du/Projekte/SMAFIRA/git/data/arff/arff_UseCase5/";
//		String useCaseArffPath = "C:/Users/du/Projekte/SMAFIRA/git/data/arff/arff_test/";
////		String pmid = "10712494";
//		String pmid = "16192371";
////		Instances train = wcl.getData("C:/Users/du/Projekte/SMAFIRA/git/data/arff/arff_UseCase3a/assessedSetsCase1/16192371_test_completeAssessedSet_relevance_allAttributes.arff");
////		Instances test = wcl.getData("C:/Users/du/Projekte/SMAFIRA/git/data/arff/arff_UseCase3a/11932745_full_Testset_allAttributes_relevance.arff");
////		Instances train = wcl.getData("C:/Users/du/Projekte/SMAFIRA/git/data/arff/arff_UseCase3a/assessedSetsCase1/16192371_test_completeAssessedSet_similarity_allAttributes.arff");
////		Instances test = wcl.getData("C:/Users/du/Projekte/SMAFIRA/git/data/arff/arff_UseCase3a/11932745_full_Testset_allAttributes_similarity.arff");
//		
////		Instances trainAnimalTest = wcl.getData(useCaseArffPath + "assessedSetsCase1/16192371_test_completeAssessedSet_animalTest_allAttributes.arff");
////		Instances testAnimalTest = wcl.getData(useCaseArffPath + pmid + "_full_Testset_allAttributes_animalTest.arff");
////		Instances trainRelevance = wcl.getData(useCaseArffPath + "assessedSetsCase1/16192371_test_completeAssessedSet_relevance_allAttributes.arff");
////		Instances testRelevance = wcl.getData(useCaseArffPath + pmid + "_full_Testset_allAttributes_relevance.arff");
////		Instances trainSimilarity = wcl.getData(useCaseArffPath + "assessedSetsCase1/16192371_test_completeAssessedSet_similarity_allAttributes.arff");
////		Instances testSimilarity = wcl.getData(useCaseArffPath + pmid + "_full_Testset_allAttributes_similarity.arff");
//		
//		Instances trainAnimalTest = wcl.getData(useCaseArffPath + "assessedSetsCase1/16192371_test_completeAssessedSet_animalTest_allAttributes.arff");
//		Instances testAnimalTest = wcl.getData(useCaseArffPath + pmid + "_animalTest_test.arff");
//		Instances trainRelevance = wcl.getData(useCaseArffPath + "assessedSetsCase1/16192371_test_completeAssessedSet_relevance_allAttributes.arff");
//		Instances testRelevance = wcl.getData(useCaseArffPath + pmid + "_relevance_test.arff");
//		Instances trainSimilarity = wcl.getData(useCaseArffPath + "assessedSetsCase1/16192371_test_completeAssessedSet_similarity_allAttributes.arff");
//		Instances testSimilarity = wcl.getData(useCaseArffPath + pmid + "_similarity_test.arff");
//		
////		filter
//		StringToWordVector stwvFilter = new StringToWordVector();	
//		
////		classifier
//		J48 j48 = new J48();
//		j48.setUnpruned(true);
//		
//		JRip jrip = new JRip();
//		jrip.setUsePruning(false);
//		
//		NaiveBayes naiveBayes = new NaiveBayes();
//		naiveBayes.setUseKernelEstimator(true);
//		
//		BayesNet bayesNet = new BayesNet();
//
//		SMO smo = new SMO();
//		smo.setBuildLogisticModels(true);
//		
//		LWL lwl = new LWL();
//
//
//		String animalTestPath = useCaseArffPath + "predictions/animalTest/animalTest_";
//		String relevancePath = useCaseArffPath + "predictions/relevance/relevance_";
//		String similarityPath = useCaseArffPath + "predictions/similarity/similarity_";
//		
//		System.out.println("J48:");
//		wcl.classifyWithFilter(trainAnimalTest, testAnimalTest, j48, stwvFilter, animalTestPath + "J48.txt");
//		System.out.println("JRip:");
//		wcl.classifyWithFilter(trainAnimalTest, testAnimalTest, jrip, stwvFilter, animalTestPath + "JRip.txt");
//		System.out.println("NaiveBayes:");
//		wcl.classifyWithFilter(trainAnimalTest, testAnimalTest, naiveBayes, stwvFilter, animalTestPath + "NaiveBayes.txt");
//		System.out.println("BayesNet:");
//		wcl.classifyWithFilter(trainAnimalTest, testAnimalTest, bayesNet, stwvFilter, animalTestPath + "BayesNet.txt");
//		System.out.println("SMO:");
//		wcl.classifyWithFilter(trainAnimalTest, testAnimalTest, smo, stwvFilter, animalTestPath + "SMO.txt");
//		System.out.println("LWL:");
//		wcl.classifyWithFilter(trainAnimalTest, testAnimalTest, lwl, stwvFilter, animalTestPath + "LWL.txt");
//		
//		System.out.println("J48:");
//		wcl.classifyWithFilter(trainRelevance, testRelevance, j48, stwvFilter, relevancePath + "J48.txt");
//		System.out.println("JRip:");
//		wcl.classifyWithFilter(trainRelevance, testRelevance, jrip, stwvFilter, relevancePath + "JRip.txt");
//		System.out.println("NaiveBayes:");
//		wcl.classifyWithFilter(trainRelevance, testRelevance, naiveBayes, stwvFilter, relevancePath + "NaiveBayes.txt");
//		System.out.println("BayesNet:");
//		wcl.classifyWithFilter(trainRelevance, testRelevance, bayesNet, stwvFilter, relevancePath + "BayesNet.txt");
//		System.out.println("SMO:");
//		wcl.classifyWithFilter(trainRelevance, testRelevance, smo, stwvFilter, relevancePath + "SMO.txt");
//		System.out.println("LWL:");
//		wcl.classifyWithFilter(trainRelevance, testRelevance, lwl, stwvFilter, relevancePath + "LWL.txt");
//		
//		System.out.println("J48:");
//		wcl.classifyWithFilter(trainSimilarity, testSimilarity, j48, stwvFilter, similarityPath + "J48.txt");
//		System.out.println("JRip:");
//		wcl.classifyWithFilter(trainSimilarity, testSimilarity, jrip, stwvFilter, similarityPath + "JRip.txt");
//		System.out.println("NaiveBayes:");
//		wcl.classifyWithFilter(trainSimilarity, testSimilarity, naiveBayes, stwvFilter, similarityPath + "NaiveBayes.txt");
//		System.out.println("BayesNet:");
//		wcl.classifyWithFilter(trainSimilarity, testSimilarity, bayesNet, stwvFilter, similarityPath + "BayesNet.txt");
//		System.out.println("SMO:");
//		wcl.classifyWithFilter(trainSimilarity, testSimilarity, smo, stwvFilter, similarityPath + "SMO.txt");
//		System.out.println("LWL:");
//		wcl.classifyWithFilter(trainSimilarity, testSimilarity, lwl, stwvFilter, similarityPath + "LWL.txt");
//		
////		wcl.evaluationWithFilter(train, test, j48, stwvFilter);
////		wcl.evaluationWithFilter(train, test, jrip, stwvFilter);
////		wcl.evaluationWithFilter(train, test, naiveBayes, stwvFilter);
////		wcl.evaluationWithFilter(train, test, bayesNet, stwvFilter);
////		wcl.evaluationWithFilter(train, test, smo, stwvFilter);
////		wcl.evaluationWithFilter(train, test, lwl, stwvFilter);
//		
//	}
	
	public static void useCase3aJRip(){
		WekaClassifier wcl = new WekaClassifier();
		String useCaseArffPath = "C:/Users/du/Projekte/SMAFIRA/git/data/arff/arff_UseCase3a/";
		String pmid = "11932745";
		
		Instances trainSimilarity = wcl.getData(useCaseArffPath + "assessedSetsCase1/16192371_test_completeAssessedSet_similarity_allAttributes.arff");
		Instances testSimilarity = wcl.getData(useCaseArffPath + pmid + "_full_Testset_allAttributes_similarity.arff");
		
		
//		filter
		StringToWordVector stwvFilter = new StringToWordVector();
		
//		classifier
		JRip jrip = new JRip();
		jrip.setUsePruning(false);
		
		ArrayList<Integer> similarityDocRanks = wcl.getSimilarDocsByRank(trainSimilarity, testSimilarity, jrip, stwvFilter);
		for(int rank: similarityDocRanks){
			System.out.println(rank + ",");
		}
	}
	
	

	
	
	private ArrayList<String> doReranking(String refDocPmid, ClassifierType classifierType){
		Instances trainingAnimalTest = getData(arffPath + refDocPmid + "_training_animalTest.arff");
		Instances classifyAnimalTest = getData(arffPath + refDocPmid + "_classify_animalTest.arff");
		Instances trainingRelevance = getData(arffPath + refDocPmid + "_training_relevance.arff");
		Instances classifyRelevance = getData(arffPath + refDocPmid + "_classify_relevance.arff");
		Instances trainingSimilarity = getData(arffPath + refDocPmid + "_training_similarity.arff");
		Instances classifySimilarity = getData(arffPath + refDocPmid + "_classify_similarity.arff");
		
//		filter
		StringToWordVector stwvFilter = new StringToWordVector();	
		
//		classifier
		Classifier classifier = getClassifier(classifierType);

		String animalTestPath = resultPath + "predictions/" + refDocPmid + "animalTest_";
		String relevancePath = resultPath + "predictions/" + refDocPmid + "relevance_";
		String similarityPath = resultPath + "predictions/" + refDocPmid + "similarity_";
		
		HashMap<String, WekaClassificationResultInstance> animalTestResults = classifyWithFilter(trainingAnimalTest, classifyAnimalTest, classifier, stwvFilter, animalTestPath + classifier.getClass().getName() + ".txt", this.animalTestMapping);		
		HashMap<String, WekaClassificationResultInstance> relevanceResults = classifyWithFilter(trainingRelevance, classifyRelevance, classifier, stwvFilter, relevancePath + classifier.getClass().getName() + ".txt", this.relevanceMapping);		
		HashMap<String, WekaClassificationResultInstance> similaritytResults = classifyWithFilter(trainingSimilarity, classifySimilarity, classifier, stwvFilter, similarityPath + classifier.getClass().getName() + ".txt", this.similarityMapping);
		
		
		ArrayList<String> newSorting = getNewSorting(animalTestResults, relevanceResults, similaritytResults);
		
//		for(int i = 0; i < newSorting.size(); i++){
//			System.out.println((i + 1) + ": " + newSorting.get(i));
//		}
		
//		wcl.evaluationWithFilter(train, test, j48, stwvFilter);
//		wcl.evaluationWithFilter(train, test, jrip, stwvFilter);
//		wcl.evaluationWithFilter(train, test, naiveBayes, stwvFilter);
//		wcl.evaluationWithFilter(train, test, bayesNet, stwvFilter);
//		wcl.evaluationWithFilter(train, test, smo, stwvFilter);
//		wcl.evaluationWithFilter(train, test, lwl, stwvFilter);
		
		
		return newSorting;
	}
	
	
	private ArrayList<String> getNewSorting(HashMap<String, WekaClassificationResultInstance> animalTestResults, HashMap<String, WekaClassificationResultInstance> relevanceResults, HashMap<String, WekaClassificationResultInstance> similarityResults){
		ArrayList<String> newSorting = new ArrayList<String>();
		HashMap<String, Double> positivePredictions_allClasses = new HashMap<String, Double>();
		HashMap<String, Double> positivePredictions_relevanceAndSimilarity = new HashMap<String, Double>();
		HashMap<String, Double> positivePredictions_relevanceOrSimilarity = new HashMap<String, Double>();
		HashMap<String, Double> negativePredictions = new HashMap<String, Double>();
		
		Set<String> pmidList = animalTestResults.keySet();
		
		double animalTestConfidence, relevanceConfidence, similarityConfidence;
		WekaClassificationResultInstance animalTestResult, relevanceResult, similarityResult;
		ResultVariants resultVariant;
		
		for(String pmid: pmidList){
			animalTestResult = animalTestResults.get(pmid);
			relevanceResult = relevanceResults.get(pmid);
			similarityResult = similarityResults.get(pmid);
			
			resultVariant = getResultVariant(animalTestResult.predictedClass, relevanceResult.predictedClass, similarityResult.predictedClass);
			animalTestConfidence = animalTestResult.confidenceNegativeClass;
			relevanceConfidence = relevanceResult.confidencePositiveClass;
			similarityConfidence = similarityResult.confidencePositiveClass;
			
			switch(resultVariant){
			case ALL_CLASSES:
				positivePredictions_allClasses.put(pmid, getMeanConfidenceValue(new double[]{animalTestConfidence, relevanceConfidence, similarityConfidence}));
				break;
			case REL_AND_SIM:
				positivePredictions_relevanceAndSimilarity.put(pmid, getMeanConfidenceValue(new double[]{relevanceConfidence, similarityConfidence}));
				break;
			case REL_OR_SIM:
				positivePredictions_relevanceOrSimilarity.put(pmid, isRelevant(relevanceResult.predictedClass) ? relevanceConfidence: similarityConfidence);
				break;
			case NEGATIVE:
				negativePredictions.put(pmid, getMeanConfidenceValue(new double[]{animalTestResult.confidencePositiveClass, relevanceResult.confidenceNegativeClass,
						similarityResult.confidenceNegativeClass}));
			}
		}
		
		newSorting.addAll(sort(positivePredictions_allClasses));
		newSorting.addAll(sort(positivePredictions_relevanceAndSimilarity));
		newSorting.addAll(sort(positivePredictions_relevanceOrSimilarity));
		newSorting.addAll(sort(negativePredictions));
		
		return newSorting;
	}
	
	private ArrayList<String> sort(HashMap<String, Double> toSort){
		ArrayList<String> pmidsSorted = new ArrayList<String>();
		
		MapValueComparator mvc = new MapValueComparator(toSort);
		TreeMap<String, Double> sortedMap = new TreeMap<String, Double>(mvc);
		
		sortedMap.putAll(toSort);
		
		pmidsSorted.addAll(sortedMap.keySet());
		
		return pmidsSorted;
	}
	
	private double getMeanConfidenceValue(double[] values){
		double meanValue = 0;
		
		for(double value: values){
			meanValue += value;
		}
		
		meanValue /= values.length;
		
		return meanValue;
	}
	
	
	private ResultVariants getResultVariant(String animalTestClass, String relevanceClass, String similarityClass){		
		if(!isAnimalTest(animalTestClass) && isRelevant(relevanceClass) && isSimilar(similarityClass))
			return ResultVariants.ALL_CLASSES;
		else if(isRelevant(relevanceClass) && isSimilar(similarityClass))
			return ResultVariants.REL_AND_SIM;
		else if(isRelevant(relevanceClass) || isSimilar(similarityClass))
			return ResultVariants.REL_OR_SIM;
		else
			return ResultVariants.NEGATIVE;
	}
	
	private boolean isAnimalTest(String animalTestClass){
		String animalTestPositiveClass = "1";
		
		return animalTestClass.equals(animalTestPositiveClass);
	}
	
	private boolean isRelevant(String relevanceClass){
		String relevancePositiveClass = "4";
		
		return relevanceClass.equals(relevancePositiveClass);
	}
	
	private boolean isSimilar(String similarityClass){
		String similarityPositiveClass = "1";
		
		return similarityClass.equals(similarityPositiveClass);
	}
	
	private Classifier getClassifier(ClassifierType classifierType){
		switch(classifierType){
		case J48: 
			J48 j48 = new J48();
			j48.setUnpruned(true);
			return j48;
		case JRIP: 
			JRip jrip = new JRip();
			jrip.setUsePruning(false);
			return jrip;
		case NAIVE_BAYES: 
			NaiveBayes naiveBayes = new NaiveBayes();
			naiveBayes.setUseKernelEstimator(true);
			return naiveBayes;
		case BAYES_NET:
			BayesNet bayesNet = new BayesNet();
			return bayesNet;
		case SMO:
			SMO smo = new SMO();
			smo.setBuildLogisticModels(true);
			return smo;
		case LWL:
			LWL lwl = new LWL();
			return lwl;
		default: return null;
		}
	}
	
	private void loadIDMapping(String refDocPmid){
		setMappingPaths(refDocPmid);
		this.relevanceMapping = new HashMap<String, String>();
		this.similarityMapping = new HashMap<String, String>();
		this.animalTestMapping = new HashMap<String, String>();
		
		readMapping(this.relevanceMapping, this.relevanceMappingPath);
		readMapping(this.similarityMapping, this.similarityMappingPath);
		readMapping(this.animalTestMapping, this.animalTestMappingPath);
		
//		Set<String> keys = this.relevanceMapping.keySet();
//		for(String mapKey: keys){
//			System.out.println(mapKey + ": " + this.relevanceMapping.get(mapKey));
//		}
	}
	
	private void readMapping(HashMap<String, String> idMapping, String mappingPath){
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(mappingPath)));
			
			String line;
			String instanceID, pmid;
			
			while((line = reader.readLine()) != null){
				if(!line.trim().isEmpty()){
					instanceID = line.substring(0, line.indexOf(":")).trim();
					pmid = line.substring(line.indexOf(":") + 1).trim();
					
					idMapping.put(instanceID, pmid);
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	public ArrayList<String> getReranking(String refDocPmid, ClassifierType classifierType, String rankColumnName){
		ArrayList<String> reranking = new ArrayList<String>();
		loadIDMapping(refDocPmid);
		ArrayList<String> newRanking = doReranking(refDocPmid, classifierType);
		AssessmentDBWriter dbWriter = new AssessmentDBWriter();
		
		int rank = 1;
		for(String resultDocPmid: newRanking){
			dbWriter.updateRankColumn(refDocPmid, resultDocPmid, rankColumnName, rank++);
		}
		
		return reranking;
	}
	
	public static void grailsIntegrationTest(){
		WekaClassifier classifier = new WekaClassifier();
		
		classifier.getReranking("16192371", ClassifierType.SMO, "ranksmo");
	}
	

	public static void main(String[] args) {
		grailsIntegrationTest();
//		test();
//		useCase3aJRip();
	}

	
	protected class WekaClassificationResultInstance{
		public String pmid;
		
		public String predictedClass;
		public double confidencePositiveClass;
		public double confidenceNegativeClass;
	}
}
