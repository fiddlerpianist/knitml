package test.support;

import java.io.StringReader;

import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;

public class JiBXUtils {

	protected static final String DEFAULT_BINDING_NAME = "pattern-binding";

	@SuppressWarnings("unchecked")
	public static <C> C parseXml(String xml, Class<C> rootClass) throws JiBXException {
		IBindingFactory factory = BindingDirectory.getFactory(DEFAULT_BINDING_NAME, rootClass);
		IUnmarshallingContext uctx = factory.createUnmarshallingContext();
		return (C) uctx.unmarshalDocument(new StringReader(xml));
	}

}
