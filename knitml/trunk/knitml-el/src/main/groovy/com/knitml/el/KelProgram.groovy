package com.knitml.el

import javax.xml.validation.*
import javax.xml.transform.*
import javax.xml.XMLConstants
import javax.xml.transform.stream.StreamSource
import groovy.xml.MarkupBuilder

import com.knitml.core.xml.PluggableSchemaResolver
import com.knitml.core.xml.EntityResolverWrapper

import org.xml.sax.EntityResolver
import org.w3c.dom.ls.LSResourceResolver

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import static com.knitml.core.xml.Schemas.CURRENT_PATTERN_SCHEMA

class KelProgram {

	private static final Logger log = LoggerFactory.getLogger(KelProgram)
	
	String convertToXml(options) {
		log.info("New KEL to XML conversion started")
		
		StringWriter convertedXmlWriter = new StringWriter()
		def builder = new MarkupBuilder(convertedXmlWriter)
		builder.useDoubleQuotes = true

		new KelToXml().createKnitML(options.reader, builder)
		def result = convertedXmlWriter.toString()

		if (options.checkSyntax) {
			log.info "Performing validation on newly produced XML document"
			validate result
		}
		if (options.writer != null) {
			try {
				options.writer.write result
			} finally {
				options.writer.close()
			}
		}
		
		log.info("KEL to XML conversion completed")
		return result
	}
	
	def validate(String xml) {

		EntityResolver entityResolver = new PluggableSchemaResolver(this.class.classLoader)
		def resourceResolver =  new EntityResolverWrapper(entityResolver)
		
		// create a SchemaFactory capable of understanding XML Schema
	    SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
		// set a strategy to validate included XML schemas
	    factory.resourceResolver = resourceResolver

	    // set primary XML to validate
	    def patternSchema = entityResolver.resolveEntity(null,CURRENT_PATTERN_SCHEMA)
	    Source patternSchemaSource = new StreamSource(patternSchema.byteStream)
	    Schema schema = factory.newSchema(patternSchemaSource)

	    // create a Validator instance to validate the document
	    Validator validator = schema.newValidator()
	    def source = new StreamSource(new StringReader(xml))

	    // validate the tree
        validator.validate source
	}
}