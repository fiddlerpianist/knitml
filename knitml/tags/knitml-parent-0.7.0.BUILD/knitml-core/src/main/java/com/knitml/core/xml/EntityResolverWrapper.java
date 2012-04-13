package com.knitml.core.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class EntityResolverWrapper implements LSResourceResolver {
	private EntityResolver delegate;
	
	public EntityResolverWrapper(EntityResolver rs) {
		delegate = rs;
	}

	public LSInput resolveResource(String type, String namespaceURI,
			String publicId, String systemId, String baseURI) {
		LSInput input = null;
		try {
			InputSource is = delegate.resolveEntity(publicId, systemId);
			if (is != null) {
				input = new LSInputSourceWrapper(is);
			}
		} catch (SAXException ex) {
		} catch (IOException ex) {
		}
		return input;
	}
	
	class LSInputSourceWrapper implements LSInput {
		
		private InputStream byteStream;
		private Reader characterStream;
		private String encoding;
		private String publicId;
		private String systemId;
		private String baseURI;
		private String stringData;
		private boolean certifiedText;
		
		public LSInputSourceWrapper(InputSource is) {
			byteStream = is.getByteStream();
			characterStream = is.getCharacterStream();
			encoding = is.getEncoding();
			publicId = is.getPublicId();
			systemId = is.getSystemId();
		}

		public String getStringData() {
			return stringData;
		}

		public void setStringData(String stringData) {
			this.stringData = stringData;
		}

		public InputStream getByteStream() {
			return byteStream;
		}

		public void setByteStream(InputStream byteStream) {
			this.byteStream = byteStream;
		}

		public Reader getCharacterStream() {
			return characterStream;
		}

		public void setCharacterStream(Reader characterStream) {
			this.characterStream = characterStream;
		}

		public String getEncoding() {
			return encoding;
		}

		public void setEncoding(String encoding) {
			this.encoding = encoding;
		}

		public String getPublicId() {
			return publicId;
		}

		public void setPublicId(String publicId) {
			this.publicId = publicId;
		}

		public String getSystemId() {
			return systemId;
		}

		public void setSystemId(String systemId) {
			this.systemId = systemId;
		}

		public String getBaseURI() {
			return baseURI;
		}

		public void setBaseURI(String baseURI) {
			this.baseURI = baseURI;
		}

		public boolean getCertifiedText() {
			return certifiedText;
		}

		public void setCertifiedText(boolean certifiedText) {
			this.certifiedText = certifiedText;
		}
	}

}
