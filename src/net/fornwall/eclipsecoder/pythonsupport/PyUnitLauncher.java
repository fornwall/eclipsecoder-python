package net.fornwall.eclipsecoder.pythonsupport;

import org.eclipse.core.resources.IFile;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.python.pydev.debug.core.Constants;
import org.python.pydev.plugin.PydevPlugin;

import net.fornwall.eclipsecoder.util.AbstractLauncher;

public class PyUnitLauncher extends AbstractLauncher {

	private IFile unitTestFile;

	public PyUnitLauncher(IFile unitTestFile) {
		this.unitTestFile = unitTestFile;
	}

	@Override
	protected String getLauncherName() {
		return unitTestFile.getName();
	}

	@Override
	protected String getLauncherTypeId() {
		return Constants.ID_PYTHON_UNITTEST_LAUNCH_CONFIGURATION_TYPE;
	}

	@Override
	protected void setUpConfiguration(ILaunchConfigurationWorkingCopy config) throws Exception {
		String fileLocation = "${workspace_loc:" + unitTestFile.getProject().getName() + "/" + unitTestFile.getName()
				+ "}";
		String workingDir = "${workspace_loc:" + unitTestFile.getProject().getName() + "}";

		config.setAttribute(Constants.ATTR_LOCATION, fileLocation);
		config.setAttribute(Constants.ATTR_WORKING_DIRECTORY, workingDir);
		config.setAttribute(Constants.ATTR_PROJECT, unitTestFile.getProject().getName());
		config.setAttribute(Constants.ATTR_INTERPRETER, Constants.ATTR_INTERPRETER_DEFAULT);
	}
}
