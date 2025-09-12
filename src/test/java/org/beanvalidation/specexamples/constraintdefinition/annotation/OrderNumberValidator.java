/*
 * Jakarta Validation: constrain once, validate everywhere.
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.beanvalidation.specexamples.constraintdefinition.annotation;

import jakarta.validation.ConstraintValidator;

abstract class OrderNumberValidator implements ConstraintValidator<OrderNumber, String> {
}
