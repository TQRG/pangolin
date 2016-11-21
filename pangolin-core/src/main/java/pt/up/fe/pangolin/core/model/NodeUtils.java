package pt.up.fe.pangolin.core.model;

import java.util.List;

public class NodeUtils {
	public static String getClassName (Node n) {
		while (n != null && n.getType() != Node.Type.CLASS) {
			n = n.getParent();
		}

		if (n == null) {
			return null;
		}

		return n.getFullNameWithSymbol(1);
	}

	public static int getLineNumber (Node n) {

		if (n.getType() == Node.Type.LINE) {
			return n.getLine();
		}

		if (n.getType() == Node.Type.METHOD) {
			List<Node> c = n.getChildren();

			if (c.isEmpty()) {
				return n.getLine();
			}
			else if (c.get(0).getType() == Node.Type.LINE) {
				return c.get(0).getLine();
			}
		}

		return -1;
	}
}