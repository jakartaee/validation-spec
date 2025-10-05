/*
 * Jakarta Validation: constrain once, validate everywhere.
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.beanvalidation.specexamples.constraintmetadata;

import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

//tag::include[]
public interface Employee extends Person {

	@Override
	Set<@NotBlank String> getRoles();

	Map<String, List<@NotNull @Valid Address>> getAddresses();
}
// end::include[]
