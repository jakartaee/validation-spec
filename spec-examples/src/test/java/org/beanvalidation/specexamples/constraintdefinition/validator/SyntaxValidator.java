package org.beanvalidation.specexamples.constraintdefinition.validator;

/*
 * Bean Validation: constrain once, validate everywhere.
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
import java.text.Format;
import java.util.Collections;
import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

//tag::include[]
/**
 * Check that a text is within the authorized syntax.
 */
public class SyntaxValidator implements ConstraintValidator<Syntax, String> {

	private Set<Format> allowedFormats;

	/**
	 * Configure the constraint validator based on the elements specified at the time it was defined.
	 *
	 * @param constraint the constraint definition
	 */
	@Override
	public void initialize(Syntax constraint) {
		allowedFormats = convertToFormatSet( constraint.value() );
	}

	/**
	 * Validate a specified value. returns false if the specified value does not conform to the definition
	 */
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if ( value == null )
			return true;

		return allowedFormats.size() == 0
				|| ( !Collections.disjoint( guessFormats( value ), allowedFormats ) );
	}

	private Set<Format> convertToFormatSet(String[] value) {
		// [...]
		return null;
	}

	private Set<Format> guessFormats(String text) {
		// [...]
		return null;
	}
}
//end::include[]
