package pt.up.fe.pangolin.eclipse.core.launching;

import java.util.Arrays;

import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.jdt.core.IJavaProject;

import pt.up.fe.pangolin.core.events.EventListener;
import pt.up.fe.pangolin.core.model.Node.Type;
import pt.up.fe.pangolin.core.model.Tree;
import pt.up.fe.pangolin.core.spectrum.Spectrum;
import pt.up.fe.pangolin.core.spectrum.SpectrumBuilder;
import pt.up.fe.pangolin.core.spectrum.diagnosis.SFL;
import pt.up.fe.pangolin.eclipse.core.Configuration;
import pt.up.fe.pangolin.eclipse.core.visualization.tree.TransactionTree;

public class ExecutionDescription implements EventListener {

	private SpectrumBuilder delegate;
	private ILaunchConfigurationType type;
	private IJavaProject project;

	public ExecutionDescription(ILaunchConfigurationType type, IJavaProject project) {
		this.type = type;
		this.project = project;
		this.delegate = new SpectrumBuilder();
	}

	@Override
	public void endTransaction (String transactionName, boolean[] activity, boolean isError) {
		delegate.endTransaction(transactionName, activity, isError);
	}

	@Override
	public void endTransaction (String transactionName, boolean[] activity, int hashCode, boolean isError) {
		delegate.endTransaction(transactionName, activity, hashCode, isError);
	}

	@Override
	public void addNode(int id, String name, Type type, int parentId, int line) {
		delegate.addNode(id, name, type, parentId, line);
	}

	@Override
	public void addProbe(int id, int nodeId) {
		delegate.addProbe(id, nodeId);
	}

	@Override
	public void endSession() {
		delegate.endSession();
		diagnose();
	}

	public EventListener getEventListener() {
		return delegate;
	}

	public Spectrum getSpectrum() {
		return delegate.getSpectrum();
	}

	private void diagnose() {
		Spectrum s = getSpectrum();

		TransactionTree tt = new TransactionTree(project, s, false);

		boolean[] errorVector = tt.getErrorVector();
		System.out.println("Error vector " + Arrays.toString(errorVector));
		double[] diagnosis = SFL.diagnose(s, errorVector);
		System.out.println("Diagnosis " + Arrays.toString(diagnosis));
		double[] treeDiagnosis = constructTreeDiagnosis(s, diagnosis);
		System.out.println("Tree Diagnosis " + Arrays.toString(treeDiagnosis));

		Tree t = s.getTree();

		StringBuilder sb = new StringBuilder("{\"type\":\"visualization\",");
		sb.append(t.toString());
		sb.append(",\"scores\":");
		sb.append(Arrays.toString(treeDiagnosis));
		sb.append("}");


		Configuration.get().initializeVisualization(project, sb.toString());
		Configuration.get().getTransactionViewer().setInput(tt);
	}

	private static double[] constructTreeDiagnosis(Spectrum s, double[] diagnosis) {
		double[] treeDiagnosis = new double[s.getTree().size()];
		Arrays.fill(treeDiagnosis, -1);

		for (int c = 0; c < diagnosis.length; c++) {
			treeDiagnosis[s.getNodeOfProbe(c).getId()] = diagnosis[c];
		}

		return treeDiagnosis;
	}
}
