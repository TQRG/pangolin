package pt.up.fe.pangolin.eclipse.core.visualization.tree;

public abstract class TransactionTreeNode {
	
	protected TransactionTree transactionTree;
	protected TransactionTreeNode parent;
	
	TransactionTreeNode(TransactionTree transactionTree, TransactionTreeNode parent) {
		this.transactionTree = transactionTree;
		this.parent = parent;
	}
	
	public abstract String getName();
	
	public Object[] getChildren() {
		return null;
	}
	
	public Object getParent() {
		return parent;
	}
	
	public static class RootNode extends TransactionTreeNode {

		RootNode(TransactionTree transactionTree, TransactionTreeNode parent) {
			super(transactionTree, parent);
		}
		
		public String getName() {
			return transactionTree.getProjectName();
		}
		
		public Object[] getChildren() {
			return transactionTree.getChildren();
		}
	}
	
	public static class TestNode extends TransactionTreeNode {

		private String name;
		private int id;
		
		TestNode(TransactionTree transactionTree, TransactionTreeNode parent, String name, int id) {
			super(transactionTree, parent);
			this.name = name;
			this.id = id;
		}
		
		public String getName() {
			return name;
		}
	}
	
}
