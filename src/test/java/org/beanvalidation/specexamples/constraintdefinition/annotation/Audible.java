/*
 * Jakarta Validation: constrain once, validate everywhere.
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.beanvalidation.specexamples.constraintdefinition.annotation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

//tag::include[]
/**
 * A frequency in Hz as audible to human ear. Adjustable to the age of the person. Accepts
 * Numbers.
 */
@Documented
@Constraint(validatedBy = AudibleValidator.class)
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
public @interface Audible {

	Age age() default Age.YOUNG;

	String message() default "{com.acme.constraint.Audible.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	public enum Age {
		YOUNG,
		WONDERING,
		OLD
	}
}
// end::include[]
