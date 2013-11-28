package de.gravitex.test;

public class ProcessVariableDTO {
	
	public ProcessVariableDTO(String fieldName, String fieldValueAsString, Class<?> fieldClass) {
		super();
		this.fieldName = fieldName;
		this.fieldClass = fieldClass;
		this.fieldValue = ProcessingParserUtil.parseValue(fieldValueAsString, fieldClass);
	}

	private String fieldName;
	
	private Object fieldValue;
	
	private Class<?> fieldClass;

	public String getFieldName() {
		return fieldName;
	}

	public Object getFieldValue() {
		return fieldValue;
	}

	public Class<?> getFieldClass() {
		return fieldClass;
	}
}
