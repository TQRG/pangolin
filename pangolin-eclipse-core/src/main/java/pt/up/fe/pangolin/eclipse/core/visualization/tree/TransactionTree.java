package pt.up.fe.pangolin.eclipse.core.visualization.tree;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.IJavaProject;

import pt.up.fe.pangolin.core.spectrum.Spectrum;
import pt.up.fe.pangolin.eclipse.core.visualization.tree.TransactionTreeNode.RootNode;
import pt.up.fe.pangolin.eclipse.core.visualization.tree.TransactionTreeNode.TestNode;

public class TransactionTree {

	private boolean isLocalJavaApplication;
	private Spectrum spectrum;
	private IJavaProject project;

	private TransactionTreeNode rootNode;
	private Object[] rootNodes;
	private Object[] children;

	public TransactionTree(IJavaProject project, Spectrum spectrum, boolean isLocalJavaApplication) {
		this.project = project;
		this.spectrum = spectrum;
		this.isLocalJavaApplication = isLocalJavaApplication;

		this.rootNode = new RootNode(this, null);
		this.rootNodes = new Object[]{rootNode};
	}

	public String getProjectName() {
		return project.getElementName();
	}

	public Object[] getRootNodes() {
		return rootNodes;
	}

	public Object[] getChildren() {
		if (children == null) {
			List<TransactionTreeNode> tmp = new ArrayList<TransactionTreeNode>();

			for (int t = 0; t < spectrum.getTransactionsSize(); t++) {
				tmp.add(new TestNode(this, rootNode, spectrum.getTransactionName(t), t));
			}

			this.children = tmp.toArray();
		}
		return children;
	}
}
