/*
 * Bean Validation: constrain once, validate everywhere.
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.beanvalidation.specexamples.constraintdefinition.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

//tag::include[]
/**
 * Check that a String begins with "SN-" and has a specified length.
 * <p>
 * Error messages are using either key:
 * <ul>
 *   <li>com.acme.constraint.SerialNumber.wrongprefix if the string doesn't begin with
 *   "SN-"</li>
 *   <li>com.acme.constraint.SerialNumber.wronglength if the string doesn't have the
 *   specified length</li>
 * </ul>
 */
public class SerialNumberValidator implements ConstraintValidator<SerialNumber, String> {

	private int length;

	/**
	 * Configure the constraint validator based on the elements specified at the time it was
	 * defined.
	 *
	 * @param constraint the constraint definition
	 */
	@Override
	public void initialize(SerialNumber constraint) {
		this.length = constraint.length();
	}

	/**
	 * Validate a specified value. returns false if the specified value does not conform to
	 * the definition.
	 */
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if ( value == null )
			return true;

		context.disableDefaultConstraintViolation();

		if ( !value.startsWith( "SN-" ) ) {
			String wrongPrefix = "{com.acme.constraint.SerialNumber.wrongprefix}";
			context.buildConstraintViolationWithTemplate( wrongPrefix )
					.addConstraintViolation();
			return false;
		}
		if ( value.length() != length ) {
			String wrongLength = "{com.acme.constraint.SerialNumber.wronglength}";
			context.buildConstraintViolationWithTemplate( wrongLength )
					.addConstraintViolation();
			return false;
		}
		return true;
	}
}
//end::include[]
