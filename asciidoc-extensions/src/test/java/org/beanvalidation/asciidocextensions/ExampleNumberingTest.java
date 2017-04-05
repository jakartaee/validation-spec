package org.beanvalidation.asciidocextensions;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.OptionsBuilder;
import org.asciidoctor.SafeMode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author Marko Bekhta
 */
public class ExampleNumberingTest {

	private static final Pattern EXAMPLE_TITLE_PATTERN = Pattern.compile( "Example [\\d]+\\.[\\d]+: .*" );

	private static Asciidoctor DOCTOR;

	@BeforeClass
	public static void initAsciidoctor() {
		DOCTOR = Asciidoctor.Factory.create();
		BeanValidationAsciidoctorExtensionRegistry registry = new BeanValidationAsciidoctorExtensionRegistry();
		registry.register( DOCTOR );
	}

	@Test
	public void simpleExampleNumberingTest() throws URISyntaxException, IOException {
		File file = Paths.get( ExampleNumberingTest.class.getClassLoader().getResource( "test.asciidoc" ).toURI() ).toFile();
		try ( FileReader reader = new FileReader( file ) ) {
			StringWriter writer = new StringWriter();
			DOCTOR.convert( reader, writer, OptionsBuilder.options().safe( SafeMode.SAFE ).asMap() );

			Document convertedDoc = Jsoup.parse( writer.toString() );

			Elements examples = convertedDoc.getElementsByClass( "exampleblock" );

			examples.forEach( this::assertExampleNumbering );

		}
	}

	private void assertExampleNumbering(Element example) {
		Element titleDiv = example.child( 0 );
		Assert.assertTrue( "Class should match", titleDiv.hasClass( "title" ) );
		Assert.assertTrue( "Example caption should match regexp",
				EXAMPLE_TITLE_PATTERN.matcher( titleDiv.html() ).matches()
		);

	}
}
