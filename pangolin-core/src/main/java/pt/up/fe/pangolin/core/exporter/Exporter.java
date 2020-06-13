package pt.up.fe.pangolin.core.exporter;

import pt.up.fe.pangolin.core.model.Node;
import pt.up.fe.pangolin.core.spectrum.Spectrum;
import pt.up.fe.pangolin.core.spectrum.diagnosis.SFL;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Exporter {

    public static void exportAnalysis(Spectrum spectrum, String dir) {
        boolean[] errorVector = new boolean[spectrum.getTransactionsSize()];
		Arrays.fill(errorVector, false);
		for (int transaction = 0; transaction < spectrum.getTransactionsSize(); transaction++) {
			errorVector[transaction] = spectrum.isError(transaction);
		}

		double[] scores = SFL.diagnose(spectrum, errorVector);
        List<Row> rows = new ArrayList<Row>();
        for (int id = 0; id < scores.length; id++) {
            Node node = spectrum.getNodeOfProbe(id);
            String packageName = node.getPackageName();
            String className = node.getClassName();
            String methodName = node.getMethodName();
            String line = Integer.toString(node.getLine());
            String score = Double.toString(scores[id]);
            Row row = new Row(packageName, className, methodName, line, score);
            rows.add(row);
        }

        CSVUtil.initializeCSVDirectory(dir);
        CSVUtil.addAllToCSVFile(dir + File.separator + Constants.ANALYSIS_PATH_SUFFIX, rows);

        System.out.println("Exported results of analysis to " + dir + File.separator + Constants.ANALYSIS_PATH_SUFFIX);
    }

    public static String getTemporaryDir() {
        try {
            final File temp;

            temp = File.createTempFile("temp", Long.toString(System.nanoTime()));

            if(!(temp.delete()))
            {
                throw new IOException("Could not delete temp file: " + temp.getAbsolutePath());
            }

            if(!(temp.mkdir()))
            {
                throw new IOException("Could not create temp directory: " + temp.getAbsolutePath());
            }

            return temp.getAbsolutePath();
        } catch (IOException e) {
            return ".";
        }
    }
}
