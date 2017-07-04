/*
 * Bean Validation: constrain once, validate everywhere.
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.beanvalidation.specexamples.constraintmetadata;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static javax.validation.constraintvalidation.ValidationTarget.PARAMETERS;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import javax.validation.constraintvalidation.SupportedValidationTarget;

import org.beanvalidation.specexamples.constraintmetadata.ValidInterval.List;
//tag::include[]
@Documented
@Constraint(validatedBy = ValidInterval.Validator.class)
@Target({ METHOD, ANNOTATION_TYPE, CONSTRUCTOR })
@Retention(RUNTIME)
@Repeatable( List.class )
public @interface ValidInterval {

	String message() default "{com.acme.constraint.ValidInterval.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	int startParameter();

	int endParameter();

	@Target({ METHOD, ANNOTATION_TYPE, CONSTRUCTOR })
	@Retention(RUNTIME)
	@Documented
	@interface List {

		ValidInterval[] value();
	}

	@SupportedValidationTarget(PARAMETERS)
	class Validator implements ConstraintValidator<ValidInterval, Object[]> {

		private int start;
		private int end;

		@Override
		public void initialize(ValidInterval constraintAnnotation) {
			this.start = constraintAnnotation.startParameter();
			this.end = constraintAnnotation.endParameter();
		}

		@Override
		public boolean isValid(Object[] value, ConstraintValidatorContext context) {
			return Integer.parseInt( String.valueOf( value[start] ) ) <
					Integer.parseInt( String.valueOf( value[end] ) );
		}
	}
}
// end::include[]
