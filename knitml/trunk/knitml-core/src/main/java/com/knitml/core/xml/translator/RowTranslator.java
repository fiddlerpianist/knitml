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

import com.knitml.core.common.DataConversion;
import com.knitml.core.common.EnumUtils;
import com.knitml.core.common.KnittingShape;
import com.knitml.core.common.RowDefinitionScope;
import com.knitml.core.common.Side;
import com.knitml.core.model.directions.InlineOperation;
import com.knitml.core.model.directions.block.Row;
import com.knitml.core.model.directions.information.FollowupInformation;
import com.knitml.core.model.directions.information.Information;

public class RowTranslator implements IMarshaller, IUnmarshaller, IAliasable {

	private String uri;
	private int index;
	private String name;

	public RowTranslator(String uri, int index, String name) {
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
		if (!(obj instanceof Row)) {
			throw new JiBXException("Invalid object type for row");
		}
		if (!(ictx instanceof MarshallingContext)) {
			throw new JiBXException("Invalid object type for marshaller");
		}
		MarshallingContext ctx = (MarshallingContext) ictx;
		Row row = (Row) obj;
		ctx.startTagAttributes(this.index, this.name);

		if (row.getYarnIdRef() != null) {
			ctx.attribute(this.index, "yarn-ref", row.getYarnIdRef());
		}
		if (row.isInformSide()) {
			ctx.attribute(this.index, "inform-side", Boolean.TRUE.toString());
		}
		if (row.isShortRow()) {
			ctx.attribute(this.index, "short", Boolean.TRUE.toString());
		}
		if (row.isLongRow()) {
			ctx.attribute(this.index, "long", Boolean.TRUE.toString());
		}
		if (row.getSide() != null) {
			ctx
					.attribute(this.index, "side", EnumUtils.fromEnum(row
							.getSide()));
		}
		if (row.getAssignRowNumber() != null
				&& row.getAssignRowNumber().equals(Boolean.FALSE)) {
			ctx.attribute(this.index, "assign-row-number", Boolean.FALSE
					.toString());
		}
		if (row.isResetRowCount()) {
			ctx.attribute(this.index, "reset-row-count", Boolean.TRUE
					.toString());
		}
		if (row.getNumbers() != null && row.getNumbers().length > 0) {
			ctx.attribute(this.index, "number", DataConversion
					.serializeIntArray(row.getNumbers()));
		}
		if (row.getSubsequent() != null) {
			ctx
					.attribute(this.index, "subsequent", EnumUtils.fromEnum(row
							.getSubsequent()));
		}
		if (row.getType() != null) {
			ctx
					.attribute(this.index, "type", EnumUtils.fromEnum(row
							.getType()));
		}

		ctx.closeStartContent();
		if (row.getInformation() != null) {
			((IMarshallable) row.getInformation()).marshal(ctx);
		}
		List<InlineOperation> operations = row.getOperations();
		for (InlineOperation operation : operations) {
			if (operation instanceof IMarshallable) {
				((IMarshallable) operation).marshal(ctx);
			} else {
				throw new JiBXException("Element is not marshallable");
			}
		}
		if (row.getFollowupInformation() != null) {
			((IMarshallable) row.getFollowupInformation()).marshal(ctx);
		}
		ctx.endTag(this.index, this.name);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jibx.runtime.IUnmarshaller#isPresent(org.jibx.runtime.
	 * IUnmarshallingContext)
	 */

	public boolean isPresent(IUnmarshallingContext ctx) throws JiBXException {
		return ctx.isAt(uri, name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jibx.runtime.IUnmarshaller#unmarshal(java.lang.Object,
	 * org.jibx.runtime.IUnmarshallingContext)
	 */

	public Object unmarshal(Object obj, IUnmarshallingContext ictx)
			throws JiBXException {
		// make sure we're at the appropriate start tag
		UnmarshallingContext ctx = (UnmarshallingContext) ictx;

		if (!ctx.isAt(this.uri, this.name)) {
			ctx.throwStartTagNameError(this.uri, this.name);
		}

		boolean assignRowNumber = true;
		int[] numbers = null;

		if (ctx.hasAttribute(null, "assign-row-number")) {
			assignRowNumber = ctx.attributeBoolean(null, "assign-row-number");
		}
		if (ctx.hasAttribute(null, "number")) {
			numbers = DataConversion.deserializeIntArray(ctx.attributeText(
					null, "number"));
		}
		Row row = new Row(assignRowNumber, numbers);

		row.setYarnIdRef(getAttribute(ctx, "yarn-ref"));
		row.setInformSide(false);
		if (ctx.hasAttribute(null, "inform-side")) {
			row.setInformSide(ctx.attributeBoolean(null, "inform-side"));
		}
		row.setShortRow(false);
		if (ctx.hasAttribute(null, "short")) {
			row.setShortRow(ctx.attributeBoolean(null, "short"));
		}
		if (ctx.hasAttribute(null, "long")) {
			row.setLongRow(ctx.attributeBoolean(null, "long"));
		}
		row.setSide(EnumUtils.toEnum(getAttribute(ctx, "side"), Side.class));
		row.setResetRowCount(false);
		if (ctx.hasAttribute(null, "reset-row-count")) {
			row.setResetRowCount(ctx.attributeBoolean(null, "reset-row-count"));
		}
		row.setSubsequent(EnumUtils.toEnum(getAttribute(ctx, "subsequent"),
				RowDefinitionScope.class));
		row.setType(EnumUtils.toEnum(getAttribute(ctx, "type"),
				KnittingShape.class));
		ctx.parsePastStartTag(this.uri, this.name);

		List<InlineOperation> operations = new ArrayList<InlineOperation>();
		while (!ctx.isEnd()) {
			Object element = ctx.unmarshalElement();
			if (element instanceof FollowupInformation) {
				row.setFollowupInformation((FollowupInformation) element);
			} else if (element instanceof Information) {
				row.setInformation((Information) element);
			} else {
				if (!(element instanceof InlineOperation)) {
					ctx
							.throwNameException(
									"Contains an invalid element (i.e. not an InlineOperation)",
									this.uri, this.name);
				}
				operations.add((InlineOperation) element);
			}
		}
		row.setOperations(operations);

		ctx.parsePastEndTag(this.uri, this.name);
		return row;
	}

	private String getAttribute(UnmarshallingContext ctx, String name)
			throws JiBXException {
		if (ctx.hasAttribute(null, name)) {
			return ctx.attributeText(null, name);
		}
		return null;
	}
}