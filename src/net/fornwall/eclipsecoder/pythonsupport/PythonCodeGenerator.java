package net.fornwall.eclipsecoder.pythonsupport;

import java.lang.reflect.Array;

import net.fornwall.eclipsecoder.stats.CodeGenerator;
import net.fornwall.eclipsecoder.stats.ProblemStatement;

public class PythonCodeGenerator extends CodeGenerator {

	public PythonCodeGenerator(ProblemStatement problemStatement) {
		super(problemStatement);
	}

	private String getCreateStatement(Object value, String variableName, Class<?> type) {
		return variableName + " = " + getValueString(value, type) + "\n";
	}

	@Override
	public String getDummyReturnString() {
		return "None";
	}

	@Override
	protected String getMethodParamsString() {
		// with self first parameter
		StringBuilder builder = new StringBuilder("self");
		for (String name : problemStatement.getParameterNames()) {
			builder.append(", ");
			builder.append(name);
		}
		return builder.toString();
	}

	@Override
	public String getTestsSource() {
		StringBuilder builder = new StringBuilder("import " + problemStatement.getSolutionClassName()
				+ "\nimport unittest\n");
		if (problemStatement.getReturnType() == Double.class) {
			builder.append("import math\n");
		}
		builder.append("\nclass " + problemStatement.getSolutionClassName() + "Test(unittest.TestCase):\n\n");
		if (problemStatement.getReturnType() == Double.class) {
			String fail = "self.fail(\"'\" + str(expected) + \"' != '\" + str(actual) + \"'\")";
			builder.append("\tdef assertDoubleEqual(self, expected, actual):\n");
			builder.append("\t\ttry:\n");
			builder.append("\t\t\tdelta = max(1e-9, 1e-9 * abs(expected))\n");
			builder.append("\t\t\tif abs(expected - actual) > delta:\n");
			builder.append("\t\t\t\t" + fail + "\n");
			builder.append("\t\texcept TypeError:\n");
			builder.append("\t\t\t" + fail);
			builder.append("\n\n");
		}
		builder.append("\tdef setUp(self):\n\t\t" + "self.solution = " + problemStatement.getSolutionClassName() + "."
				+ problemStatement.getSolutionClassName() + "()\n");
		builder.append("\n");

		int count = 0;
		for (ProblemStatement.TestCase testCase : problemStatement.getTestCases()) {
			builder.append("\tdef test_" + count + "(self):\n");

			for (int i = 0; i < testCase.getParameters().length; i++) {
				builder.append("\t\t");
				builder.append(getCreateStatement(testCase.getParameters()[i], problemStatement.getParameterNames()
						.get(i), problemStatement.getParameterTypes().get(i)));
			}

			builder.append("\n\t\t");
			builder.append(getCreateStatement(testCase.returnValue, "expected", problemStatement.getReturnType()));
			builder.append("\t\t");

			builder.append("actual = self.solution." + problemStatement.getSolutionMethodName() + "(");
			for (int i = 0; i < problemStatement.getParameterNames().size(); i++) {
				if (i != 0)
					builder.append(", ");
				builder.append(problemStatement.getParameterNames().get(i));
			}
			builder.append(")\n");

			String maybeDouble = (problemStatement.getReturnType() == Double.class) ? "Double" : "";
			builder.append("\n\t\tself.assert" + maybeDouble + "Equal(expected, actual)\n\n");
			count++;
		}

		builder.append("if __name__ == '__main__': unittest.main()\n");

		return builder.toString();
	}

	@Override
	public String getTypeString(Class<?> type) {
		return "";
	}

	private String getValueString(Object value, Class<?> type) {
		if (type == Integer.class || type == Long.class || type == Double.class) {
			return value.toString();
		} else if (type == String.class) {
			return "'" + value.toString() + "'";
		} else if (type.isArray()) {
			StringBuilder builder = new StringBuilder("[");
			for (int i = 0; i < Array.getLength(value); i++) {
				if (i != 0)
					builder.append(", ");
				builder.append(getValueString(Array.get(value, i), type.getComponentType()));
			}
			builder.append("]");
			return builder.toString();
		} else if (type == Character.class) {
			String escapedValue;
			char c = (Character) value;
			switch (c) {
			case '\'':
				escapedValue = "\\'";
				break;
			case '\\':
				escapedValue = "\\\\";
				break;
			default:
				escapedValue = Character.toString(c);
			}
			return "'" + escapedValue + "'";
		} else {
			throw new IllegalArgumentException("Got type \"" + type + "\"");
		}
	}

}
