package pt.up.fe.pangolin.eclipse.core.visualization.tree;

import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ICheckStateProvider;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

public class TransactionViewer implements ICheckStateListener {

	private ViewPart viewPart;
	private CheckboxTreeViewer treeViewer;

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

		TransactionTreeContentProvider contentProvider = new TransactionTreeContentProvider();
		this.treeViewer.setContentProvider(contentProvider);
		this.treeViewer.setLabelProvider(contentProvider);
		this.treeViewer.setCheckStateProvider(contentProvider);
		this.treeViewer.addDoubleClickListener(contentProvider);

		this.treeViewer.addCheckStateListener(this);
	}

	public void setInput(final TransactionTree t) {
		final CheckboxTreeViewer tv = this.treeViewer;
		PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				tv.setInput(t);
				tv.expandAll();
			}
		});
	}

	public void refresh() {
		final CheckboxTreeViewer tv = this.treeViewer;
		PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				tv.refresh();
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
	}

}
