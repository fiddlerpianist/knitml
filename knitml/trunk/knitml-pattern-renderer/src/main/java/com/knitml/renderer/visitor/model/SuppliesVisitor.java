package com.knitml.renderer.visitor.model;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.common.ValidationException;
import com.knitml.core.model.header.Needle;
import com.knitml.core.model.header.Supplies;
import com.knitml.core.model.header.Yarn;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.PatternRepository;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.visitor.impl.AbstractRenderingVisitor;

public class SuppliesVisitor extends AbstractRenderingVisitor {
	
	private static final String SYMBOL_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(SuppliesVisitor.class);

	public void visit(Object element, RenderingContext context)
			throws RenderingException {
		Supplies supplies = (Supplies) element;
		PatternRepository repository = context.getPatternRepository();
		List<Needle> needles = supplies.getNeedles();
		if (needles != null) {
			for (Needle needle : needles) {
				if (needle.getLabel() == null) {
					if (needle.getMessageKey() != null) {
						needle.setLabel(repository.getPatternMessage(needle.getMessageKey()));
					} else {
						needle.setLabel(needle.getId());
					}
				}
				repository.addNeedle(needle);
			}
		}
		List<Yarn> yarns = supplies.getYarns();
		if (yarns != null) {
			int i = 0;
			boolean symbolFound = false;
			boolean symbolCreated = false;
			for (Yarn yarn : yarns) {
				if (yarn.getSymbol() == null) {
					// default values for the symbol if none provided
					yarn.setSymbol(String.valueOf(SYMBOL_STRING.charAt(i)));
					symbolCreated = true;
				} else {
					symbolFound = true;
				}
				repository.addYarn(yarn);
				i++;
			}
			if (symbolCreated && symbolFound) {
				throw new ValidationException("Yarns must either all specify their symbols or leave all of their symbols blank", yarns);
			}
		}
		context.getRenderer().renderSupplies(supplies);
	}

}
