/*
 * Jakarta Bean Validation: constrain once, validate everywhere.
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.beanvalidation.specexamples.constraintmetadata;

import java.util.List;
import java.util.Map;

//tag::include[]
public class EmployeeImpl implements Employee {

	@Override
	public Roles getRoles() {
		// [...]
		//end::include[]
		return null;
		//tag::include[]
	}

	@Override
	public Map<String, List<@ValidAddress Address>> getAddresses() {
		// [...]
		//end::include[]
		return null;
		//tag::include[]
	}

	@Override
	public Map<String, Account> getAccounts() {
		// [...]
		//end::include[]
		return null;
		//tag::include[]
	}
}
//end::include[]
