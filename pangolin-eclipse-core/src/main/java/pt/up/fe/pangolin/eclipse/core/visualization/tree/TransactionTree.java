package pt.up.fe.pangolin.eclipse.core.visualization.tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.core.IJavaProject;

import pt.up.fe.pangolin.core.spectrum.Spectrum;
import pt.up.fe.pangolin.eclipse.core.visualization.tree.TransactionTreeNode.ManualNode;
import pt.up.fe.pangolin.eclipse.core.visualization.tree.TransactionTreeNode.RootNode;

public class TransactionTree {

	private boolean isLocalJavaApplication;
	private Spectrum spectrum;
	private IJavaProject project;

	private RootNode rootNode;
	private TransactionTreeNode[] rootNodes;
	private TransactionTreeNode[] children;

	public TransactionTree(IJavaProject project, Spectrum spectrum, boolean isLocalJavaApplication) {
		this.project = project;
		this.spectrum = spectrum;
		this.isLocalJavaApplication = isLocalJavaApplication;

		this.rootNode = new RootNode(this);
		this.rootNodes = new TransactionTreeNode[]{rootNode};
	}

	public String getProjectName() {
		return project.getElementName();
	}

	public IJavaProject getProject() {
		return project;
	}

	public TransactionTreeNode[] getRootNodes() {
		return rootNodes;
	}

	public TransactionTreeNode[] getChildren() {
		if (children == null) {
			List<TransactionTreeNode> tmp;

			if (isLocalJavaApplication) {
				tmp = new ArrayList<TransactionTreeNode>();
				for (int t = 0; t < spectrum.getTransactionsSize(); t++) {
					tmp.add(new ManualNode(this, rootNode, spectrum.getTransactionName(t), t, spectrum.isError(t)));
				}
			} else {
				tmp = rootNode.addTests(spectrum);
			}

			this.children = tmp.toArray(new TransactionTreeNode[tmp.size()]);
		} else if (isLocalJavaApplication && spectrum.getTransactionsSize() > children.length) {
			List<TransactionTreeNode> tmp = new ArrayList<TransactionTreeNode>(Arrays.asList(children));

			for (int t = children.length; t < spectrum.getTransactionsSize(); t++) {
				tmp.add(new ManualNode(this, rootNode, spectrum.getTransactionName(t), t, spectrum.isError(t)));
			}

			this.children = tmp.toArray(new TransactionTreeNode[tmp.size()]);
		}
		return children;
	}

	public boolean[] getErrorVector() {
		boolean[] errorVector = new boolean[spectrum.getTransactionsSize()];
		Arrays.fill(errorVector, false);

		rootNode.fillErrorVector(errorVector);

		return errorVector;
	}
}
