package pt.up.fe.pangolin.eclipse.core.visualization;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;

public class OpenEditorAction {

	public static void openElement(final IJavaProject project, final String element) {
		openElement(project, element, -1);
	}

	public static void openElement(final IJavaProject project, final String element, final int lineNumber) {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				try {
					IType type = project.findType(element);

					IEditorPart editor = JavaUI.openInEditor(type);

					if(editor != null && 
							lineNumber != -1 && 
							editor instanceof ITextEditor) {
						ITextEditor textEditor = (ITextEditor) editor;
						IDocument document = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput());
						textEditor.selectAndReveal(document.getLineOffset(lineNumber-1), 0);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

}
