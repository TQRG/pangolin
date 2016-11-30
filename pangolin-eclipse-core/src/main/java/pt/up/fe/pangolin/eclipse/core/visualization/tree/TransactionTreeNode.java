package pt.up.fe.pangolin.eclipse.core.visualization.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.swt.graphics.Image;

import pt.up.fe.pangolin.core.spectrum.Spectrum;
import pt.up.fe.pangolin.eclipse.core.Configuration;
import pt.up.fe.pangolin.eclipse.core.visualization.OpenEditorAction;

public abstract class TransactionTreeNode {

	private static final String PROJECT_ICON = "icons/prj_obj.gif";
	private static final String PACKAGE_ICON = "icons/package.gif";
	private static final String CLASS_ICON = "icons/tsuite.png";
	private static final String TEST_ICON = "icons/test.png";

	private static final String DEFAULT_PACKAGE = "(default package)";

	protected TransactionTree transactionTree;
	protected TransactionTreeNode parent;
	protected String name;

	protected Boolean checked;
	protected Boolean grayed;

	TransactionTreeNode(TransactionTree transactionTree, TransactionTreeNode parent, String name) {
		this.transactionTree = transactionTree;
		this.parent = parent;
		this.name = name;

		this.checked = null;
		this.grayed = null;
	}

	public String getName() {
		return name;
	}

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
		boolean oneGrayed = false;
		boolean allUnchecked = true;

		this.checked = false;
		this.grayed = false;

		TransactionTreeNode[] children = getChildren();
		if (children != null && children.length > 0) {
			for (TransactionTreeNode child : children) {
				allChecked = allChecked && child.isChecked();
				allUnchecked = allUnchecked && !child.isChecked();
				oneGrayed = oneGrayed || child.isGrayed();
			}

			if (allUnchecked) {
				this.checked = false;
				this.grayed = false;
			} else if (allChecked) {
				this.checked = true;
				this.grayed = oneGrayed;
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
				for (TransactionTreeNode child : children) {
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

	public void fillErrorVector(boolean[] errorVector) {
		TransactionTreeNode[] children = getChildren();
		if (children != null) {
			for (TransactionTreeNode child : children) {
				child.fillErrorVector(errorVector);
			}
		}
	}

	public void handleDoubleClick() {

	}

	public Image getImage() {
		return null;
	}

	public String getToolTip() {
		if (this.checked) {
			return "Uncheck To Dissociate " + getElementName();
		}
		else {
			return "Check To Associate " + getElementName();
		}
	}

	protected abstract String getElementName();

	public static class RootNode extends TransactionTreeNode {

		RootNode(TransactionTree transactionTree) {
			super(transactionTree, null, null);
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

		public Image getImage() {
			return Configuration.get().getImage(PROJECT_ICON);
		}

		public List<TransactionTreeNode> addTests(Spectrum spectrum) {

			Map<String, PackageNode> nodeMap = new TreeMap<String, PackageNode>();

			for (int t = 0; t < spectrum.getTransactionsSize(); t++) {
				//parse package
				String transactionName = spectrum.getTransactionName(t);
				int index = transactionName.indexOf('(');
				String testName = transactionName.substring(0, index);

				String className = transactionName.substring(index + 1, transactionName.length() - 1);
				index = className.lastIndexOf('.');

				String packageName = "(default package)";
				if (index != -1) {
					packageName = className.substring(0, index);
					className = className.substring(index + 1, className.length());
				}

				PackageNode child = nodeMap.get(packageName);
				if (child == null) {
					child = new PackageNode(transactionTree, this, packageName);
					nodeMap.put(packageName, child);
				}

				child.insertTest(className, testName, t, spectrum.isError(t));
			}

			return new ArrayList<TransactionTreeNode>(nodeMap.values());
		}

		@Override
		protected String getElementName() {
			return "Project";
		}
	}

	public static class PackageNode extends TransactionTreeNode {

		private List<ClassNode> children;

		PackageNode(TransactionTree transactionTree, TransactionTreeNode parent, String name) {
			super(transactionTree, parent, name);

			children = new ArrayList<ClassNode>();
		}

		public void insertTest(String className, String testName, int t, boolean error) {
			ClassNode child = null;

			for (ClassNode c : children) {
				if (className.equals(c.getName())) {
					child = c;
					break;
				}
			}

			if (child == null) {
				child = new ClassNode(transactionTree, this, className);
				children.add(child);
			}

			child.insertTest(testName, t, error);
		}

		public Image getImage() {
			return Configuration.get().getImage(PACKAGE_ICON);
		}

		public TransactionTreeNode[] getChildren() {
			return children.toArray(new TransactionTreeNode[children.size()]);
		}

		public boolean hasChildren() {
			return !children.isEmpty();
		}

		@Override
		protected String getElementName() {
			return "Package";
		}
	}

	public static class ClassNode extends TransactionTreeNode {

		private List<TransactionTreeNode> children;
		private String fullyQualifiedName;

		ClassNode(TransactionTree transactionTree, TransactionTreeNode parent, String name) {
			super(transactionTree, parent, name);

			children = new ArrayList<TransactionTreeNode>();

			String packageName = parent.getName() + ".";
			if (packageName.startsWith(DEFAULT_PACKAGE)) {
				packageName = "";
			}
			fullyQualifiedName = packageName + name;
		}

		public void insertTest(String testName, int t, boolean error) {
			children.add(new TestNode(transactionTree, this, testName, t, error));
		}

		public TransactionTreeNode[] getChildren() {
			return children.toArray(new TransactionTreeNode[children.size()]);
		}

		public boolean hasChildren() {
			return !children.isEmpty();
		}

		public Image getImage() {
			return Configuration.get().getImage(CLASS_ICON);
		}

		public String getFullyQualifiedName() {
			return fullyQualifiedName;
		}

		public void handleDoubleClick() {
			OpenEditorAction.openElement(transactionTree.getProject(), fullyQualifiedName);
		}

		@Override
		protected String getElementName() {
			return "Test Class";
		}

	}

	public static class TestNode extends TransactionTreeNode {

		private int id;
		private String testName;

		TestNode(TransactionTree transactionTree, TransactionTreeNode parent, String name, int id, boolean checked) {
			super(transactionTree, parent, name);
			this.id = id;

			this.grayed = false;
			this.checked = checked;

			this.testName = name; //remove things after [
		}

		public void fillErrorVector(boolean[] errorVector) {
			if (id >= 0 && id < errorVector.length) {
				errorVector[id] = this.checked;
			}
		}

		public void handleDoubleClick() {
			if (parent instanceof ClassNode) {
				String className = ((ClassNode)parent).getFullyQualifiedName();
				OpenEditorAction.openElement(transactionTree.getProject(), className, testName);
			}
		}

		public Image getImage() {
			return Configuration.get().getImage(TEST_ICON);
		}

		@Override
		protected String getElementName() {
			return "Test";
		}
	}

	public static class ManualNode extends TransactionTreeNode {

		private int id;

		ManualNode(TransactionTree transactionTree, TransactionTreeNode parent, String name, int id, boolean checked) {
			super(transactionTree, parent, name);
			this.id = id;

			this.grayed = false;
			this.checked = checked;
		}

		public void fillErrorVector(boolean[] errorVector) {
			if (id >= 0 && id < errorVector.length) {
				errorVector[id] = this.checked;
			}
		}

		public Image getImage() {
			return Configuration.get().getImage(TEST_ICON);
		}

		@Override
		protected String getElementName() {
			return "Transaction";
		}
	}
}
