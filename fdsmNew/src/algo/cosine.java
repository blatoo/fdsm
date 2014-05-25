package algo;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import util.ColumnComparator;
import util.MyBitSet;
import util.Text;

public class cosine {

	public static String inputFile = info.dataReadWrite.selectedEntriesSecondaryId_Model_1TXT;

	public static String outputPath = info.DataSource.outputPath
			+ File.separator + "Cosine" + File.separator;

	// public static int seed = 3306;

	public static String Cosine_GL_TXT = outputPath + "Cosine_GL.txt";
	public static String Cosine_LL_TXT = outputPath + "Cosine_LL.txt";

//	public static int[] readDegree(MyBitSet[] adjMPrimary) {
//		int[] degrees = new int[adjMPrimary.length];
//		int length = degrees.length;
//		for (int i = 0; i < length; i++) {
//			degrees[i] = adjMPrimary[i].cardinality();
//		}
//
//		return degrees;
//	}

	public static ArrayList<int[]> calculate() {

		ArrayList<int[]> globalList = new ArrayList<int[]>();

		BipartiteGraph bG = new BipartiteGraph(inputFile);
		
//		System.out.println(inputFile);

		int length = bG.numberOfPrimaryIds;

		int[][] coocc = new int[length][length];

		MyBitSet[] adjMPrimary = bG.toPrimBS();

		CooccFkt.readCooccPrimaryAddTopRight(adjMPrimary, coocc);

		int cosine;

		for (int i = 0; i < length; i++) {

			int d_i = adjMPrimary[i].cardinality();

			if (d_i == 0) {
				continue;
			}

			for (int j = i + 1; j < length; j++) {

				if (coocc[i][j] == 0.0) {
					continue;
				}

				int d_j = adjMPrimary[j].cardinality();

				if (d_j == 0) {
					continue;
				}

				cosine = (int) ((double) coocc[i][j] * (double) 1000 / Math
						.sqrt(d_i * d_j));

				int[] measure = new int[]{ i, j, cosine };

				globalList.add(measure);

			}

		}

		return globalList;

	}

	public static void run() {

		File file = new File(outputPath);

		if (!file.exists()) {
			file.mkdirs();
		}
		
		ArrayList<int[]> measures = calculate();
		
		Collections.sort(measures, new ColumnComparator(-3,1,2));
		
		Text.writeList(measures, Cosine_GL_TXT, "Consine", "global list", "", true);

	}

	public static void main(String[] args) {

		run();
		
	}

}
