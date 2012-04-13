/**
 * 
 */
package test.support;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.custommonkey.xmlunit.XMLAssert;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.knitml.core.model.pattern.Pattern;
import com.knitml.core.xml.EntityResolverWrapper;
import com.knitml.core.xml.PluggableSchemaResolver;
import com.knitml.core.xml.Schemas;

public class JiBXTestUtils {

	public static Object unmarshalXml(String xml, Class<?> rootClass)
			throws JiBXException {
		IBindingFactory factory = getBindingFactory(rootClass);
		IUnmarshallingContext uctx = factory.createUnmarshallingContext();
		return uctx.unmarshalDocument(new StringReader(xml));
	}

	public static Object unmarshalXml(String xml) throws JiBXException {
		return unmarshalXml(xml, Pattern.class);
	}

	private static IBindingFactory getBindingFactory(Class<?> rootClass)
			throws JiBXException {
		return BindingDirectory.getFactory(rootClass);
	}

	public static void marshalXmlAndCompare(Object object, String xml)
			throws JiBXException, SAXException, IOException, ParserConfigurationException {
		IMarshallingContext mctx = getBindingFactory(object.getClass())
				.createMarshallingContext();
		StringWriter writer = new StringWriter();
		mctx.setOutput(writer);
		mctx.marshalDocument(object);
		
//		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
//        domFactory.setNamespaceAware(true); // never forget this
//        DocumentBuilder builder = domFactory.newDocumentBuilder();
//        Document doc = builder.parse(new InputSource(new StringReader(writer.toString())));
        
//		checkSyntax(new DOMSource(doc));
		XMLAssert.assertXMLEqual(xml, writer.toString());
	}
	
	protected static void checkSyntax(Source source) throws IOException, SAXException {

		EntityResolver entityResolver = new PluggableSchemaResolver(JiBXTestUtils.class.getClassLoader());
		LSResourceResolver resourceResolver = new EntityResolverWrapper(
				entityResolver);

		// create a SchemaFactory capable of understanding XML Schema
		SchemaFactory factory = SchemaFactory
				.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
//		
		//-Djavax.xml.validation.SchemaFactory:http://www.w3.org/2001/XMLSchema=org.apache.xerces.jaxp.validation.XMLSchemaFactory
//		SchemaFactory factory = new XMLSchemaFactory();
		
		// set a strategy to validate included XML schemas
		factory.setResourceResolver(resourceResolver);

		// set primary XML to validate
		InputSource patternSchema = entityResolver.resolveEntity(null,
				Schemas.CURRENT_PATTERN_SCHEMA);
		Source patternSchemaSource = new StreamSource(patternSchema
				.getByteStream());
		Schema schema = factory.newSchema(patternSchemaSource);

		// create a Validator instance to validate the document
		Validator validator = schema.newValidator();
		
		// validate the tree
		validator.validate(source);
	}
	
}
