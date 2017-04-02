/*
 * Bean Validation: constrain once, validate everywhere.
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.beanvalidation.asciidocextensions.extensions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.asciidoctor.ast.AbstractBlock;
import org.asciidoctor.ast.DocumentRuby;
import org.asciidoctor.extension.BlockProcessor;
import org.asciidoctor.extension.Reader;

/**
 * An AsciidoctorJ extension to preserve block tags such as "tck-testable".
 * The sources for the specification document of Bean Validation uses such
 * tags to mark blocks and phrases which need to be matched by tests from the TCK.
 *
 * @author Sanne Grinovero
 */
public class CustomRoleBlockProcessor extends BlockProcessor {

	public CustomRoleBlockProcessor(String name, Map<String, Object> config) {
		//Override the config map as we can't configure it using the ANT task
		super( name, initialConfig( config ) );
	}

	@Override
	public Object process(AbstractBlock parent, Reader reader, Map<String, Object> attributes) {
		List<String> lines = reader.readLines();
		HashMap<Object, Object> options = new HashMap<>();
		options.put( "content_model", ":compound" );
		DocumentRuby document = parent.getDocument();
		String attr = String.valueOf( document.getAttr( "backend" ) );

		if ( !"pdf".equals( attr ) ) {
			//'paragraph' generates simple-para: you can't nest them when rendering PDF.
			attributes.put( "role", getName() );
			AbstractBlock block = createBlock( parent, "paragraph", lines, attributes, options );
			return block;
		}
		//.. so use an 'open' block for PDF.
		return createBlock( parent, "open", lines, attributes, options );
	}

	private static Map<String, Object> initialConfig(Map<String, Object> config) {
		config.put( "contexts", Arrays.asList( ":open", ":compound", ":simple", ":verbatim", ":paragraph", ":role" ) );
		return config;
	}

}
