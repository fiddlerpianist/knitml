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

import com.knitml.core.common.EnumUtils;
import com.knitml.core.common.StitchesOnNeedle;
import com.knitml.core.model.Identifiable;
import com.knitml.core.model.directions.block.RepeatInstruction;
import com.knitml.core.model.directions.expression.Expression;
import com.knitml.core.model.header.Needle;

public class RepeatInstructionTranslator implements IMarshaller, IUnmarshaller,
		IAliasable {

	private String uri;
	private int index;
	private String name;

	public RepeatInstructionTranslator() {
		this.uri = null;
		this.index = 0;
		this.name = "repeat-instruction";
	}

	public RepeatInstructionTranslator(String uri, int index, String name) {
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
	@SuppressWarnings("unchecked")
	public void marshal(Object obj, IMarshallingContext ictx)
			throws JiBXException {

		// make sure the parameters are as expected
		if (!(obj instanceof RepeatInstruction)) {
			throw new JiBXException("Invalid object type for marshaller");
		}
		if (!(ictx instanceof MarshallingContext)) {
			throw new JiBXException("Invalid object type for marshaller");
		}

		// start by generating start tag for container
		MarshallingContext ctx = (MarshallingContext) ictx;
		RepeatInstruction repeatInstruction = (RepeatInstruction) obj;
		Identifiable ref = repeatInstruction.getRef();
		if (ref != null) {
			ctx.startTagAttributes(this.index, this.name);
			ctx.attribute(this.index, "ref", ref.getId());
			ctx.closeStartContent();
		}
		String untilTagName = EnumUtils.fromEnum(repeatInstruction.getUntil());
		switch (repeatInstruction.getUntil()) {
		case ADDITIONAL_TIMES:
		case UNTIL_STITCHES_REMAIN: {
			ctx.startTag(this.index, untilTagName);
			ctx.content(String.valueOf(repeatInstruction.getValue()));
			ctx.endTag(this.index, untilTagName);
			break;
		}
		case UNTIL_DESIRED_LENGTH: {
			ctx.startTag(this.index, untilTagName);
			ctx.closeStartEmpty();
			break;
		}
		case UNTIL_STITCHES_REMAIN_ON_NEEDLES: {
			ctx.startTag(this.index, untilTagName);
			ctx.closeStartContent();
			List<StitchesOnNeedle> needles = (List) repeatInstruction
					.getValue();
			for (StitchesOnNeedle needle : needles) {
				ctx.startTagAttributes(this.index, "needle");
				ctx.attribute(this.index, "ref", needle.getNeedle().getId());
				ctx.closeStartContent();
				ctx.content(needle.getNumberOfStitches());
				ctx.endTag(this.index, "needle");
			}
			ctx.endTag(this.index, untilTagName);
			break;
		}
		case UNTIL_EQUALS: {
			ctx.startTag(this.index, untilTagName);
			ctx.closeStartContent();
			List<Expression> expressions = (List) repeatInstruction.getValue();
			for (Expression expression : expressions) {
				if (expression instanceof IMarshallable) {
					((IMarshallable) expression).marshal(ctx);
				} else {
					throw new JiBXException("Expression is not marshallable");
				}
			}
			ctx.endTag(this.index, untilTagName);
			break;
		}
		case UNTIL_MEASURES: {
			if (repeatInstruction.getValue() instanceof IMarshallable) {
				((IMarshallable) repeatInstruction.getValue()).marshal(ctx);
			} else {
				throw new JiBXException("Mapped value is not marshallable");
			}
			break;
		}
		}
		// finish with end tag for container element
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

		RepeatInstruction repeatInstruction = new RepeatInstruction();
		Object instructionRef = ctx.attributeExistingIDREF(null, "ref", 0);
		if (!(instructionRef instanceof Identifiable)) {
			ctx
					.throwException("Object with id on repeat-instruction element not defined");
		}
		repeatInstruction.setRef((Identifiable) instructionRef);

		// process all entries present in document
		ctx.parsePastStartTag(uri, name);
		if (ctx.isAt(uri, "until-desired-length")) {
			repeatInstruction
					.setUntil(RepeatInstruction.Until.UNTIL_DESIRED_LENGTH);
			ctx.skipElement();
		} else if (ctx.isAt(uri, "until-stitches-remain")) {
			repeatInstruction
					.setUntil(RepeatInstruction.Until.UNTIL_STITCHES_REMAIN);
			repeatInstruction.setValue(ctx.parseElementInt(uri,
					"until-stitches-remain"));
		} else if (ctx.isAt(uri, "until-stitches-remain-on-needles")) {
			repeatInstruction
					.setUntil(RepeatInstruction.Until.UNTIL_STITCHES_REMAIN_ON_NEEDLES);
			List<StitchesOnNeedle> needles = new ArrayList<StitchesOnNeedle>();
			repeatInstruction.setValue(needles);
			ctx.parsePastStartTag(uri, "until-stitches-remain-on-needles");
			while (ctx.isAt(uri, "needle")) {
				Object needle = ctx.attributeExistingIDREF(null, "ref",
						this.index);
				if (!(needle instanceof Needle)) {
					ctx
							.throwException("Object with ref on until-stitches-remain-on-needles element not defined");
				}
				ctx.parsePastStartTag(uri, "needle");
				int numberOfStitches = ctx.parseContentInt(uri, "needle");
				StitchesOnNeedle stitchesOnNeedle = new StitchesOnNeedle(
						(Needle) needle, numberOfStitches);
				needles.add(stitchesOnNeedle);
				// ctx.parsePastEndTag(uri, "needle");
			}
			ctx.parsePastEndTag(uri, "until-stitches-remain-on-needles");
		} else if (ctx.isAt(uri, "until-measures")) {
			repeatInstruction.setUntil(RepeatInstruction.Until.UNTIL_MEASURES);
			repeatInstruction.setValue(ctx.unmarshalElement());
		} else if (ctx.isAt(uri, "additional-times")) {
			repeatInstruction
					.setUntil(RepeatInstruction.Until.ADDITIONAL_TIMES);
			repeatInstruction.setValue(ctx.parseElementInt(uri,
					"additional-times"));
		} else if (ctx.isAt(uri, "until-equals")) {
			repeatInstruction.setUntil(RepeatInstruction.Until.UNTIL_EQUALS);
			List<Expression> expressions = new ArrayList<Expression>();
			repeatInstruction.setValue(expressions);
			ctx.parsePastStartTag(uri, "until-equals");
			while (!ctx.isEnd()) {
				Object element = ctx.unmarshalElement();
				if (!(element instanceof Expression)) {
					ctx
							.throwNameException(
									"Contains an invalid element (i.e. not an expression)",
									this.uri, "until-equals");
				}
				expressions.add((Expression) element);
			}
			ctx.parsePastEndTag(uri, "until-equals");
		}
		ctx.parsePastEndTag(uri, name);
		return repeatInstruction;
	}
}