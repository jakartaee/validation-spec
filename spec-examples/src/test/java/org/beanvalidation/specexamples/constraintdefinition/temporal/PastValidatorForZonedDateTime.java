/*
 * Jakarta Bean Validation: constrain once, validate everywhere.
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.beanvalidation.specexamples.constraintdefinition.temporal;

import java.time.ZonedDateTime;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraints.Past;

//tag::include[]
/**
 * Validates that the given {@link ZonedDateTime} is in the past.
 */
public class PastValidatorForZonedDateTime implements ConstraintValidator<Past, ZonedDateTime> {

	@Override
	public boolean isValid(ZonedDateTime value, ConstraintValidatorContext context) {
		if ( value == null ) {
			return true;
		}

		ZonedDateTime now = ZonedDateTime.now( context.getClockProvider().getClock() );

		return value.isBefore( now );
	}
}
//end::include[]
