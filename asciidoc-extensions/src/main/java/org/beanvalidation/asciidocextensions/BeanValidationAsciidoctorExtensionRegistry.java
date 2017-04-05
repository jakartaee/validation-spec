package org.beanvalidation.asciidocextensions;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.extension.JavaExtensionRegistry;
import org.asciidoctor.extension.spi.ExtensionRegistry;
import org.beanvalidation.asciidocextensions.extensions.CustomRoleBlockProcessor;

/**
 * @author Marko Bekhta
 */
public class BeanValidationAsciidoctorExtensionRegistry implements ExtensionRegistry {

	@Override public void register(Asciidoctor asciidoctor) {
		JavaExtensionRegistry javaExtensionRegistry = asciidoctor.javaExtensionRegistry();
		asciidoctor.rubyExtensionRegistry().loadClass(
				BeanValidationAsciidoctorExtensionRegistry.class.getClassLoader().getResourceAsStream( "ExampleNumberingProcessor.rb" ) );
		asciidoctor.rubyExtensionRegistry().treeprocessor( "ExampleNumberingProcessor" );
		javaExtensionRegistry.block( "tck-testable", CustomRoleBlockProcessor.class );
		javaExtensionRegistry.block( "tck-not-testable", CustomRoleBlockProcessor.class );
		javaExtensionRegistry.block( "tck-ignore", CustomRoleBlockProcessor.class );
	}
}
