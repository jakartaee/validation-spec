/*
 * Jakarta Bean Validation: constrain once, validate everywhere.
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.beanvalidation.specexamples.constraintmetadata;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.groups.Default;
import jakarta.validation.metadata.BeanDescriptor;
import jakarta.validation.metadata.ConstraintDescriptor;
import jakarta.validation.metadata.ConstructorDescriptor;
import jakarta.validation.metadata.ContainerElementTypeDescriptor;
import jakarta.validation.metadata.CrossParameterDescriptor;
import jakarta.validation.metadata.GroupConversionDescriptor;
import jakarta.validation.metadata.MethodDescriptor;
import jakarta.validation.metadata.MethodType;
import jakarta.validation.metadata.ParameterDescriptor;
import jakarta.validation.metadata.PropertyDescriptor;
import jakarta.validation.metadata.ReturnValueDescriptor;

import org.beanvalidation.specexamples.constraintmetadata.Book.SecondLevelCheck;

/**
 * Example used in 6.12 (metadata example).
 *
 * @author Emmanuel Bernard
 * @author Gunnar Morling
 * @author Guillaume Smet
 */
public class MetaDataApiTest {

	public void testMeta() {
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

		//tag::include[]
		BeanDescriptor bookDescriptor = validator.getConstraintsForClass(Book.class);

		assert ! bookDescriptor.hasConstraints();

		assert bookDescriptor.isBeanConstrained();
		assert bookDescriptor.getConstrainedMethods( MethodType.NON_GETTER ).size() > 0;

		assert bookDescriptor.getConstraintDescriptors().size() == 0; //no bean-level constraint

		//more specifically "author", "title" and "keywordsPerChapter"
		assert bookDescriptor.getConstrainedProperties().size() == 2;

		//not a property
		assert bookDescriptor.getConstraintsForProperty( "doesNotExist" ) == null;

		//property with no constraint
		assert bookDescriptor.getConstraintsForProperty( "description" ) == null;

		PropertyDescriptor propertyDescriptor = bookDescriptor.getConstraintsForProperty( "title" );
		assert propertyDescriptor.getConstraintDescriptors().size() == 2;
		assert "title".equals( propertyDescriptor.getPropertyName() );

		//assuming the implementation returns the @NotEmpty constraint first
		ConstraintDescriptor<?> constraintDescriptor = propertyDescriptor.getConstraintDescriptors()
				.iterator().next();
		assert constraintDescriptor.getAnnotation().annotationType().equals( NotEmpty.class );
		assert constraintDescriptor.getGroups().size() == 2; //FirstLevelCheck and Default
		assert constraintDescriptor.getComposingConstraints().size() == 2;
		assert constraintDescriptor.isReportAsSingleViolation() == true;

		//@NotEmpty cannot be null
		boolean notNullPresence = false;
		for ( ConstraintDescriptor<?> composingDescriptor :
					constraintDescriptor.getComposingConstraints() ) {
			if ( composingDescriptor.getAnnotation().annotationType().equals( NotNull.class ) ) {
				notNullPresence = true;
			}
		}
		assert notNullPresence;

		//assuming the implementation returns the Size constraint second
		constraintDescriptor = propertyDescriptor.getConstraintDescriptors().iterator().next();
		assert constraintDescriptor.getAnnotation().annotationType().equals( Size.class );
		assert constraintDescriptor.getAttributes().get( "max" ) == Integer.valueOf( 30 );
		assert constraintDescriptor.getGroups().size() == 1;

		propertyDescriptor = bookDescriptor.getConstraintsForProperty( "author" );
		assert propertyDescriptor.getConstraintDescriptors().size() == 1;
		assert propertyDescriptor.isCascaded();

		//getTitle() and addChapter()
		assert bookDescriptor.getConstrainedMethods( MethodType.GETTER, MethodType.NON_GETTER ).size() ==
				2;

		//the constructor accepting title, description and author
		assert bookDescriptor.getConstrainedConstructors().size() == 1;

		ConstructorDescriptor constructorDescriptor = bookDescriptor.getConstraintsForConstructor(
				String.class, String.class, Author.class
		);
		assert constructorDescriptor.getName().equals( "Book" );
		assert constructorDescriptor.getElementClass() == Book.class;
		assert constructorDescriptor.hasConstrainedParameters() == true;

		//return value is marked for cascaded validation
		assert constructorDescriptor.hasConstrainedReturnValue() == true;

		//constraints are retrieved via the sub-descriptors for parameters etc.
		assert constructorDescriptor.hasConstraints() == false;

		//one descriptor for each parameter
		assert constructorDescriptor.getParameterDescriptors().size() == 3;

		//"description" parameter
		ParameterDescriptor parameterDescriptor = constructorDescriptor.getParameterDescriptors()
				.get( 1 );

		//assuming the default parameter name provider is used and parameter names can
		//be obtained
		assert parameterDescriptor.getName().equals( "description" );
		assert parameterDescriptor.getElementClass() == String.class;
		assert parameterDescriptor.getIndex() == 1;
		assert parameterDescriptor.hasConstraints() == true;

		Set<ConstraintDescriptor<?>> parameterConstraints =
				parameterDescriptor.getConstraintDescriptors();
		assert parameterConstraints.iterator().next().getAnnotation().annotationType() == Size.class;

		//"author" parameter
		parameterDescriptor = constructorDescriptor.getParameterDescriptors().get( 2 );
		assert parameterDescriptor.hasConstraints() == false;
		assert parameterDescriptor.isCascaded() == true;

		//group conversion on "author" parameter
		GroupConversionDescriptor groupConversion =
				parameterDescriptor.getGroupConversions().iterator().next();
		assert groupConversion.getFrom() == Default.class;
		assert groupConversion.getTo() == SecondLevelCheck.class;

		//constructor return value
		ReturnValueDescriptor returnValueDescriptor = constructorDescriptor.getReturnValueDescriptor();
		assert returnValueDescriptor.hasConstraints() == false;
		assert returnValueDescriptor.isCascaded() == true;

		//a getter is also a method which is constrained on its return value
		MethodDescriptor methodDescriptor = bookDescriptor.getConstraintsForMethod( "getTitle" );
		assert methodDescriptor.getName().equals( "getTitle" );
		assert methodDescriptor.getElementClass() == String.class;
		assert methodDescriptor.hasConstrainedParameters() == false;
		assert methodDescriptor.hasConstrainedReturnValue() == true;
		assert methodDescriptor.hasConstraints() == false;

		returnValueDescriptor = methodDescriptor.getReturnValueDescriptor();
		assert returnValueDescriptor.getElementClass() == String.class;
		assert returnValueDescriptor.getConstraintDescriptors().size() == 2;
		assert returnValueDescriptor.isCascaded() == false;

		//void method which has a cross-parameter constraint
		methodDescriptor = bookDescriptor.getConstraintsForMethod(
				"addChapter", String.class, int.class, int.class
		);
		assert methodDescriptor.getElementClass() == void.class;
		assert methodDescriptor.hasConstrainedParameters() == true;
		assert methodDescriptor.hasConstrainedReturnValue() == false;

		//cross-parameter constraints accessible via separate descriptor
		assert methodDescriptor.hasConstraints() == false;

		assert methodDescriptor.getReturnValueDescriptor().getElementClass() == void.class;

		//cross-parameter descriptor
		CrossParameterDescriptor crossParameterDescriptor =
				methodDescriptor.getCrossParameterDescriptor();
		assert crossParameterDescriptor.getElementClass() == Object[].class;
		assert crossParameterDescriptor.hasConstraints() == true;

		ConstraintDescriptor<?> crossParameterConstraint =
				crossParameterDescriptor.getConstraintDescriptors().iterator().next();
		assert crossParameterConstraint.getAnnotation().annotationType() == ValidInterval.class;

		// no constrained container element types for title
		assert bookDescriptor.getConstraintsForProperty( "title" )
				.getConstrainedContainerElementTypes().size() == 0;

		BeanDescriptor employeeImplDescriptor = validator.getConstraintsForClass( Employee.class );

		// container element constraints for property "roles"
		PropertyDescriptor rolesDescriptor = employeeImplDescriptor.getConstraintsForProperty( "roles" );
		assert rolesDescriptor != null;

		Set<ContainerElementTypeDescriptor> constrainedContainerElementTypes = rolesDescriptor
				.getConstrainedContainerElementTypes();
		// the container element types of Set and Iterable; Roles does not declare any container element types itself
		assert constrainedContainerElementTypes.size() == 2;

		Iterator<ContainerElementTypeDescriptor> it = constrainedContainerElementTypes.iterator();

		// assuming that the descriptor for Set is returned first
		ContainerElementTypeDescriptor containerElementTypeDescriptor = it.next();
		assert containerElementTypeDescriptor.getContainerClass() == Set.class;
		assert containerElementTypeDescriptor.getTypeArgumentIndex() == 0;
		assert containerElementTypeDescriptor.getElementClass() == String.class;
		// @NotEmpty and @NotBlank
		assert containerElementTypeDescriptor.getConstraintDescriptors().size() == 2;

		// assuming that the descriptor for Iterable is returned next
		containerElementTypeDescriptor = it.next();
		assert containerElementTypeDescriptor.getContainerClass() == Iterable.class;
		assert containerElementTypeDescriptor.getTypeArgumentIndex() == 0;
		assert containerElementTypeDescriptor.getElementClass() == String.class;
		// @NotNull
		assert containerElementTypeDescriptor.getConstraintDescriptors().size() == 1;

		// container element constraints for property "accounts"
		PropertyDescriptor accountsDescriptor = employeeImplDescriptor
				.getConstraintsForProperty( "accounts" );
		constrainedContainerElementTypes = accountsDescriptor.getConstrainedContainerElementTypes();
		// the map key type and the map value type
		assert constrainedContainerElementTypes.size() == 2;

		it = constrainedContainerElementTypes.iterator();

		// assuming that the descriptor for the map key is returned first
		containerElementTypeDescriptor = it.next();
		assert containerElementTypeDescriptor.getContainerClass() == Map.class;
		assert containerElementTypeDescriptor.getTypeArgumentIndex() == 0;
		assert containerElementTypeDescriptor.getElementClass() == String.class;
		// @NotNull
		assert containerElementTypeDescriptor.getConstraintDescriptors().size() == 1;
		assert containerElementTypeDescriptor.isCascaded() == false;

		// assuming that the descriptor for the map value is returned next
		containerElementTypeDescriptor = it.next();
		assert containerElementTypeDescriptor.getContainerClass() == Map.class;
		assert containerElementTypeDescriptor.getTypeArgumentIndex() == 1;
		assert containerElementTypeDescriptor.getElementClass() == Account.class;
		assert containerElementTypeDescriptor.getConstraintDescriptors().size() == 0;
		assert containerElementTypeDescriptor.isCascaded() == true;

		// container element constraints for property "addresses"
		PropertyDescriptor addressesDescriptor = employeeImplDescriptor
				.getConstraintsForProperty( "addresses" );
		constrainedContainerElementTypes = addressesDescriptor.getConstrainedContainerElementTypes();
		// the map value type
		assert constrainedContainerElementTypes.size() == 1;

		it = constrainedContainerElementTypes.iterator();

		containerElementTypeDescriptor = it.next();
		assert containerElementTypeDescriptor.getContainerClass() == Map.class;
		assert containerElementTypeDescriptor.getTypeArgumentIndex() == 1;
		assert containerElementTypeDescriptor.getElementClass() == List.class;
		// No constraints nor @Valid on List itself
		assert containerElementTypeDescriptor.getConstraintDescriptors().size() == 0;
		assert containerElementTypeDescriptor.isCascaded() == false;

		// container element type of the nested List container
		constrainedContainerElementTypes = containerElementTypeDescriptor.getConstrainedContainerElementTypes();
		assert constrainedContainerElementTypes.size() == 1;
		it = constrainedContainerElementTypes.iterator();

		containerElementTypeDescriptor = it.next();
		assert containerElementTypeDescriptor.getContainerClass() == List.class;
		assert containerElementTypeDescriptor.getTypeArgumentIndex() == 0;
		assert containerElementTypeDescriptor.getElementClass() == Address.class;
		// @NotNull and @ValidAddress
		assert containerElementTypeDescriptor.getConstraintDescriptors().size() == 2;
		assert containerElementTypeDescriptor.isCascaded() == true;
		//end::include[]
	}
}
