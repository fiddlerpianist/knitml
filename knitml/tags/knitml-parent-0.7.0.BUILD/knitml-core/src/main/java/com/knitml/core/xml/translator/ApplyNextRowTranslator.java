package com.knitml.core.xml.translator;

import java.text.MessageFormat;

import org.jibx.runtime.IAliasable;
import org.jibx.runtime.IMarshaller;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshaller;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;
import org.jibx.runtime.impl.MarshallingContext;
import org.jibx.runtime.impl.UnmarshallingContext;

import com.knitml.core.model.common.Identifiable;
import com.knitml.core.model.operations.inline.ApplyNextRow;

public class ApplyNextRowTranslator implements IMarshaller, IUnmarshaller,
		IAliasable {

	private String uri;
	private int index;
	private String name;

	public ApplyNextRowTranslator(String uri, int index, String name) {
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
		if (!(obj instanceof ApplyNextRow)) {
			throw new JiBXException(MessageFormat.format(Messages.getString("INVALID_OBJ_TYPE"), "instructionRef")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		if (!(ictx instanceof MarshallingContext)) {
			throw new JiBXException(Messages.getString("INVALID_OBJ_TYPE_FOR_MARSHALLER")); //$NON-NLS-1$
		}
		MarshallingContext ctx = (MarshallingContext) ictx;
		ApplyNextRow applyNextRow = (ApplyNextRow) obj;

		ctx.startTagAttributes(this.index, this.name);
		ctx.attribute(this.index, "instruction-ref", applyNextRow.getInstructionRef().getId()); //$NON-NLS-1$
		ctx.closeStartEmpty();
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
		Object identifiable = ctx.attributeExistingIDREF(null, "instruction-ref", 0); //$NON-NLS-1$
		if (!(identifiable instanceof Identifiable)) {
			ctx
					.throwException(MessageFormat
							.format(Messages.getString("IDENTIFIABLE_TYPE_REQUIRED"), //$NON-NLS-1$
									name));  
		}
		ApplyNextRow applyNextRow = new ApplyNextRow((Identifiable)identifiable);
		ctx.parsePastEndTag(this.uri, this.name);
		return applyNextRow;
	}
}