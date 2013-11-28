package de.gravitex.test;

import java.util.HashMap;

import de.gravitex.test.parsers.AbstractValueParser;
import de.gravitex.test.parsers.BooleanValueParser;
import de.gravitex.test.parsers.IntegerValueParser;
import de.gravitex.test.parsers.StringValueParser;


public class ProcessingParserUtil {
	
	private static HashMap<Class<?>, AbstractValueParser> parsers = new HashMap<>();
	static {
		parsers.put(Boolean.class, new BooleanValueParser());
		parsers.put(Integer.class, new IntegerValueParser());
		parsers.put(String.class, new StringValueParser());
	}

	public static ProcessVariableDTO parseVariable(String variableDescription) throws Exception {
		System.out.println("parsing : " + variableDescription + ".");
		String[] tmp = variableDescription.split("#");
		return new ProcessVariableDTO(tmp[0], tmp[1], Class.forName(tmp[2]));
	}

	public static Object parseValue(String value, Class<?> fieldClass) {
		return parsers.get(fieldClass).parse(value);
	}
}
