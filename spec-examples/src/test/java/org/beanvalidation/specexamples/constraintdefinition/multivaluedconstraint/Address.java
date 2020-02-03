/*
 * Jakarta Bean Validation: constrain once, validate everywhere.
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.beanvalidation.specexamples.constraintdefinition.multivaluedconstraint;

import jakarta.validation.groups.Default;

//tag::include[]
public class Address {
	@ZipCode(countryCode = "fr", groups = Default.class, message = "zip code is not valid")
	@ZipCode(
		countryCode = "fr",
		groups = SuperUser.class,
		message = "zip code invalid. Requires overriding before saving."
	)
	private String zipCode;
}
//end::include[]
