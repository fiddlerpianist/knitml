package com.knitml.validation.visitor.instruction.model;

import javax.measure.Measurable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.header.Gauge;
import com.knitml.core.units.RowGauge;
import com.knitml.core.units.StitchGauge;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.visitor.instruction.impl.AbstractPatternVisitor;

public class GaugeVisitor extends AbstractPatternVisitor {
	
	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory.getLogger(GaugeVisitor.class);

	public void visit(Object element, KnittingContext context) throws KnittingEngineException {
		Gauge gauge = (Gauge)element;
		Measurable<StitchGauge> stitchGauge = gauge.getStitchGauge();
		if (stitchGauge != null) {
			context.getPatternRepository().setStitchGauge(stitchGauge);
		}
		Measurable<RowGauge> rowGauge = gauge.getRowGauge();
		if (rowGauge != null) {
			context.getPatternRepository().setRowGauge(rowGauge);
		}
	}

}
