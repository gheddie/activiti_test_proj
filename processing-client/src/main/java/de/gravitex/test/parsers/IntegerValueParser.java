package de.gravitex.test.parsers;

public class IntegerValueParser extends AbstractValueParser {

	public Object parse(String value) {
		if ((value == null) || (value.length() == 0)) {
			return null;
		}
		return Integer.parseInt(value);
	}
}
