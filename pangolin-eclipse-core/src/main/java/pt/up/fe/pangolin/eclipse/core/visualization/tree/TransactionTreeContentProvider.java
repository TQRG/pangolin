package pt.up.fe.pangolin.eclipse.core.visualization.tree;

import org.eclipse.jface.viewers.ITreeContentProvider;

public class TransactionTreeContentProvider implements ITreeContentProvider {

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof TransactionTree) {
			return ((TransactionTree) inputElement).getRootNodes();
		}
		return null;
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof TransactionTreeNode) {
			return ((TransactionTreeNode) parentElement).getChildren();
		}
		return null;
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof TransactionTreeNode) {
			return ((TransactionTreeNode) element).getParent();
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof TransactionTreeNode) {
			Object[] children = ((TransactionTreeNode) element).getChildren();
			return children != null && children.length > 0;
		}
		return false;
	}

}
