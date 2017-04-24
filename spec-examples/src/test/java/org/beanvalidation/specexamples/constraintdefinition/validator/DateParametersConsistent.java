/*
 * Bean Validation: constrain once, validate everywhere.
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.beanvalidation.specexamples.constraintdefinition.validator;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

//tag::include[]
/**
 * Cross-parameter constraint ensuring that two date parameters of a method are in the correct order.
 */
@Documented
@Constraint(validatedBy = DateParametersConsistentValidator.class)
@Target({ METHOD, CONSTRUCTOR, ANNOTATION_TYPE })
@Retention(RUNTIME)
public @interface DateParametersConsistent {

	String message() default "{com.acme.constraint.DateParametersConsistent.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
// end::include[]
