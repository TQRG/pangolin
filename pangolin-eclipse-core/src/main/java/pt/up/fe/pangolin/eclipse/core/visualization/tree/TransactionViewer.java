package pt.up.fe.pangolin.eclipse.core.visualization.tree;

import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import pt.up.fe.pangolin.eclipse.core.launching.ExecutionDescription;

public class TransactionViewer implements ICheckStateListener {

	private ViewPart viewPart;
	private CheckboxTreeViewer treeViewer;
	private ExecutionDescription executionDescription;

	public static TransactionViewer newTransactionViewer(Composite parent, ViewPart viewPart) {
		return new TransactionViewer(parent, viewPart);
	}

	public static void revealView() {
		PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				try {
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().
					getActivePage().showView("pt.up.fe.pangolin.eclipse.ui.transactionView");
				} catch (PartInitException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public TransactionViewer(Composite parent, ViewPart viewPart) {
		this.viewPart = viewPart;
		this.treeViewer = new CheckboxTreeViewer(parent, SWT.NONE);
		ColumnViewerToolTipSupport.enableFor(this.treeViewer);

		TransactionTreeContentProvider contentProvider = new TransactionTreeContentProvider();
		this.treeViewer.setContentProvider(contentProvider);
		this.treeViewer.setLabelProvider(contentProvider);
		this.treeViewer.setCheckStateProvider(contentProvider);
		this.treeViewer.addDoubleClickListener(contentProvider);

		this.treeViewer.addCheckStateListener(this);
	}

	public void setInput(final ExecutionDescription executionDescription) {
		final boolean isSame = this.executionDescription == executionDescription;
		this.executionDescription = executionDescription;

		final CheckboxTreeViewer tv = this.treeViewer;
		PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				if (isSame) {
					tv.refresh();
				}
				else {
					tv.setInput(executionDescription.getTransactionTree());
				}
				tv.expandAll();
			}
		});
	}

	public void setFocus() {
	}

	public void dispose() {
	}

	@Override
	public void checkStateChanged(CheckStateChangedEvent event) {
		TransactionTreeNode element = (TransactionTreeNode) event.getElement();
		element.setChecked(event.getChecked());
		this.treeViewer.refresh();
		this.executionDescription.diagnose();
	}

}
