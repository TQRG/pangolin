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
	private boolean isLocalJavaApplication;
	private IJavaProject project;
	private TransactionTree transactionTree;

	public ExecutionDescription(ILaunchConfigurationType type, IJavaProject project) {
		this.isLocalJavaApplication = "org.eclipse.jdt.launching.localJavaApplication".equals(type.getIdentifier());
		this.project = project;
		this.delegate = new SpectrumBuilder();
		this.transactionTree = null;
	}

	@Override
	public void endTransaction (String transactionName, boolean[] activity, boolean isError) {
		delegate.endTransaction(transactionName, activity, isError);

		if(isLocalJavaApplication) {
			diagnose();
			notifyTransactionTree();
		}
	}

	@Override
	public void endTransaction (String transactionName, boolean[] activity, int hashCode, boolean isError) {
		delegate.endTransaction(transactionName, activity, hashCode, isError);

		if(isLocalJavaApplication) {
			diagnose();
			notifyTransactionTree();
		}
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

		if (!isLocalJavaApplication) {
			diagnose();
			notifyTransactionTree();
		}
	}

	public EventListener getEventListener() {
		return delegate;
	}

	public Spectrum getSpectrum() {
		return delegate.getSpectrum();
	}

	public TransactionTree getTransactionTree() {
		if (transactionTree == null) {
			transactionTree = new TransactionTree(project, getSpectrum(), isLocalJavaApplication);
		}
		return transactionTree;
	}

	public synchronized void diagnose() {
		Spectrum s = getSpectrum();
		TransactionTree tt = getTransactionTree();

		if (s.getTransactionsSize() > 0) {
			boolean[] errorVector = tt.getErrorVector();
			double[] diagnosis = SFL.diagnose(s, errorVector);
			double[] treeDiagnosis = constructTreeDiagnosis(s, diagnosis);

			Tree t = s.getTree();
			StringBuilder sb = new StringBuilder("{\"type\":\"visualization\",");
			sb.append(t.toString());
			sb.append(",\"scores\":");
			sb.append(Arrays.toString(treeDiagnosis));
			sb.append("}");

			Configuration.get().initializeVisualization(project, sb.toString());
		}
	}

	private void notifyTransactionTree() {
		Configuration.get().getTransactionViewer().setInput(this);
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
