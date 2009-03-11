package com.knitml.renderer.impl.html;

import java.io.Writer;

import com.knitml.core.model.Pattern;
import com.knitml.renderer.context.InstructionInfo;
import com.knitml.renderer.impl.basic.BasicTextRenderer;
import com.knitml.renderer.impl.helpers.HeaderHelper;
import com.knitml.renderer.impl.helpers.OperationSetHelper;

public class HtmlRenderer extends BasicTextRenderer {

	private boolean closePreTagBeforePreCraftedInstructions = true;
	private HtmlWriterHelper writerHelper;

	@Override
	public void beginPattern(Pattern pattern) {
		writerHelper.setPreformatted(false);
		writerHelper.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
		writerHelper.writeSystemNewLine();
		writerHelper.write("<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"en\" xml:lang=\"en\"><head><title>KnitML Pattern</title></head><body>");
		writerHelper.setPreformatted(true);
	}

	@Override
	public void endPattern() {
		writerHelper.setPreformatted(false);
		writerHelper.write("</body></html>");
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
	
	@Override
	public void setWriter(Writer writer) {
		if (writer != null) {
			writerHelper = new HtmlWriterHelper(writer);
			setWriterHelper(writerHelper);
		}
		setHeaderHelper(new HeaderHelper(writerHelper, this.context.getOptions()));
		setOperationSetHelper(new OperationSetHelper(getWriterHelper(),
				getMessageHelper()));
	}

}
