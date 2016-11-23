package pt.up.fe.pangolin.eclipse.core.visualization.tree;

public abstract class TransactionTreeNode {

	protected TransactionTree transactionTree;
	protected TransactionTreeNode parent;

	protected Boolean checked;
	protected Boolean grayed;

	TransactionTreeNode(TransactionTree transactionTree, TransactionTreeNode parent) {
		this.transactionTree = transactionTree;
		this.parent = parent;

		this.checked = null;
		this.grayed = null;
	}

	public abstract String getName();

	public TransactionTreeNode[] getChildren() {
		return null;
	}

	public boolean hasChildren() {
		return false;
	}

	public TransactionTreeNode getParent() {
		return parent;
	}

	public boolean isGrayed() {
		if (grayed == null) {
			computeCheckedState();
		}
		return grayed;
	}

	public boolean isChecked() {
		if (checked == null) {
			computeCheckedState();
		}
		return checked;
	}

	private void computeCheckedState() {
		boolean allChecked = true;
		boolean allUnchecked = true;

		TransactionTreeNode[] children = getChildren();
		if (children != null && children.length > 0) {
			for (TransactionTreeNode child : getChildren()) {
				allChecked = allChecked && child.isChecked();
				allUnchecked = allUnchecked && !child.isChecked();
			}

			if (allUnchecked) {
				this.checked = false;
				this.grayed = false;
			} else if (allChecked) {
				this.checked = true;
				this.grayed = false;
			} else if (!allUnchecked && !allChecked) {
				this.checked = true;
				this.grayed = true;
			}
		}
	}

	public void setChecked(boolean value) {
		if (!this.checked.equals(value)) {
			//cascade to children
			this.checked = value;

			TransactionTreeNode[] children = getChildren();
			if (children != null) {
				for (TransactionTreeNode child : getChildren()) {
					child.setChecked(value);
				}
			}

			//ask parent to recompute
			computeParentCheckedState();
		}
	}

	private void computeParentCheckedState() {
		TransactionTreeNode parent = getParent();
		if (parent != null) {
			parent.computeCheckedState();
			parent.computeParentCheckedState();
		}
	}

	public static class RootNode extends TransactionTreeNode {

		RootNode(TransactionTree transactionTree, TransactionTreeNode parent) {
			super(transactionTree, parent);
		}

		public String getName() {
			return transactionTree.getProjectName();
		}

		public TransactionTreeNode[] getChildren() {
			return transactionTree.getChildren();
		}

		public boolean hasChildren() {
			TransactionTreeNode[] children = getChildren();
			return children != null && getChildren().length > 0;
		}
	}

	public static class TestNode extends TransactionTreeNode {

		private String name;
		private int id;

		TestNode(TransactionTree transactionTree, TransactionTreeNode parent, String name, int id, boolean checked) {
			super(transactionTree, parent);
			this.name = name;
			this.id = id;

			this.grayed = false;
			this.checked = checked;
		}

		public String getName() {
			return name;
		}
	}

}
