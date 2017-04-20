/*
 * Bean Validation: constrain once, validate everywhere.
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.beanvalidation.specexamples.constraintdefinition.constraintcomposition;

import javax.validation.ConstraintValidator;

public abstract class FrenchZipCodeValidator implements ConstraintValidator<FrenchZipCode, String> {

}
