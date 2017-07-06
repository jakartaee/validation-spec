/*
 * Bean Validation: constrain once, validate everywhere.
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.beanvalidation.specexamples.validationapi;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ElementKind;
import javax.validation.Path.Node;
import javax.validation.Validation;
import javax.validation.Validator;

/**
 * @author Gunnar Morling
 *
 */
public class ValidationApiTest {

	public void testValidationApi() {
		//tag::invocation[]
		Author author = new Author();
		author.setCompany( "ACME" );

		List<String> tags = Arrays.asList( "a", "science fiction" );

		Book book = new Book();
		book.setTitle( "" );
		book.setAuthor( author );

		book.setTags( tags );

		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<Book>> constraintViolations = validator.validate( book );
		//end::invocation[]

		for ( ConstraintViolation<Book> constraintViolation : constraintViolations ) {

			if ( constraintViolation.getPropertyPath().toString().contains( "title" ) ) {
				//tag::title[]
				//assuming an english locale, the interpolated message is returned
				assert "may not be null or empty".equals( constraintViolation.getMessage() );
				assert book == constraintViolation.getRootBean();
				assert book == constraintViolation.getLeafBean();

				//the offending value
				assert book.getTitle().equals( constraintViolation.getInvalidValue() );

				//the offending property
				Iterator<Node> nodeIter = constraintViolation.getPropertyPath().iterator();
				Node node = nodeIter.next();
				assert "title".equals( node.getName() );
				assert ElementKind.PROPERTY.equals( node.getKind() );

				assert false == nodeIter.hasNext();
				//end::title[]
			}
			else if ( constraintViolation.getPropertyPath().toString().contains( "lastName" ) ) {
				//tag::lastName[]
				assert "lastname must not be null".equals( constraintViolation.getMessage() );
				assert book == constraintViolation.getRootBean();
				assert author == constraintViolation.getLeafBean();

				//the offending value
				assert book.getAuthor().getLastName() == constraintViolation.getInvalidValue();

				//the offending property
				Iterator<Node> nodeIter = constraintViolation.getPropertyPath().iterator();

				Node node = nodeIter.next();
				assert "author".equals( node.getName() );
				assert ElementKind.PROPERTY.equals( node.getKind() );

				node = nodeIter.next();
				assert "lastName".equals( node.getName() );
				assert ElementKind.PROPERTY.equals( node.getKind() );

				assert false == nodeIter.hasNext();
				//end::lastName[]
			}
			else if ( constraintViolation.getPropertyPath().toString().contains( "tags" ) ) {
				//tag::tags[]
				assert "size must be between 3 and 30".equals( constraintViolation.getMessage() );
				assert book == constraintViolation.getRootBean();
				assert book == constraintViolation.getLeafBean();

				//the offending value
				assert book.getTags().get( 0 ) == constraintViolation.getInvalidValue();

				//the offending property
				Iterator<Node> nodeIter = constraintViolation.getPropertyPath().iterator();

				Node node = nodeIter.next();
				assert "tags".equals( node.getName() );
				assert ElementKind.PROPERTY.equals( node.getKind() );

				node = nodeIter.next();
				assert "<list element>".equals( node.getName() );
				assert ElementKind.CONTAINER_ELEMENT.equals( node.getKind() );

				assert false == nodeIter.hasNext();
				//end::tags[]
			}
		}
	}
}
