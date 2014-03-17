package netflix;

import info.DataSource;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.RandomAccess;
import java.util.StringTokenizer;

public class Check {

	public static String movieTitleTXT = info.DataSource.Pfad_doc
			+ "movie_titles.txt";
	
	public static String outputPath = info.DataSource.outputPath+"netflix/";

	public static void readGL_Top(String levFDSM_GL_TXT, int top, String outputFile) {

		ArrayList<String[]> movieTitles = new ArrayList<String[]>();

		try {
			
			// Read the movie titles into an Arraylist
			BufferedReader br = new BufferedReader(
					new FileReader(movieTitleTXT));

			String line = br.readLine();

			while (line != null) {

				String[] content = line.split(",", 3);

				movieTitles.add(Arrays.copyOfRange(content, 1, 3));

				line = br.readLine();
			}

			br.close();

			// Read the global list.
			br = new BufferedReader(new FileReader(levFDSM_GL_TXT));

			line = br.readLine();

			HashMap<String, String> hm = util.Text.readLineInfos(line);

			int numberOflines = Integer.parseInt(hm.get("length"));

			if (Math.abs(top) > numberOflines) {
				System.err
						.println("You can't input a number which bigger than the number of the total cooccurences.");
				System.exit(-1);
			}

			int begin = 0;

			if (top < 0) {
				begin = numberOflines + top;
			}

			for (int i = 0; i < begin; i++) {

				br.readLine();

			}
			
			File file = new File(outputPath);
			
			if (!file.exists()) {
				file.mkdir();
			}

			BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
			
			bw.write("#Format: PrimaryId1, year1, title1, PrimaryId2, year2, title2, measure");
			bw.write("#Top = "+top);
			
			top = Math.abs(top);
			


//			file = new File(outputFile);
			
			
			
			for (int i = 0; i < top; i++) {

				line = br.readLine();

				StringTokenizer st = new StringTokenizer(line, ",");

				int[] cooccInfo = new int[] { Integer.parseInt(st.nextToken()),
						Integer.parseInt(st.nextToken()),
						Integer.parseInt(st.nextToken()) };

				String details = cooccInfo[0] + ","
						+ movieTitles.get(cooccInfo[0])[0] + ","
						+ movieTitles.get(cooccInfo[0])[1] + ","
						+ cooccInfo[1]+ "," 
						+ movieTitles.get(cooccInfo[1])[0] + ","
						+ movieTitles.get(cooccInfo[1])[1] + "," + cooccInfo[2];

				bw.write(details+System.lineSeparator());
				System.out.println(details);

			}
			
//			System.out.println("0 : "+movieTitles.get(0)[0]+", "+movieTitles.get(0)[1]);

			bw.close();
			br.close();

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

	}

	public static void test() {

	}

	public static void main(String[] args) {
		
		String outputFile = outputPath+"levFDSM_GL_Top.txt";
		readGL_Top(algo.levFDSM.levFDSM_GL_TXT, 100, outputFile);

	}

}