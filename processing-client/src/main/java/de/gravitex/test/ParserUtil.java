package de.gravitex.test;


public class ParserUtil {

	public static ProcessVariableDTO parseVariable(String variableDescription) throws Exception {
		System.out.println("parsing : " + variableDescription + ".");
		String[] tmp = variableDescription.split("#");
		return new ProcessVariableDTO(tmp[0], null, Class.forName(tmp[2]));
	}
}
