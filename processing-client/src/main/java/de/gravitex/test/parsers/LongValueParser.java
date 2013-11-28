package de.gravitex.test.parsers;

public class LongValueParser extends AbstractValueParser {

	public Object parse(String value) {
		if ((value == null) || (value.length() == 0)) {
			return null;
		}
		return Long.parseLong(value);
	}
}
