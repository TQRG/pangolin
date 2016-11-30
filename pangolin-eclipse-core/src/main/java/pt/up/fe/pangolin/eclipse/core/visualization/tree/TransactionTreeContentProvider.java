package pt.up.fe.pangolin.eclipse.core.visualization.tree;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ICheckStateProvider;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;

public class TransactionTreeContentProvider extends CellLabelProvider implements
		ITreeContentProvider, ICheckStateProvider, IDoubleClickListener {

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement != null && inputElement instanceof TransactionTree) {
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

	@Override
	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	public Image getImage(Object element) {
		if (element instanceof TransactionTreeNode) {
			return ((TransactionTreeNode) element).getImage();
		}
		return null;
	}

	public String getText(Object element) {
		if (element instanceof TransactionTreeNode) {
			return ((TransactionTreeNode) element).getName();
		}
		else if (element instanceof String) {
			return (String) element;
		}
		return null;
	}

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

	@Override
	public void doubleClick(DoubleClickEvent event) {
		ISelection selection = event.getSelection();
		if (selection instanceof ITreeSelection) {
			Object element = ((ITreeSelection)selection).getFirstElement();
			if (element instanceof TransactionTreeNode) {
				((TransactionTreeNode) element).handleDoubleClick();;
			}
		}
	}

	@Override
	public void update(ViewerCell cell) {
		cell.setText(getText(cell.getElement()));
		cell.setImage(getImage(cell.getElement()));
	}

	@Override
	public String getToolTipText(Object element) {
		if (element instanceof TransactionTreeNode) {
			return ((TransactionTreeNode)element).getToolTip();
		}
		return null;
	}

	@Override
	public Point getToolTipShift(Object object) {
		return new Point(5,5);
	}

	@Override
	public int getToolTipDisplayDelayTime(Object object) {
		return 1000;
	}

	@Override
	public int getToolTipTimeDisplayed(Object object) {
		return 5000;
	}
}
