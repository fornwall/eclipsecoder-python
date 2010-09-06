package net.fornwall.eclipsecoder.pythonsupport;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class PythonSupportPlugin extends AbstractUIPlugin {

	private static PythonSupportPlugin plugin;

	public static PythonSupportPlugin getDefault() {
		return plugin;
	}

	/**
	 * The constructor.
	 */
	public PythonSupportPlugin() {
		plugin = this;
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
	}
}
