package com.knitml.core.xml.translator;

import java.text.ParseException;

import javax.measure.Measure;
import javax.measure.unit.Unit;
import javax.measure.unit.UnitFormat;

import org.jibx.runtime.IAliasable;
import org.jibx.runtime.IMarshaller;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshaller;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;
import org.jibx.runtime.impl.MarshallingContext;
import org.jibx.runtime.impl.UnmarshallingContext;

import com.knitml.core.units.KnittingMeasure;
import com.knitml.core.units.Units;

public class MeasurableTranslator implements IMarshaller, IUnmarshaller,
		IAliasable {

	private String uri;
	private int index;
	private String name;
	
	static {
		// statically load KnittingMeasure class
		@SuppressWarnings("unused")
		Unit<?> unit = Units.WRAPS_PER_INCH;
	}

	public MeasurableTranslator() {
		this.uri = null;
		this.index = 0;
		this.name = "measure"; //$NON-NLS-1$
	}

	public MeasurableTranslator(String uri, int index, String name) {
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
		if (!(obj instanceof Measure)) {
			throw new JiBXException("Invalid object type for marshaller"); //$NON-NLS-1$
		}
		if (!(ictx instanceof MarshallingContext)) {
			throw new JiBXException("Invalid object type for marshaller"); //$NON-NLS-1$
		}
		MarshallingContext ctx = (MarshallingContext) ictx;
		Measure<?,?> measure = (Measure<?,?>)obj;
		
		ctx.startTagAttributes(this.index, this.name);
		ctx.attribute(this.index, "unit", measure.getUnit().toString()); //$NON-NLS-1$
		ctx.closeStartContent();
		ctx.content(String.valueOf(measure.getValue()));
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

		String unitString = ctx.attributeText(null, "unit"); //$NON-NLS-1$
		Unit<?> unit = null;
		try {
			unit = (Unit<?>) (UnitFormat.getInstance()
					.parseObject(unitString));
		} catch (ParseException ex) {
			throw new JiBXException("Unable to parse unit element", ex); //$NON-NLS-1$
		}
		//unit.asType(Length.class);
		ctx.parsePastStartTag(this.uri, this.name);
		String value = ctx.parseContentText();
		ctx.parsePastEndTag(this.uri, this.name);
		return KnittingMeasure.valueOf(value, unit);
	}
}