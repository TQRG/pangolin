package pt.up.fe.pangolin.eclipse.ui.actions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.expressions.EvaluationContext;
import org.eclipse.core.expressions.EvaluationResult;
import org.eclipse.core.expressions.Expression;
import org.eclipse.core.expressions.ExpressionConverter;
import org.eclipse.core.expressions.ExpressionTagNames;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.core.ILaunchManager;


public class ContextualLaunchableTester extends PropertyTester {

	private Map<String, Expression> expressions = new HashMap<String, Expression>();

	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		String delegateShortcutID = (String) args[0];
		Expression expr = expressions.get(delegateShortcutID);
		if (expr == null) {
			expr = createEnablementExpression(delegateShortcutID);
			expressions.put(delegateShortcutID, expr);
		}

		try {
			return expr.evaluate(createContext(receiver)) != EvaluationResult.FALSE;
		} catch (CoreException ce) {
			ce.printStackTrace();
			return false;
		}
	}

	private IEvaluationContext createContext(Object selection) {
		IEvaluationContext context = new EvaluationContext(null, selection);
		context.addVariable("selection", selection);
		return context;
	}

	private Expression createEnablementExpression(String delegateShortcutID) {
		IConfigurationElement element = findEnablementConfiguration(delegateShortcutID);
		if (element != null) {
			try {
				return ExpressionConverter.getDefault().perform(element);
			} catch (CoreException ce) {
				ce.printStackTrace();
			}
		}
		return Expression.FALSE;
	}

	private IConfigurationElement findEnablementConfiguration(String delegateShortcutID) {

		IConfigurationElement[] configs = Platform.getExtensionRegistry().getConfigurationElementsFor("org.eclipse.debug.ui.launchShortcuts");

		for (final IConfigurationElement config : configs) {
			if (!delegateShortcutID.equals(config.getAttribute("id")))
				continue;

			String modes = config.getAttribute("modes");
			if (modes == null)
				continue;

			if (!Arrays.asList(modes.split("\\W")).contains(ILaunchManager.RUN_MODE))
				continue;

			IConfigurationElement[] launch = config.getChildren("contextualLaunch");
			if (launch.length != 1)
				continue;

			IConfigurationElement[] enablement = launch[0].getChildren(ExpressionTagNames.ENABLEMENT);

			if (enablement.length == 1)
				return enablement[0];
		}
		return null;
	}

}
