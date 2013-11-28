package de.gravitex.test;

public class ProcessVariableDTO {
	
	public ProcessVariableDTO(String fieldName, Object fieldValue, Class<?> fieldClass) {
		super();
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
		this.fieldClass = fieldClass;
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
