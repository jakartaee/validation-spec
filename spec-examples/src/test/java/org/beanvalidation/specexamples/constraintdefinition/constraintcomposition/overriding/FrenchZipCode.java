/*
 * Bean Validation: constrain once, validate everywhere.
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.beanvalidation.specexamples.constraintdefinition.constraintcomposition.overriding;

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

import javax.validation.Constraint;
import javax.validation.OverridesAttribute;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.beanvalidation.specexamples.constraintdefinition.constraintcomposition.FrenchZipCodeValidator;

//tag::include[]
@Pattern(regexp = "[0-9]*")
@Size
@Constraint(validatedBy = FrenchZipCodeValidator.class)
@Documented
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
public @interface FrenchZipCode {

	String message() default "Wrong zip code";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	@OverridesAttribute(constraint = Size.class, name = "min")
	@OverridesAttribute(constraint = Size.class, name = "max")
	int size() default 5;

	@OverridesAttribute(constraint = Size.class, name = "message")
	String sizeMessage() default "{com.acme.constraint.FrenchZipCode.zipCode.size}";

	@OverridesAttribute(constraint = Pattern.class, name = "message")
	String numberMessage() default "{com.acme.constraint.FrenchZipCode.number.size}";

	@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
	@Retention(RUNTIME)
	@Documented
	@interface List {

		FrenchZipCode[] value();
	}
}
// end::include[]
