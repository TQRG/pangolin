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

public class TransactionViewer {

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
		this.treeViewer.setContentProvider(new TransactionTreeContentProvider());
		this.treeViewer.setLabelProvider(new TransactionLabelProvider());


		this.treeViewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				System.out.println("double clicked " + event.getSelection());

			}
		});

		this.treeViewer.setCheckStateProvider(new ICheckStateProvider() {

			@Override
			public boolean isGrayed(Object element) {
				if (element instanceof TransactionTreeNode) {
					return ((TransactionTreeNode) element).isGrayed();
				}
				return false;
			}

			@Override
			public boolean isChecked(Object element) {
				if (element instanceof TransactionTreeNode) {
					return ((TransactionTreeNode) element).isChecked();
				}
				return false;
			}
		});
		
		final CheckboxTreeViewer tv = this.treeViewer;
		this.treeViewer.addCheckStateListener(new ICheckStateListener() {

			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {
				TransactionTreeNode element = (TransactionTreeNode) event.getElement();
				element.setChecked(event.getChecked());
				tv.refresh();
			}
			
			/*public void checkParents(TransactionTreeNode element) {
			    if (element == null) {
			        return;
			    }

			    if (element != null) {
			        boolean allChecked = true;
			        boolean allUnchecked = true;
			        for (final Object fieldElement : element.getChildren()) {
			            allChecked = allChecked && tv.getChecked(fieldElement);
			            allUnchecked = allUnchecked && !tv.getChecked(fieldElement);
			        }
			        
			        if (allUnchecked) {
			            tv.setChecked(element, false);
			            tv.setGrayed(element, false);
			        } else
			        if (allChecked) {
			            tv.setChecked(element, true);
			            tv.setGrayed(element, false);
			        } else
			        if (!allUnchecked && !allChecked) {
			            tv.setChecked(element, true);
			            tv.setGrayed(element, true);
			        }
			    }
			    checkParents( (TransactionTreeNode) element.getParent());
			}

			public void checkChildren(TransactionTreeNode element, final boolean checked) {
			    if (element == null) {
			        return;
			    }

			    if (element != null) {
			        tv.setChecked(element, checked);
			        tv.setGrayed(element, false);
			        tv.setSubtreeChecked(element, checked);
			    }
			}*/
		});
	}

	public void setInput(final TransactionTree t) {
		final CheckboxTreeViewer tv = this.treeViewer;
		PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				tv.setInput(t);
				//tv.refresh();
			}
		});
	}

	public void setFocus() {
	}

	public void dispose() {
	}

}
