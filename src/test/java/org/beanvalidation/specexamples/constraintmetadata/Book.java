/*
 * Jakarta Validation: constrain once, validate everywhere.
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.beanvalidation.specexamples.constraintmetadata;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.groups.ConvertGroup;
import jakarta.validation.groups.Default;

//tag::include[]
public class Book {

	public interface FirstLevelCheck {
	}

	public interface SecondLevelCheck {
	}

	private String title;
	private String description;

	@Valid
	@NotNull
	private Author author;

	@Valid
	public Book(
			String title,
			@Size(max = 30) String description,
			@Valid @ConvertGroup(from = Default.class, to = SecondLevelCheck.class) Author author) {
		// [...]
	}

	public Book() {
		// [...]
	}

	@NotEmpty(groups = { FirstLevelCheck.class, Default.class })
	@Size(max = 30)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public String getDescription() {
		return description;
	}

	public void setAuthor(String description) {
		this.description = description;
	}

	@ValidInterval(startParameter = 1, endParameter = 2)
	public void addChapter(String title, int startPage, int endPage) {
		// [...]
	}
}
//end::include[]
