package com.knitml.renderer.impl.html;

import static org.apache.commons.lang.StringEscapeUtils.escapeXml;

import java.io.Writer;

import javax.inject.Inject;

import com.google.inject.assistedinject.Assisted;
import com.knitml.core.model.pattern.Pattern;
import com.knitml.renderer.chart.stylesheet.StylesheetProvider;
import com.knitml.renderer.context.InstructionInfo;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.impl.basic.BasicTextRenderer;
import com.knitml.renderer.impl.helpers.HeaderHelper;
import com.knitml.renderer.impl.helpers.OperationSetHelper;
import com.knitml.renderer.plural.PluralRuleFactory;

public class HtmlRenderer extends BasicTextRenderer {

	private StylesheetProvider stylesheetProvider;
	private boolean closePreTagBeforePreCraftedInstructions = true;
	private HtmlWriterHelper writerHelper;

	@Inject
	public HtmlRenderer(PluralRuleFactory pluralRuleFactory,
			StylesheetProvider stylesheetProvider,
			@Assisted RenderingContext context, @Assisted Writer writer) {
		super(pluralRuleFactory, context, writer);
		this.stylesheetProvider = stylesheetProvider;
		if (writer != null) {
			writerHelper = new HtmlWriterHelper(writer);
			setWriterHelper(writerHelper);
		}
		setHeaderHelper(new HeaderHelper(writerHelper, getRenderingContext()
				.getOptions()));
		setOperationSetHelper(new OperationSetHelper(writerHelper,
				getMessageHelper()));
	}

	@Override
	public void beginInstructionGroup(String message) {
		if (message != null) {
			getWriterHelper().writeLine(
					"<span style=\"text-decoration: underline;\">" + message
							+ "</span>");
		}
	}

	@Override
	public void beginPattern(Pattern pattern) {
		writerHelper.setPreformatted(false);
		writerHelper
				.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
		writerHelper.writeSystemNewLine();
		writerHelper
				.write("<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"en\" xml:lang=\"en\"><head><title>KnitML Pattern</title>");
		writerHelper.writeSystemNewLine();
		String[] fontNames = getRenderingContext().getOptions().getFontNames();
		if (fontNames != null) {
			writerHelper.write("<style type=\"text/css\">");
			writerHelper.writeSystemNewLine();
			writerHelper.write("body { font-family: ");
			// escape the XML to eliminate the chance of an attack
			for (int i = 0; i < fontNames.length; i++) {
				if (i + 1 < fontNames.length) {
					writerHelper.write("\"");
					writerHelper.write(escapeXml(fontNames[i]));
					writerHelper.write("\",");
				} else {
					// this would be a generic font family (such as serif,
					// sans-serif, etc.) and should not be in quotes
					writerHelper.write(escapeXml(fontNames[i]));
					writerHelper.write(";");
				}
			}
			writerHelper.write(" }");
			writerHelper.writeSystemNewLine();
			writerHelper.write("</style>");
			writerHelper.writeSystemNewLine();
		}
		// for (StylesheetProvider stylesheetProvider : stylesheetProviders) {
		writerHelper.write("<style type=\"");
		writerHelper.write(escapeXml(stylesheetProvider.getMimeType()));
		writerHelper.write("\">");
		writerHelper.writeSystemNewLine();
		// do a custom replace here since the body of a stylesheet is
		// treated like CDATA
		String stylesheet = stylesheetProvider.getStylesheet();
		stylesheet = stylesheet.replaceAll("<\\s*/?\\s*[A-Za-z]+\\s*>", "");
		writerHelper.write(stylesheet);
		writerHelper.writeSystemNewLine();
		writerHelper.write("</style>");
		writerHelper.writeSystemNewLine();
		// }
		writerHelper.write("</head><body><div>");
		// writerHelper.setPreformatted(true);
	}

	@Override
	public void endPattern() {
		writerHelper.setPreformatted(false);
		writerHelper.write("</div></body></html>");
		super.endPattern();
	}

	@Override
	public void beginInstructionDefinition(InstructionInfo instructionInfo) {
		boolean wasPreformatted = writerHelper.isPreformatted();
		if (wasPreformatted && closePreTagBeforePreCraftedInstructions) {
			writerHelper.setPreformatted(false);
		}
		super.beginInstructionDefinition(instructionInfo);
		if (wasPreformatted && closePreTagBeforePreCraftedInstructions) {
			writerHelper.setPreformatted(true);
		}
	}

	@Override
	public void beginInstruction(InstructionInfo instructionInfo) {
		boolean wasPreformatted = writerHelper.isPreformatted();
		if (wasPreformatted && closePreTagBeforePreCraftedInstructions) {
			writerHelper.setPreformatted(false);
		}
		super.beginInstruction(instructionInfo);
		if (wasPreformatted && closePreTagBeforePreCraftedInstructions) {
			writerHelper.setPreformatted(true);
		}
	}

}
