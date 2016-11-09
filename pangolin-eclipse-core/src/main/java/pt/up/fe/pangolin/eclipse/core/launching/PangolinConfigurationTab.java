package pt.up.fe.pangolin.eclipse.core.launching;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import pt.up.fe.pangolin.core.instrumentation.granularity.GranularityFactory.GranularityLevel;
import pt.up.fe.pangolin.eclipse.core.PluginAgentConfigs;

public abstract class PangolinConfigurationTab extends AbstractLaunchConfigurationTab {

	private PluginAgentConfigs pluginConfigs;
	private Button methodGranularity;
	private Button basicBlockGranularity;
	private Button statementGranularity;
	
	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));

		Group granularity = new Group(composite, SWT.SHADOW_IN);
		granularity.setLayout(new GridLayout(1, false));
		granularity.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		granularity.setText("Instrumentation Granularity:");

		methodGranularity = createRadioButton(granularity, "Method");
		methodGranularity.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		methodGranularity.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				pluginConfigs.setGranularityLevel(GranularityLevel.method);
				setDirty(true);
				updateLaunchConfigurationDialog();
			}
		});

		basicBlockGranularity = createRadioButton(granularity, "Basic Block");
		basicBlockGranularity.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		basicBlockGranularity.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				pluginConfigs.setGranularityLevel(GranularityLevel.basicblock);
				setDirty(true);
				updateLaunchConfigurationDialog();
			}
		});
		
		statementGranularity = createRadioButton(granularity, "Statement");
		statementGranularity.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		statementGranularity.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				pluginConfigs.setGranularityLevel(GranularityLevel.line);
				setDirty(true);
				updateLaunchConfigurationDialog();
			}
		});

		setControl(composite);
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
	}

	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		pluginConfigs = PluginAgentConfigs.getConfigs(configuration);
		
		methodGranularity.setSelection(pluginConfigs.getGranularityLevel() == GranularityLevel.method);
		basicBlockGranularity.setSelection(pluginConfigs.getGranularityLevel() == GranularityLevel.basicblock);
		statementGranularity.setSelection(pluginConfigs.getGranularityLevel() == GranularityLevel.line);
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(PluginAgentConfigs.PANGOLIN_CONFIGS, pluginConfigs.serialize());
	}
	
	@Override
	public boolean canSave() {
		return true;
	}
	
	@Override
	public boolean isValid(ILaunchConfiguration launchConfig) {
		return true;
	}
}
