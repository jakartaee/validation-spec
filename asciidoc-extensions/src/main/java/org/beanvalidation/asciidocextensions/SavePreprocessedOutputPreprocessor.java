package org.beanvalidation.asciidocextensions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.asciidoctor.ast.Document;
import org.asciidoctor.extension.Preprocessor;
import org.asciidoctor.extension.PreprocessorReader;

/**
 * Preprocessor used to save the preprocessed output of the asciidoctor conversion. It allows to generate a single file
 * integrating all the includes.
 *
 * @author Guillaume Smet
 */
public class SavePreprocessedOutputPreprocessor extends Preprocessor {

	private static final String OUTPUT_FILE = "target/preprocessed/beanvalidation-specification-full.adoc";

	private static final List<String> FILTER_LICENSE_MARKERS = Arrays.asList( "[preface]", "<<<" );
	private static final String COMMENT_MARKER = "//";

	public SavePreprocessedOutputPreprocessor(Map<String, Object> config) {
		super( config );
	}

	@Override
	public PreprocessorReader process(Document document, PreprocessorReader reader) {
		try {
			Files.write( Paths.get( OUTPUT_FILE ), filterLines( reader.readLines() ) );
			return reader;
		}
		catch (IOException e) {
			throw new RuntimeException( "Unable to write the preprocessed file " + OUTPUT_FILE, e );
		}
	}

	/**
	 * This is used to filter out the license headers that are just after the markers defined.
	 *
	 * It's basic but it does the trick.
	 */
	private List<String> filterLines(List<String> lines) {
		List<String> filteredLines = new ArrayList<>();

		boolean filtered = false;
		for ( String line : lines ) {
			if ( FILTER_LICENSE_MARKERS.contains( line ) ) {
				filtered = true;
				filteredLines.add( line );
				continue;
			}

			if ( filtered ) {
				if ( line.startsWith( COMMENT_MARKER ) ) {
					continue;
				}
				else {
					filtered = false;
				}
			}

			filteredLines.add( line );
		}

		return filteredLines;
	}

}
