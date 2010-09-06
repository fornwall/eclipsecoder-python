package net.fornwall.eclipsecoder.pythonsupport;

import java.io.ByteArrayInputStream;

import net.fornwall.eclipsecoder.languages.LanguageSupport;
import net.fornwall.eclipsecoder.stats.CodeGenerator;
import net.fornwall.eclipsecoder.stats.ProblemStatement;
import net.fornwall.eclipsecoder.util.Utilities;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.python.pydev.core.IPythonNature;
import org.python.pydev.editor.PyEdit;
import org.python.pydev.plugin.nature.PythonNature;
import org.python.pydev.ui.perspective.PythonPerspectiveFactory;

public class PythonLanguageSupport extends LanguageSupport {

	@Override
	protected CodeGenerator createCodeGenerator(ProblemStatement problemStatemnt) {
		return new PythonCodeGenerator(problemStatemnt);
	}

	@Override
	protected IFile createLanguageProject(final IProject project) throws Exception {
		IPythonNature nature = PythonNature.addNature(project, null, null, null, null, null, null);
		nature.getPythonPathNature().setProjectSourcePath(".");

		final String testModuleName = getProblemStatement().getSolutionClassName() + "Test";
		final String testFileName = testModuleName + ".py";
		final IFile testsFile = project.getFile(testFileName);
		testsFile.create(new ByteArrayInputStream(getCodeGenerator().getTestsSource().getBytes()), true, null);

		IFile sourceFile = project.getFile(getSolutionFileName());
		sourceFile.create(new ByteArrayInputStream(getInitialSource().getBytes()), true, null);

		Utilities.buildAndRun(project, new PyUnitLauncher(testsFile));
		return sourceFile;
	}

	@Override
	protected String getCodeEditorID() {
		return PyEdit.EDITOR_ID;
	}

	@Override
	protected String getCodeTemplate() {
		return "class $CLASSNAME$:\n\n\tdef $METHODNAME$($METHODPARAMS$):\n\t\treturn $DUMMYRETURN$\n";
	}

	@Override
	public String getLanguageName() {
		return LanguageSupport.LANGUAGE_NAME_PYTHON;
	}

	@Override
	public String getPerspectiveID() {
		return PythonPerspectiveFactory.PERSPECTIVE_ID;
	}

	@Override
	protected String getSolutionFileName() {
		return getProblemStatement().getSolutionClassName() + ".py";
	}

	@Override
	protected String getSubmission() throws Exception {
		throw new UnsupportedOperationException("Python support is only for practising");
	}

}
