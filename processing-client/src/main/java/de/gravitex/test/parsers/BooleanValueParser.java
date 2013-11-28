package de.gravitex.test.parsers;

public class BooleanValueParser extends AbstractValueParser {

	public Object parse(String value) {
		if ((value == null) || (value.length() == 0)) {
			return null;
		}
		value = value.toUpperCase();
		if (value.equals("TRUE")) {
			return Boolean.TRUE;
		} else if (value.equals("FALSE")) {
			return Boolean.FALSE;
		} else {
			return null;	
		}
	}
}
