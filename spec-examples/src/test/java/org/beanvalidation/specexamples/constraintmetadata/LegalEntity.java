/*
 * Jakarta Bean Validation: constrain once, validate everywhere.
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.beanvalidation.specexamples.constraintmetadata;

import javax.validation.constraints.NotNull;

//tag::include[]
public interface LegalEntity {

	Iterable<@NotNull String> getRoles();
}
//end::include[]
