package algo;

import info.Setting;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import structure.BipartiteGraph;
import structure.CooccFkt;
import util.MyBitSet;
import util.Text;

public class LevFDSM {

	// for general settings:
	public static int numberOfSampleGraphs = Setting.numberOfSampleGraphs;
	public static String inputFile = Setting.inputFile;
	public static String outputPath = Setting.outputRoot + "LevFDSM/"
			+ numberOfSampleGraphs + "/";
	public static int seed = Setting.seed;

	// for individual settings:
	// public static int numberOfSampleGraphs = 5000;
	// public static String inputFile =
	// "Example/Output/selectedEntriesSecondaryId_Model_1.txt";
	// public static String outputPath = "Example/Output/" +
	// "LevFDSM/"+numberOfSampleGraphs+"/";
	// public static int seed = 3306;

	// output file name for the levFDSM result:global list
	public static String levFDSM_GL_TXT = outputPath + "levFDSM_GL.txt";
	public static String levFDSM_LL_TXT = outputPath + "levFDSM_LL.txt";

	public static ArrayList<int[]> calculate() {

		ArrayList<int[]> measure = new ArrayList<int[]>();
		BipartiteGraph bG = new BipartiteGraph(inputFile);

		MyBitSet[] adjM = bG.toSecBS();

		int[][] edges = bG.generateEdges();

		int[][] coocc = new int[bG.numberOfPrimaryIds][bG.numberOfPrimaryIds];

		int length = bG.numberOfPrimaryIds;

		CooccFkt.readCooccSecAddTopRight(adjM, coocc);

		CooccFkt.multipleMatrixTopRight(coocc, numberOfSampleGraphs);

		Random generator_edges = new Random(seed);

		CooccFkt.swap(4 * bG.numberOfEdges, edges, adjM, generator_edges);

		int lengthOfWalks = (int) (bG.numberOfSamples * Math
				.log(bG.numberOfSamples));

		for (int i = 0; i < numberOfSampleGraphs; i++) {

			CooccFkt.swap(lengthOfWalks, edges, adjM, generator_edges);

			CooccFkt.readCooccSecSubstractTopRight(adjM, coocc);

		}

		for (int i = 0; i < length; i++) {
			for (int j = i + 1; j < length; j++) {
				if (coocc[i][j] > 0) {

					measure.add(new int[] { i, j, coocc[i][j] });

				}

			}

		}

		return measure;

	}

	/**
	 * Calculate all the co-occurrence with negative values
	 * 
	 * @return
	 */
	public static ArrayList<int[]> calculateAll() {

		ArrayList<int[]> measure = new ArrayList<int[]>();
		BipartiteGraph bG = new BipartiteGraph(inputFile);

		MyBitSet[] adjM = bG.toSecBS();

		int[][] edges = bG.generateEdges();

		int[][] coocc = new int[bG.numberOfPrimaryIds][bG.numberOfPrimaryIds];

		int length = bG.numberOfPrimaryIds;

		CooccFkt.readCooccSecAddTopRight(adjM, coocc);

		CooccFkt.multipleMatrixTopRight(coocc, numberOfSampleGraphs);

		Random generator_edges = new Random(seed);

		CooccFkt.swap(4 * bG.numberOfEdges, edges, adjM, generator_edges);

		int lengthOfWalks = (int) (bG.numberOfSamples * Math
				.log(bG.numberOfSamples));

		for (int i = 0; i < numberOfSampleGraphs; i++) {

			CooccFkt.swap(lengthOfWalks, edges, adjM, generator_edges);

			CooccFkt.readCooccSecSubstractTopRight(adjM, coocc);

		}

		for (int i = 0; i < length; i++) {
			for (int j = i + 1; j < length; j++) {
				if (coocc[i][j] != 0) {

					measure.add(new int[] { i, j, coocc[i][j] });

				}

			}

		}

		return measure;

	}

	public static void run() {

		File file = new File(outputPath);

		if (!file.exists()) {
			file.mkdirs();

		}

		ArrayList<int[]> measure = calculate();

		Text.writeListScale(measure, numberOfSampleGraphs, levFDSM_GL_TXT,
				"levFDSM", "global list", "", true);

		Text.writeLocalListScale(measure, numberOfSampleGraphs, levFDSM_LL_TXT,
				"levFDSM", "local list", "");
	}

	// Write down all the co-occurrence values. The value of the missing nodes
	// paars is 0
	public static void runAll() {
		File file = new File(outputPath);

		if (!file.exists()) {
			file.mkdirs();

		}

		ArrayList<int[]> measure = calculateAll();

		Text.writeListScale(measure, numberOfSampleGraphs, levFDSM_GL_TXT,
				"LevFDSM", "global list", "", true);

		Text.writeLocalListScale(measure, numberOfSampleGraphs, levFDSM_LL_TXT,
				"LevFDSM", "local list", "");

	}

	public static void main(String[] args) {

		run();
		// runAll();

	}

}
