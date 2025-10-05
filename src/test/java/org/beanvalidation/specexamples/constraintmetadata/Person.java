/*
 * Jakarta Validation: constrain once, validate everywhere.
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.beanvalidation.specexamples.constraintmetadata;

import java.util.Map;
import java.util.Set;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

//tag::include[]
public interface Person extends LegalEntity {

	@Override
	Set<@NotEmpty String> getRoles();

	Map<@NotNull String, @Valid Account> getAccounts();
}
//end::include[]
