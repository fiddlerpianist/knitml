/**
 * 
 */
package test.support;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.custommonkey.xmlunit.XMLAssert;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;
import org.xml.sax.SAXException;

import com.knitml.core.model.Pattern;

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
			throws JiBXException, SAXException, IOException {
		IMarshallingContext mctx = getBindingFactory(object.getClass())
				.createMarshallingContext();
		StringWriter writer = new StringWriter();
		mctx.setOutput(writer);
		mctx.marshalDocument(object);
		XMLAssert.assertXMLEqual(xml, writer.toString());
	}
}
