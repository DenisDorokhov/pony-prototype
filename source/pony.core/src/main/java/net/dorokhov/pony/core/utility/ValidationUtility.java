package net.dorokhov.pony.core.utility;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

public class ValidationUtility {

	public static void validate(Object aObject, Validator aValidator) throws ConstraintViolationException {
		
		Set<ConstraintViolation<Object>> violations = aValidator.validate(aObject);
		
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
	}
	
}
