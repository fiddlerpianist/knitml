package com.knitml.core.xml.translator;

import java.util.ArrayList;
import java.util.List;

import org.jibx.runtime.IAliasable;
import org.jibx.runtime.IMarshallable;
import org.jibx.runtime.IMarshaller;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshaller;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;
import org.jibx.runtime.impl.MarshallingContext;
import org.jibx.runtime.impl.UnmarshallingContext;

import com.knitml.core.model.Identifiable;
import com.knitml.core.model.directions.InlineOperation;
import com.knitml.core.model.directions.block.ForEachRowInInstruction;

public class ForEachRowInInstructionTranslator implements IMarshaller, IUnmarshaller,
		IAliasable {

	private String uri;
	private int index;
	private String name;

	public ForEachRowInInstructionTranslator(String uri, int index, String name) {
		this.uri = uri;
		this.index = index;
		this.name = name;
	}

	/**
	 * @see org.jibx.runtime.IMarshaller#isExtension(int)
	 */
	public boolean isExtension(String index) {
		return false;
	}

	/**
	 * @see org.jibx.runtime.IMarshaller#marshal(java.lang.Object,
	 *      org.jibx.runtime.IMarshallingContext)
	 */
	public void marshal(Object obj, IMarshallingContext ictx)
			throws JiBXException {

		// make sure the parameters are as expected
		if (!(obj instanceof ForEachRowInInstruction)) {
			throw new JiBXException("Invalid object type for ref");
		}
		if (!(ictx instanceof MarshallingContext)) {
			throw new JiBXException("Invalid object type for marshaller");
		}
		MarshallingContext ctx = (MarshallingContext) ictx;
		ForEachRowInInstruction eachRow = (ForEachRowInInstruction) obj;

		ctx.startTagAttributes(this.index, this.name);
		ctx.attribute(this.index, "ref", eachRow.getRef().getId());
		ctx.closeStartContent();
		List<InlineOperation> operations = eachRow.getOperations();
		for (InlineOperation operation : operations) {
			if (operation instanceof IMarshallable) {
				((IMarshallable) operation).marshal(ctx);
			} else {
				throw new JiBXException("Expression is not marshallable");
			}
		}
		ctx.endTag(this.index, this.name);
		
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jibx.runtime.IUnmarshaller#isPresent(org.jibx.runtime.IUnmarshallingContext)
	 */

	public boolean isPresent(IUnmarshallingContext ctx) throws JiBXException {
		return ctx.isAt(uri, name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jibx.runtime.IUnmarshaller#unmarshal(java.lang.Object,
	 *      org.jibx.runtime.IUnmarshallingContext)
	 */

	public Object unmarshal(Object obj, IUnmarshallingContext ictx)
			throws JiBXException {

		// make sure we're at the appropriate start tag
		UnmarshallingContext ctx = (UnmarshallingContext) ictx;
		if (!ctx.isAt(this.uri, this.name)) {
			ctx.throwStartTagNameError(this.uri, this.name);
		}
		Object identifiable = ctx.attributeExistingIDREF(null, "ref", 0);
		if (!(identifiable instanceof Identifiable)) {
			ctx
					.throwException("Object with id on " + name + " element must be an Identifiable type");
		}
		ctx.parsePastStartTag(this.uri, this.name);
		List<InlineOperation> operations = new ArrayList<InlineOperation>();
		while (!ctx.isEnd()) {
			Object element = ctx.unmarshalElement();
			if (!(element instanceof InlineOperation)) {
				ctx
						.throwNameException(
								"Contains an invalid element (i.e. not an InlineOperation)",
								this.uri, this.name);
			}
			operations.add((InlineOperation)element);
		}
		ForEachRowInInstruction eachRow = new ForEachRowInInstruction((Identifiable)identifiable, operations);
		
		ctx.parsePastEndTag(this.uri, this.name);
		return eachRow;
	}
}