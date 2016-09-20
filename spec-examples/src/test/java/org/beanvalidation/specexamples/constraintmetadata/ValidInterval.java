package org.beanvalidation.specexamples.constraintmetadata;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static javax.validation.constraintvalidation.ValidationTarget.PARAMETERS;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import javax.validation.constraintvalidation.SupportedValidationTarget;

//tag::include[]
@Documented
@Constraint(validatedBy = ValidInterval.Validator.class)
@Target({ METHOD, ANNOTATION_TYPE, CONSTRUCTOR })
@Retention(RUNTIME)
public @interface ValidInterval {

	String message() default "{com.acme.constraint.ValidInterval.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	int startParameter();

	int endParameter();

	@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
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
			return Integer.parseInt( String.valueOf( value[start] ) ) < Integer.parseInt( String.valueOf( value[end] ) );
		}
	}
}
// end::include[]
