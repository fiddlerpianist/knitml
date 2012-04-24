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
import com.knitml.core.common.KnittingShape;
import com.knitml.core.common.RowDefinitionScope;
import com.knitml.core.common.Side;
import com.knitml.core.model.operations.InlineOperation;
import com.knitml.core.model.operations.block.FollowupInformation;
import com.knitml.core.model.operations.block.Information;
import com.knitml.core.model.operations.block.Row;
import com.knitml.core.xml.DataConversion;

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
			throw new JiBXException("Invalid object type for row"); //$NON-NLS-1$
		}
		if (!(ictx instanceof MarshallingContext)) {
			throw new JiBXException("Invalid object type for marshaller"); //$NON-NLS-1$
		}
		MarshallingContext ctx = (MarshallingContext) ictx;
		Row row = (Row) obj;
		ctx.startTagAttributes(this.index, this.name);

		if (row.getYarnIdRef() != null) {
			ctx.attribute(this.index, "yarn-ref", row.getYarnIdRef()); //$NON-NLS-1$
		}
		if (row.isInformSide()) {
			ctx.attribute(this.index, "inform-side", Boolean.TRUE.toString()); //$NON-NLS-1$
		}
		if (row.isShortRow()) {
			ctx.attribute(this.index, "short", Boolean.TRUE.toString()); //$NON-NLS-1$
		}
		if (row.isLongRow()) {
			ctx.attribute(this.index, "long", Boolean.TRUE.toString()); //$NON-NLS-1$
		}
		if (row.getSide() != null) {
			ctx
					.attribute(this.index, "side", EnumUtils.fromEnum(row //$NON-NLS-1$
							.getSide()));
		}
		if (row.getAssignRowNumber() != null
				&& row.getAssignRowNumber().equals(Boolean.FALSE)) {
			ctx.attribute(this.index, "assign-row-number", Boolean.FALSE //$NON-NLS-1$
					.toString());
		}
		if (row.isResetRowCount()) {
			ctx.attribute(this.index, "reset-row-count", Boolean.TRUE //$NON-NLS-1$
					.toString());
		}
		if (row.getNumbers() != null && row.getNumbers().length > 0) {
			ctx.attribute(this.index, "number", DataConversion //$NON-NLS-1$
					.serializeIntArray(row.getNumbers()));
		}
		if (row.getSubsequent() != null) {
			ctx
					.attribute(this.index, "subsequent", EnumUtils.fromEnum(row //$NON-NLS-1$
							.getSubsequent()));
		}
		if (row.getType() != null) {
			ctx
					.attribute(this.index, "type", EnumUtils.fromEnum(row //$NON-NLS-1$
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
				throw new JiBXException("Element is not marshallable"); //$NON-NLS-1$
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

		if (ctx.hasAttribute(null, "assign-row-number")) { //$NON-NLS-1$
			assignRowNumber = ctx.attributeBoolean(null, "assign-row-number"); //$NON-NLS-1$
		}
		if (ctx.hasAttribute(null, "number")) { //$NON-NLS-1$
			numbers = DataConversion.deserializeIntArray(ctx.attributeText(
					null, "number")); //$NON-NLS-1$
		}
		Row row = new Row(assignRowNumber, numbers);

		row.setYarnIdRef(getAttribute(ctx, "yarn-ref")); //$NON-NLS-1$
		row.setInformSide(false);
		if (ctx.hasAttribute(null, "inform-side")) { //$NON-NLS-1$
			row.setInformSide(ctx.attributeBoolean(null, "inform-side")); //$NON-NLS-1$
		}
		row.setShortRow(false);
		if (ctx.hasAttribute(null, "short")) { //$NON-NLS-1$
			row.setShortRow(ctx.attributeBoolean(null, "short")); //$NON-NLS-1$
		}
		if (ctx.hasAttribute(null, "long")) { //$NON-NLS-1$
			row.setLongRow(ctx.attributeBoolean(null, "long")); //$NON-NLS-1$
		}
		row.setSide(EnumUtils.toEnum(getAttribute(ctx, "side"), Side.class)); //$NON-NLS-1$
		row.setResetRowCount(false);
		if (ctx.hasAttribute(null, "reset-row-count")) { //$NON-NLS-1$
			row.setResetRowCount(ctx.attributeBoolean(null, "reset-row-count")); //$NON-NLS-1$
		}
		row.setSubsequent(EnumUtils.toEnum(getAttribute(ctx, "subsequent"), //$NON-NLS-1$
				RowDefinitionScope.class));
		row.setType(EnumUtils.toEnum(getAttribute(ctx, "type"), //$NON-NLS-1$
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
									"Contains an invalid element (i.e. not an InlineOperation)", //$NON-NLS-1$
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