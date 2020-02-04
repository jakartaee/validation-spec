/*
 * Jakarta Bean Validation: constrain once, validate everywhere.
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.beanvalidation.specexamples.constraintdefinition.validator;

import java.util.Date;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraintvalidation.SupportedValidationTarget;
import jakarta.validation.constraintvalidation.ValidationTarget;

//tag::include[]
/**
 * Check that two date parameters of a method are in the expected order. Expects the 2nd and
 * 3rd parameter of the validated method to be of type java.util.Date.
 */
@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class DateParametersConsistentValidator implements
		ConstraintValidator<DateParametersConsistent, Object[]> {

	/**
	 * Validate a specified value. returns false if the specified value does not conform to
	 * the definition
	 */
	@Override
	public boolean isValid(Object[] value, ConstraintValidatorContext context) {
		if ( value.length != 3 ) {
			throw new IllegalArgumentException( "Unexpected method signature" );
		}
		// one or both limits are unbounded => always consistent
		if ( value[1] == null || value[2] == null ) {
			return true;
		}
		return ( (Date) value[1] ).before( (Date) value[2] );
	}
}
//end::include[]
