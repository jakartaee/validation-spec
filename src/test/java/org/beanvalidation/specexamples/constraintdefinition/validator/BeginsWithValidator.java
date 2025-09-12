/*
 * Jakarta Validation: constrain once, validate everywhere.
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.beanvalidation.specexamples.constraintdefinition.validator;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toSet;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

//tag::include[]
/**
 * Check that a String begins with one of the given prefixes.
 */
public class BeginsWithValidator implements ConstraintValidator<BeginsWith, String> {

	private Set<String> allowedPrefixes;

	/**
	 * Configure the constraint validator based on the elements specified at the time it was
	 * defined.
	 *
	 * @param constraint the constraint definition
	 */
	@Override
	public void initialize(BeginsWith constraint) {
		allowedPrefixes = Arrays.stream( constraint.value() )
				.collect( collectingAndThen( toSet(), Collections::unmodifiableSet ) );
	}

	/**
	 * Validate a specified value. returns false if the specified value does not conform to
	 * the definition.
	 */
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if ( value == null )
			return true;

		return allowedPrefixes.stream()
				.anyMatch( value::startsWith );
	}
}
//end::include[]
