package pt.up.fe.pangolin.eclipse.ui.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import pt.up.fe.pangolin.eclipse.core.Configuration;
import pt.up.fe.pangolin.eclipse.core.visualization.tree.TransactionViewer;

public class TransactionView extends ViewPart {

	private TransactionViewer tv = null;

	@Override
	public void createPartControl(Composite parent) {
		tv = TransactionViewer.newTransactionViewer(parent, this);
		Configuration.get().setTransactionViewer(tv);
	}

	@Override
	public void setFocus() {
		if (tv != null) {
			tv.setFocus();
		}
	}

	public void dispose() {
		if (tv != null) {
			tv.dispose();
			tv = null;
		}
		super.dispose();
	}
}
