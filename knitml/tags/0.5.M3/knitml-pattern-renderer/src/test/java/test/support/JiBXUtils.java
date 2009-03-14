package test.support;

import java.io.StringReader;

import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IUnmarshallingContext;

public class JiBXUtils {

	@SuppressWarnings("unchecked")
	public static <C> C parseXml(String xml, Class<C> rootClass) throws Exception {
		IBindingFactory factory = BindingDirectory.getFactory(rootClass);
		IUnmarshallingContext uctx = factory.createUnmarshallingContext();
		return (C) uctx.unmarshalDocument(new StringReader(xml));
	}
	

}
