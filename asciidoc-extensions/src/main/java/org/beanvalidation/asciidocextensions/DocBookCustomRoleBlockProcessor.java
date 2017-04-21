/*
 * Bean Validation: constrain once, validate everywhere.
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.beanvalidation.asciidocextensions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.asciidoctor.ast.AbstractBlock;
import org.asciidoctor.ast.DocumentRuby;
import org.asciidoctor.extension.BlockProcessor;
import org.asciidoctor.extension.Reader;

/**
 * An AsciidoctorJ extension to preserve block tags such as "tck-testable" in the DocBook output. The sources for the
 * specification document of Bean Validation uses such tags to mark blocks and phrases which need to be matched by tests
 * from the TCK.
 *
 * @author Sanne Grinovero
 * @author Guillaume Smet
 */
public class DocBookCustomRoleBlockProcessor extends BlockProcessor {

	public DocBookCustomRoleBlockProcessor(String name, Map<String, Object> config) {
		// add the configuration here as we can't configure it using the ANT task
		super( name, addConfiguration( config ) );
	}

	@Override
	public Object process(AbstractBlock parent, Reader reader, Map<String, Object> attributes) {
		List<String> lines = reader.readLines();

		DocumentRuby document = parent.getDocument();
		String backend = String.valueOf( document.getAttr( "backend" ) );

		if ( !backend.startsWith( "docbook" ) ) {
			throw new IllegalStateException(
					DocBookCustomRoleBlockProcessor.class.getSimpleName() + " may only be used with the docbook backend. Backend is " + backend + "." );
		}

		HashMap<Object, Object> options = new HashMap<>();
		options.put( "content_model", ":compound" );

		attributes.put( "role", getName() );

		return createBlock( parent, "paragraph", lines, attributes, options );
	}

	private static Map<String, Object> addConfiguration(Map<String, Object> config) {
		config.put( "contexts", Arrays.asList( ":open", ":compound", ":simple", ":verbatim", ":paragraph", ":role" ) );
		return config;
	}
}
