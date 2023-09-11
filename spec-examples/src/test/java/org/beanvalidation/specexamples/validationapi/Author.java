/*
 * Jakarta Validation: constrain once, validate everywhere.
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.beanvalidation.specexamples.validationapi;

import jakarta.validation.constraints.Size;

//tag::include[]
public class Author {

	private String firstName;

	@NonEmpty(message = "lastname must not be null")
	private String lastName;

	@Size(max = 30)
	private String company;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}
}
// end::include[]
