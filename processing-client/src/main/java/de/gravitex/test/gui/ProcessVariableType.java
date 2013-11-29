package de.gravitex.test.gui;

public enum ProcessVariableType {
	STRING,
	LONG,
	INTEGER,
	BOOLEAN;

	public Class<?> asClass() {
		switch (this) {
		case STRING:
			//------------------------
			return String.class;
			//------------------------
		case BOOLEAN:
			//------------------------
			return Boolean.class;
			//------------------------
		case INTEGER:
			//------------------------
			return Integer.class;
			//------------------------
		case LONG:
			//------------------------
			return Long.class;
			//------------------------
		}
		return null;
	}
}
