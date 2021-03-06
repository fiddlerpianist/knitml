package com.knitml.renderer.impl.basic;

import static com.knitml.core.common.EnumUtils.fromEnum;
import static org.apache.commons.lang.StringUtils.capitalize;

import java.io.Writer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.measure.Measure;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.Range;
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.DefaultResourceLoader;

import com.google.inject.assistedinject.Assisted;
import com.knitml.core.common.EnumUtils;
import com.knitml.core.common.KnittingShape;
import com.knitml.core.common.LoopToWork;
import com.knitml.core.common.RowDefinitionScope;
import com.knitml.core.common.SlipDirection;
import com.knitml.core.common.ValidationException;
import com.knitml.core.model.common.Needle;
import com.knitml.core.model.common.StitchesOnNeedle;
import com.knitml.core.model.common.Yarn;
import com.knitml.core.model.operations.block.CastOn;
import com.knitml.core.model.operations.block.DeclareFlatKnitting;
import com.knitml.core.model.operations.block.Information;
import com.knitml.core.model.operations.block.Instruction;
import com.knitml.core.model.operations.block.PickUpStitches;
import com.knitml.core.model.operations.block.RepeatInstruction;
import com.knitml.core.model.operations.block.Row;
import com.knitml.core.model.operations.information.Message;
import com.knitml.core.model.operations.information.NumberOfStitches;
import com.knitml.core.model.operations.inline.ApplyNextRow;
import com.knitml.core.model.operations.inline.BindOff;
import com.knitml.core.model.operations.inline.BindOffAll;
import com.knitml.core.model.operations.inline.CrossStitches;
import com.knitml.core.model.operations.inline.Decrease;
import com.knitml.core.model.operations.inline.FromStitchHolder;
import com.knitml.core.model.operations.inline.Increase;
import com.knitml.core.model.operations.inline.IncreaseIntoNextStitch;
import com.knitml.core.model.operations.inline.InlineCastOn;
import com.knitml.core.model.operations.inline.InlineInstruction;
import com.knitml.core.model.operations.inline.InlineInstructionRef;
import com.knitml.core.model.operations.inline.InlinePickUpStitches;
import com.knitml.core.model.operations.inline.Knit;
import com.knitml.core.model.operations.inline.MultipleDecrease;
import com.knitml.core.model.operations.inline.NoStitch;
import com.knitml.core.model.operations.inline.OperationGroup;
import com.knitml.core.model.operations.inline.PassPreviousStitchOver;
import com.knitml.core.model.operations.inline.Purl;
import com.knitml.core.model.operations.inline.Repeat;
import com.knitml.core.model.operations.inline.Slip;
import com.knitml.core.model.operations.inline.SlipToStitchHolder;
import com.knitml.core.model.operations.inline.WorkEven;
import com.knitml.core.model.pattern.GeneralInformation;
import com.knitml.core.model.pattern.Pattern;
import com.knitml.core.model.pattern.Section;
import com.knitml.core.model.pattern.Supplies;
import com.knitml.renderer.Renderer;
import com.knitml.renderer.context.ContextUtils;
import com.knitml.renderer.context.InstructionInfo;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.impl.helpers.FromStitchHolderOperationSet;
import com.knitml.renderer.impl.helpers.HeaderHelper;
import com.knitml.renderer.impl.helpers.MessageHelper;
import com.knitml.renderer.impl.helpers.OperationSet;
import com.knitml.renderer.impl.helpers.OperationSet.Type;
import com.knitml.renderer.impl.helpers.OperationSetHelper;
import com.knitml.renderer.impl.helpers.RepeatOperationSet;
import com.knitml.renderer.impl.helpers.SimpleInstruction;
import com.knitml.renderer.impl.helpers.WriterHelper;
import com.knitml.renderer.plural.PluralRuleFactory;

public class BasicTextRenderer implements Renderer {

	protected RenderingContext renderingContext;

	// Helpers which contribute to the rendering effort
	private WriterHelper writerHelper;
	private MessageHelper messageHelper;
	private HeaderHelper headerHelper;
	private OperationSetHelper operationSetHelper;

	private Map<String, OperationSet> inlineInstructionStore = new LinkedHashMap<String, OperationSet>();

	private int sectionCount = 0;

	private Set<Row> rowsRenderedForCurrentInstruction = new HashSet<Row>();
	private boolean withinInstruction = false;

	@Inject
	public BasicTextRenderer(PluralRuleFactory pluralRuleFactory,
			@Assisted RenderingContext context, @Assisted Writer writer) {
		if (context == null) {
			throw new IllegalArgumentException(
					"A rendering context must be provided");
		}
		this.renderingContext = context;
		if (writer == null) {
			writerHelper = new WriterHelper();
		} else {
			writerHelper = new WriterHelper(writer);
		}

		// this is the base message source built into the pattern renderer
		ReloadableResourceBundleMessageSource parent = new ReloadableResourceBundleMessageSource();
		parent.setBasename("com/knitml/renderer/impl/operations");
		// use the class loader that loaded this class
		parent.setResourceLoader(new DefaultResourceLoader(this.getClass()
				.getClassLoader()));

		HierarchicalMessageSource messageSource = context.getOptions()
				.getProgramMessageSource();
		if (messageSource != null) {
			messageSource.setParentMessageSource(parent);
		} else {
			messageSource = parent;
		}
		messageHelper = new MessageHelper(messageSource, pluralRuleFactory,
				context.getOptions().getLocale());
		// both headerHelper and operationSetHelper have dependencies on
		// writerHelper
		headerHelper = new HeaderHelper(writerHelper, context.getOptions());
		operationSetHelper = new OperationSetHelper(writerHelper, messageHelper);
	}

	// implemented operations

	// header events
	public void addYarn(Yarn yarn) {
	}

	public void beginPattern(Pattern pattern) {
	}

	public void endPattern() {
		getWriterHelper().closeWriter();
	}

	public void renderGeneralInformation(GeneralInformation generalInformation) {
		getHeaderHelper().renderGeneralInformation(generalInformation);
	}

	public void renderSupplies(Supplies supplies) {
		getHeaderHelper().renderYarns(supplies);
		getHeaderHelper().renderNeedles(supplies);
		getHeaderHelper().renderAccessories(supplies);
	}

	public void beginInstructionDefinitions() {
		getHeaderHelper().startStoringInstructionDefinitions();
	}

	public void endInstructionDefinitions() {
		getHeaderHelper().stopStoringInstructionDefinitions();
	}

	public void beginInlineInstructionDefinition(InlineInstruction instruction,
			String label) {
		// only add this instruction if it has a label
		OperationSet operationSet = new OperationSet(
				OperationSet.Type.INLINE_INSTRUCTION);
		if (label != null) {
			operationSet.setHead(capitalize(label
					+ getMessage("operation.group-end-punctuation") + " "));
		}
		getOperationSetHelper().addNewOperationSet(operationSet);
	}

	public void endInlineInstructionDefinition(InlineInstruction instruction) {
		OperationSet currentOperationSet = getOperationSetHelper()
				.getCurrentOperationSet();
		if (currentOperationSet.getHead() != null) {
			getOperationSetHelper().renderCurrentOperationSet();
			writeNewLine();
			writeNewLine();
		}
		inlineInstructionStore.put(instruction.getId(), currentOperationSet);
		getOperationSetHelper().removeCurrentOperationSet();
	}

	public void beginInstructionDefinition(InstructionInfo instructionInfo) {
		if (instructionInfo.getRenderedText() != null) {
			// someone else has already figured out how to render this, so just
			// write it to the current buffer in play
			getWriterHelper().write(instructionInfo.getRenderedText());
		} else {
			getWriterHelper().write(
					instructionInfo.getLabel()
							+ getMessage("operation.group-end-punctuation"));
		}
		this.withinInstruction = true;
	}

	public void endInstructionDefinition() {
		this.withinInstruction = false;
		this.rowsRenderedForCurrentInstruction.clear();
		writeNewLine();
	}

	// body events

	public void beginInlineInstruction(InlineInstruction instruction) {
		// only add this instruction if it has a label
		OperationSet operationSet = new OperationSet(
				OperationSet.Type.INLINE_INSTRUCTION);
		OperationSet currentOperationSet = getOperationSetHelper()
				.getCurrentOperationSet();
		if (currentOperationSet == null) {
			throw new RuntimeException(
					"Expected an operation set on the stack (at least of Row type) but found none");
		}
		currentOperationSet.addOperationSet(operationSet);
		// add the InstructionSet to the top of the stack. All
		// operations will peek at this object when writing operations
		getOperationSetHelper().addNewOperationSet(operationSet);
	}

	public void endInlineInstruction(InlineInstruction instruction) {
		OperationSet currentOperationSet = getOperationSetHelper()
				.getCurrentOperationSet();
		inlineInstructionStore.put(instruction.getId(), currentOperationSet);
		getOperationSetHelper().removeCurrentOperationSet();
	}

	public void endInstruction() {
		this.withinInstruction = false;
		this.rowsRenderedForCurrentInstruction.clear();
	}

	public void endInstructionGroup() {
		if (!getWriterHelper().isBeginningOfParagraph()) {
			writeNewLine();
		}
		writeNewLine();
	}

	public void renderJoinInRound() {
		writeSentence(getMessage("operation.join-in-round"));
		writeNewLine();
	}

	public void renderArrangeStitchesOnNeedles(
			List<StitchesOnNeedle> stitchesOnNeedles) {
		List<Needle> needles = new ArrayList<Needle>();
		for (StitchesOnNeedle stitchesOnNeedle : stitchesOnNeedles) {
			needles.add(stitchesOnNeedle.getNeedle());
		}

		if (!getWriterHelper().isBeginningOfParagraph()) {
			writeNewLine();
		}
		getWriterHelper().writeSentence(
				getMessage("operation.arrange-stitches-on-needles",
						buildNeedleList(needles)), false,
				getMessageHelper().shouldCapitalizeSentences());
		writeNewLine();
		getWriterHelper().incrementIndent();
		for (int i = 0; i < stitchesOnNeedles.size(); i++) {
			StitchesOnNeedle stitchesOnNeedle = stitchesOnNeedles.get(i);
			Integer stitchesOnThisNeedle = stitchesOnNeedle
					.getNumberOfStitches();
			if (stitchesOnThisNeedle == null || stitchesOnThisNeedle == 0) {
				// don't write out the stitch count if there are 0 stitches on
				// the needle
				continue;
			}
			StringBuffer sb = new StringBuffer();
			String needleLabel = ContextUtils.deriveLabel(
					stitchesOnNeedle.getNeedle(),
					this.renderingContext.getPatternRepository());
			sb.append(StringUtils.capitalize(needleLabel));
			sb.append(": ");
			sb.append(getNumberOfStitchesMessage(stitchesOnThisNeedle, true));
			writeLine(sb.toString());
		}
		getWriterHelper().decrementIndent();
	}

	private String getNumberOfStitchesMessage(int number,
			boolean useAbbreviation) {
		StringBuffer messageKey = new StringBuffer("operation.stitch");
		if (!useAbbreviation) {
			messageKey.append(".word");
		}
		return getMessageHelper().getPluralizedMessage(messageKey.toString(),
				number);
	}

	public void beginDirections() {
		getHeaderHelper().renderInstructionDefinitions();
		beginInstructionGroup(getMessage("operation.directions-label"));
	}

	public void beginInstructionGroup(String message) {
		if (message != null) {
			writeLine(message);
			for (int i = 0; i < message.length(); i++) {
				write("-");
			}
			writeNewLine();
		}
	}

	public void renderUseNeedles(List<Needle> needles) {
		if (needles == null || needles.size() == 0) {
			return;
		}
		String operation = getMessage("operation.use-needles",
				buildNeedleList(needles));
		writeSentence(operation);
	}

	public String buildNeedleList(List<Needle> needles) {
		List<String> needleTexts = new ArrayList<String>(needles.size());
		for (Needle needle : needles) {
			String needleText = ContextUtils.deriveLabel(needle,
					this.renderingContext.getPatternRepository());
			needleTexts.add(needleText);
		}
		return buildList(needleTexts);
	}

	public void renderInlineInstructionRef(InlineInstructionRef ref,
			String label) {
		// TODO check preferences here to see if we want a label or the full
		// inline instruction
		if (label != null) {
			getOperationSetHelper().writeOperation(label);
		} else {
			// render the inline instruction explicitly
			OperationSet operationSet = inlineInstructionStore.get(ref
					.getReferencedInstruction().getId());
			if (operationSet == null) {
				throw new RuntimeException(
						"An inlineInstructionRef was not present in the local store when it should have been");
			}
			OperationSet currentOperationSet = getOperationSetHelper()
					.getCurrentOperationSet();
			if (currentOperationSet == null) {
				throw new RuntimeException(
						"Expected an operation set on the stack (at least of Row type) but found none");
			}
			currentOperationSet.addOperationSet(operationSet);
		}
	}

	public void renderUsingNeedlesCastOn(List<Needle> needles, CastOn castOn) {
		doRenderCastOn(castOn, needles, true);
	}

	public void renderKnit(Knit knit) {
		doRenderKnit(knit, "knit");
	}

	public void renderPurl(Purl purl) {
		doRenderKnit(purl, "purl");
	}

	protected void doRenderKnit(Knit knit, String propertyKeyBase) {
		// get a "k"/"p" out of the way first
		if (knit.getNumberOfTimes() == null && knit.getYarnIdRef() == null
				&& (knit.getRowsBelow() == null || knit.getRowsBelow() == 0)
				&& knit.getLoopToWork() != LoopToWork.TRAILING) {
			SimpleInstruction operation = new SimpleInstruction(
					(knit instanceof Purl) ? SimpleInstruction.Type.PURL
							: SimpleInstruction.Type.KNIT);
			getOperationSetHelper().writeOperation(operation);
			return;
		}

		List<String> keys = new ArrayList<String>();
		keys.add(propertyKeyBase);
		List<String> values = new ArrayList<String>();

		// get numberToWork, or 1 if null
		Integer numberToWork = knit.getNumberOfTimes() != null ? knit
				.getNumberOfTimes() : 1;
		values.add(numberToWork.toString());

		LoopToWork loopToWork = knit.getLoopToWork();
		if (loopToWork == LoopToWork.TRAILING) {
			keys.add("through-trailing-loop");
		}
		Yarn yarn = renderingContext.getPatternRepository().getYarn(
				knit.getYarnIdRef());
		if (yarn != null) {
			keys.add("with-yarn");
			values.add(yarn.getSymbol());
		}
		if (knit.getRowsBelow() != null && knit.getRowsBelow() == 1) {
			keys.add("row-below");
		} else if (knit.getRowsBelow() != null && knit.getRowsBelow() > 1) {
			keys.add("rows-below");
			values.add(knit.getRowsBelow().toString());
		}

		getOperationSetHelper().writeOperation(
				getMessageHelper().getPluralizedMessage(buildPropertyKey(keys),
						numberToWork, values.toArray()));
	}

	public void renderWorkEven(WorkEven workEven) {
		StringBuffer key = new StringBuffer("operation.work-even");
		List<Object> values = new ArrayList<Object>();
		// get numberOfStitches, or 1 if null
		Integer numberToWork = workEven.getNumberOfTimes() != null ? workEven
				.getNumberOfTimes() : 1;
		values.add(numberToWork);

		// get a "k"/"p" out of the way first
		if (workEven.getNumberOfTimes() == null
				&& workEven.getYarnIdRef() == null) {
			SimpleInstruction operation = new SimpleInstruction(
					SimpleInstruction.Type.WORK_EVEN);
			getOperationSetHelper().writeOperation(operation);
			return;
		}

		Yarn yarn = renderingContext.getPatternRepository().getYarn(
				workEven.getYarnIdRef());
		if (yarn != null) {
			key.append(".with-yarn");
			values.add(yarn.getSymbol());
		}

		getOperationSetHelper().writeOperation(
				getMessageHelper().getPluralizedMessage(key.toString(),
						numberToWork, values.toArray()));
	}

	public void renderSlip(Slip slip) {
		int numberToWork = defaultToOne(slip.getNumberOfTimes());
		StringBuffer key = new StringBuffer("operation.slip");
		// e.g. "operation.slip-knitwise" or "operation.slip-purlwise"
		if (slip.getType() != null) {
			key.append("-").append(fromEnum(slip.getType()));
		}
		// e.g. "operation.slip.yarn-in-back" or
		// "operation.slip.yarn-in-front"
		if (slip.getYarnPosition() != null) {
			key.append(".yarn-in-").append(fromEnum(slip.getYarnPosition()));
		}
		if (slip.getDirection() == SlipDirection.REVERSE) {
			key.append(".reverse");
		}
		getOperationSetHelper().writeOperation(
				getMessageHelper().getPluralizedMessage(key.toString(),
						numberToWork));
	}

	public void renderNoStitch(NoStitch noStitch) {
	}

	public void renderCrossStitches(CrossStitches crossStitches) {
		StringBuffer key = new StringBuffer("operation.cross-stitches.");
		key.append(EnumUtils.fromEnum(crossStitches.getType()));
		getOperationSetHelper().writeOperation(
				getMessageHelper().getPluralizedMessage(
						key.toString(),
						new int[] { crossStitches.getFirst(),
								crossStitches.getNext() }));
	}

	public void beginInstruction(InstructionInfo instructionInfo) {
		if (instructionInfo.getRenderedText() != null) {
			// someone else has already figured out how to render this, so just
			// write it to the current buffer in play
			writeNewLine();
			getWriterHelper().write(instructionInfo.getRenderedText());
		} else {
			beginInstructionGroup(instructionInfo.getLabel());
		}
		this.withinInstruction = true;
	}

	public void beginRepeat(Repeat repeat) {
		if (!getOperationSetHelper().isWithinOperationSet()) {
			throw new IllegalStateException(
					"Cannot issue a repeat without being inside a row or operation");
		}
		OperationSet repeatOperationSet = new RepeatOperationSet();
		// add the repeat InstructionSet to the beginning of the stack. All
		// operations will peek at this object when writing operations
		getOperationSetHelper().addNewOperationSet(repeatOperationSet);
	}

	public void endRepeat(Repeat.Until until, Integer value) {
		OperationSet repeatOperationSet = getOperationSetHelper()
				.getCurrentOperationSet();
		if (repeatOperationSet == null) {
			throw new IllegalStateException(
					"Must have a currentRepeat attribute set to write a 'repeat until' operation");
		}
		if (!(repeatOperationSet instanceof RepeatOperationSet)) {
			throw new IllegalStateException(
					"The working operation set must be a RepeatInstructionSet");
		}

		// first pop the RepeatOperationSet off the top of the writing stack
		getOperationSetHelper().removeCurrentOperationSet();
		if (repeatOperationSet.size() == 0) {
			// if there was nothing captured in the repeat operation set,
			// there's nothing to do
			return;
		} else {
			// add the repeat operation set to the end of its parent operation
			// set (most likely a row operation set)
			// only if there was anything captured in the repeat (some can be
			// empty)
			getOperationSetHelper().getCurrentOperationSet().addOperationSet(
					repeatOperationSet);
		}

		RepeatOperationSet currentRepeat = (RepeatOperationSet) repeatOperationSet;
		String messageKey;
		if (currentRepeat.size() == 1) {
			messageKey = "operation.until." + fromEnum(until);
		} else {
			messageKey = "operation.until." + fromEnum(until)
					+ ".with-notation";
		}
		String operation;
		if (value == null) {
			operation = getMessageHelper().getMessage(messageKey);
		} else {
			operation = getMessageHelper().getPluralizedMessage(messageKey,
					value);
		}
		currentRepeat.setUntilInstruction(operation);
		if (until == Repeat.Until.END) {
			currentRepeat.setToEnd(true);
		}
		// remove the current repeat from the stack
		// getOperationSetHelper().removeCurrentOperationSet();
	}

	public void beginRow() {
		OperationSet rowOperationSet = new OperationSet(Type.ROW);
		// push this operation onto the stack
		getOperationSetHelper().addNewOperationSet(rowOperationSet);
	}

	public void endRow(Row row, KnittingShape shape) {
		// this was originally in beginRow, but we need to process
		// AFTER the validator has had its way with the Row object,
		// since it may be modifying row numbers, etc.

		List<Integer> rowNumbers = new ArrayList<Integer>();
		if (row.getNumbers() != null) {
			for (Integer rowNumber : row.getNumbers()) {
				rowNumbers.add(rowNumber);
			}
		}
		String yarnId = row.getYarnIdRef();
		OperationSet rowOperationSet = getOperationSetHelper()
				.getCurrentOperationSet();
		if (rowOperationSet.getType() != OperationSet.Type.ROW) {
			throw new IllegalStateException(
					"Expected to pop a ROW type OperationSet but instead was type "
							+ rowOperationSet.getType());
		}

		// initialize the information object to pass to the renderer (make a
		// copy)
		Information rendererInformation = new Information();
		Information rowInformation = row.getInformation();
		if (row.getInformation() != null) {
			rendererInformation.setDetails(rowInformation.getDetails());
		}

		if (row.getSubsequent() == RowDefinitionScope.EVEN) {
			Message subsequentMessage = new Message();
			subsequentMessage
					.setMessageKey("operation.row.subsequent-even-rows");
			rendererInformation.getDetails().add(subsequentMessage);
		} else if (row.getSubsequent() == RowDefinitionScope.ODD) {
			Message subsequentMessage = new Message();
			subsequentMessage
					.setMessageKey("operation.row.subsequent-odd-rows");
			rendererInformation.getDetails().add(subsequentMessage);
		}

		if (row.getSide() != null) {
			Message sideMessage = new Message();
			sideMessage.setMessageKey("operation."
					+ EnumUtils.fromEnum(row.getSide())
					+ "-side-row-abbreviation");
			rendererInformation.getDetails().add(sideMessage);
		}

		// get the row label and set it as the head of the InstructionSet
		String rowHeader = getRowLabel(shape, rowNumbers, yarnId,
				rendererInformation);
		rowOperationSet.setHead(capitalize(rowHeader));

		// only render the row if:
		// 1) we are not within an instruction, OR
		// 2) we are within an instruction but we haven't encountered this row
		// before
		if (!withinInstruction
				|| !rowsRenderedForCurrentInstruction.contains(row)) {
			if (!getWriterHelper().isBeginningOfParagraph()) {
				writeNewLine();
			}
			getOperationSetHelper().renderCurrentOperationSet();
			writeNewLine();
			rowsRenderedForCurrentInstruction.add(row);
		}
		getOperationSetHelper().removeCurrentOperationSet();
	}

	public boolean beginOperationGroup(OperationGroup group) {
		// create an operation set "container" if one doesn't exist
		if (!getOperationSetHelper().isWithinOperationSet()) {
			OperationSet operationSet = new OperationSet(
					OperationSet.Type.OPERATION_GROUP);
			getOperationSetHelper().addNewOperationSet(operationSet);
		}
		return true; // means we want the children to be processed
	}

	public void endOperationGroup(OperationGroup group) {
		// if an OPERATION_GROUP operation set type is found, it was added
		// because there wasn't a top-level operation set to add its children
		// to, so it was created artificially.
		if (getOperationSetHelper().getCurrentOperationSet().getType() == OperationSet.Type.OPERATION_GROUP) {
			// OPERATION_GROUP operation set found, so render and remove it
			getOperationSetHelper().renderCurrentOperationSet();
			getOperationSetHelper().removeCurrentOperationSet();
		}
	}

	public void renderRepeatInstruction(RepeatInstruction repeatInstruction,
			String label) {
		renderRepeatInstructionInternal(repeatInstruction, label);
	}

	public void renderRepeatInstruction(RepeatInstruction repeatInstruction,
			InstructionInfo instructionInfo) {
		String instructionIdentifier = instructionInfo.getLabel();
		if (instructionIdentifier == null) {
			instructionIdentifier = buildRowRangeString(
					instructionInfo.getKnittingShape(),
					instructionInfo.getRowRange());
		}
		renderRepeatInstructionInternal(repeatInstruction,
				instructionIdentifier);
	}

	@SuppressWarnings("unchecked")
	private void renderRepeatInstructionInternal(
			RepeatInstruction repeatInstruction, String instructionIdentifier) {
		StringBuffer key = new StringBuffer("operation.repeat-instruction.");
		RepeatInstruction.Until until = repeatInstruction.getUntil();
		key.append(EnumUtils.fromEnum(until));
		List<Object> args = new ArrayList<Object>(2);
		args.add(instructionIdentifier);
		Double numberToPluralize = null;
		switch (until) {
		case ADDITIONAL_TIMES:
		case UNTIL_STITCHES_REMAIN:
			numberToPluralize = ((Number) repeatInstruction.getValue())
					.doubleValue();
			args.add(repeatInstruction.getValue());
			break;
		case UNTIL_MEASURES: {
			if (!(repeatInstruction.getValue() instanceof Measure)) {
				throw new ValidationException(
						"Expecting a unit of measure for repeat-instruction's value");
			}
			@SuppressWarnings("rawtypes")
			Measure originalMeasure = (Measure) repeatInstruction.getValue();
			@SuppressWarnings("rawtypes")
			Measure newMeasure;
			if (renderingContext.getOptions().getFabricMeasurementUnit() != null) {
				newMeasure = originalMeasure.to(renderingContext.getOptions()
						.getFabricMeasurementUnit());
			} else {
				newMeasure = originalMeasure;
			}
			Object numberObject = newMeasure.getValue();
			numberToPluralize = Double.valueOf(numberObject.toString());
			DecimalFormat format = new DecimalFormat();
			format.setMaximumFractionDigits(1);
			args.add(format.format(numberToPluralize) + " "
					+ newMeasure.getUnit());
			break;
		}
		case UNTIL_DESIRED_LENGTH: {
			break;
		}
		default: {
			throw new NotImplementedException();
		}
		}
		String message;
		if (numberToPluralize != null) {
			message = getMessageHelper().getPluralizedMessage(key.toString(),
					numberToPluralize, args.toArray());
		} else {
			message = getMessage(key.toString(), args.toArray());
		}
		writeSentence(message);
		writeNewLine();
	}

	private String buildRowRangeString(KnittingShape knittingShape,
			Range rowRange) {
		return getMessageHelper().buildRowRangeString(knittingShape, rowRange,
				null);
	}

	public void renderDecrease(Decrease decrease) {
		Integer times = decrease.getNumberOfTimes();
		String style = EnumUtils.fromEnum(decrease.getType());
		renderSequentialOperation("operation.decrease." + style, times);
	}

	public void renderMultipleDecrease(MultipleDecrease decrease) {
		List<String> keys = new ArrayList<String>();
		keys.add("multiple-decrease");
		List<Object> values = new ArrayList<Object>();
		values.add(decrease.getStitchesIntoOne());

		if (decrease.getLean() != null) {
			keys.add(decrease.getLean().getCanonicalName());
		}
		if (decrease.getStitchNatureProduced() != null) {
			keys.add(decrease.getStitchNatureProduced().getCanonicalName());
		}
		Yarn yarn = renderingContext.getPatternRepository().getYarn(
				decrease.getYarnIdRef());
		if (yarn != null) {
			keys.add("with-yarn");
			values.add(yarn.getSymbol());
		}
		getOperationSetHelper().writeOperation(
				getMessageHelper().getMessage(buildPropertyKey(keys),
						values.toArray()));
	}

	public void renderPassPreviousStitchOver(PassPreviousStitchOver ppso) {
		Integer times = ppso.getNumberOfTimes();
		renderSequentialOperation("operation.pass-previous-stitch-over", times);
	}

	public void renderIncrease(Increase increase) {
		Integer times = increase.getNumberOfTimes();
		StringBuffer messageKey = new StringBuffer("operation.increase");
		String style = EnumUtils.fromEnum(increase.getType());
		if (style != null) {
			messageKey.append(".").append(style);
		}
		renderSequentialOperation(messageKey.toString(), times);
	}

	public void renderTurn() {
		getOperationSetHelper().writeOperation(getMessage("operation.turn"));
	}

	public void renderUnworkedStitches(int number) {
		OperationSet currentInstructionSet = getOperationSetHelper()
				.getCurrentOperationSet();
		String message = getMessageHelper().getPluralizedMessage(
				"operation.unworked-stitches", number);
		currentInstructionSet.setTail(message);
	}

	public void renderNumberOfStitchesInRow(NumberOfStitches numberOfStitches) {
		// used for the end of a row
		String message = getMessageHelper().getPluralizedMessage(
				"operation.number-of-stitches-in-row",
				numberOfStitches.getNumber());
		if (getOperationSetHelper().isWithinOperationSet()) {
			OperationSet currentInstructionSet = getOperationSetHelper()
					.getCurrentOperationSet();
			currentInstructionSet.setTail(message);
		} else {
			getWriterHelper().writeSentence(message, true,
					getMessageHelper().shouldCapitalizeSentences());
		}
	}

	public void renderSlipToStitchHolder(SlipToStitchHolder spec) {
		String label = ContextUtils.deriveLabel(spec.getStitchHolder(),
				getRenderingContext().getPatternRepository());
		String message;
		if (StringUtils.isBlank(label)) {
			String messageKey = "operation.slip-to-stitch-holder";
			message = getMessageHelper().getPluralizedMessage(messageKey,
					spec.getNumberOfStitches());
		} else {
			String messageKey = "operation.slip-to-stitch-holder.with-label";
			message = getMessageHelper().getPluralizedMessage(messageKey,
					spec.getNumberOfStitches(), spec.getNumberOfStitches(),
					label);
		}
		getOperationSetHelper().writeOperation(message);
	}

	public void beginInformation() {
	}

	public void endInformation() {
	}

	public void renderMessage(String messageToRender) {
		if (getOperationSetHelper().isWithinOperationSet()) {
			getOperationSetHelper().getCurrentOperationSet().setTail(
					messageToRender);
		} else {
			getWriterHelper().writeSentence(messageToRender, false,
					getMessageHelper().shouldCapitalizeSentences());
		}
	}

	public void renderDesignateEndOfRow(KnittingShape currentKnittingShape) {
		OperationSet currentInstructionSet = getOperationSetHelper()
				.getCurrentOperationSet();
		currentInstructionSet.setTail(getMessage("operation.end-of-round"));
	}

	public void beginUsingNeedle(Needle needle) {
		OperationSet needleOperationSet = new OperationSet(Type.USING_NEEDLE);
		String needleText = ContextUtils.deriveLabel(needle,
				this.renderingContext.getPatternRepository());
		needleOperationSet.setHead(capitalize(needleText
				+ getMessage("operation.group-end-punctuation") + " "));
		OperationSet currentOperationSet = getOperationSetHelper()
				.getCurrentOperationSet();
		// add this operation to the current operation set
		currentOperationSet.addOperationSet(needleOperationSet);
		// now push this down the stack so that the needle operation set is
		// used for capturing operations
		getOperationSetHelper().addNewOperationSet(needleOperationSet);
	}

	public void endUsingNeedle() {
		OperationSet needleInstructionSet = getOperationSetHelper()
				.removeCurrentOperationSet();
		if (needleInstructionSet.getType() != OperationSet.Type.USING_NEEDLE) {
			throw new IllegalStateException(
					"Expecting a USING_NEEDLE type InstructionSet on the stack; instead was "
							+ needleInstructionSet.getType());
		}
	}

	public void beginFromStitchHolder(FromStitchHolder fromStitchHolder) {
		if (!getOperationSetHelper().isWithinOperationSet()) {
			throw new IllegalStateException(
					"Cannot issue a from-stitch-holder without being inside a row");
		}
		OperationSet fromStitchHolderOperationSet = new FromStitchHolderOperationSet();
		OperationSet currentOperationSet = getOperationSetHelper()
				.getCurrentOperationSet();
		// add this operation to the current operation set
		currentOperationSet.addOperationSet(fromStitchHolderOperationSet);
		// now push this down the stack so that the operation set is used for
		// capturing operations
		getOperationSetHelper()
				.addNewOperationSet(fromStitchHolderOperationSet);
	}

	public void endFromStitchHolder(FromStitchHolder fromStitchHolder) {
		String label = ContextUtils.deriveLabel(fromStitchHolder
				.getStitchHolder(), getRenderingContext()
				.getPatternRepository());
		// remove the current repeat from the stack (so that new operation sets
		// aren't added to it)
		OperationSet operationSet = getOperationSetHelper()
				.removeCurrentOperationSet();

		if (operationSet == null) {
			throw new IllegalStateException(
					"Must have a current operation set for endFromStitchHolder to work");
		}
		if (!(operationSet instanceof FromStitchHolderOperationSet)) {
			throw new IllegalStateException(
					"The working operation set must be a FromStitchHolder type of operation set");
		}
		((FromStitchHolderOperationSet) operationSet).setLabel(label);
	}

	public boolean beginIncreaseIntoNextStitch(
			IncreaseIntoNextStitch incIntoNextStitch) {
		OperationSet incIntoNextStitchOperationSet = new OperationSet(
				Type.INC_INTO_NEXT_ST);
		// if (!getOperationSetHelper().isWithinOperationSet()) {
		// throw new IllegalStateException(
		// "Cannot issue an increase-into-next-stitch without being inside a row");
		// }
		if (getOperationSetHelper().isWithinOperationSet()) {
			OperationSet currentOperationSet = getOperationSetHelper()
					.getCurrentOperationSet();
			// add this operation to the current operation set
			currentOperationSet.addOperationSet(incIntoNextStitchOperationSet);
		}
		// now push this down the stack so that the operation set is used for
		// capturing operations
		getOperationSetHelper().addNewOperationSet(
				incIntoNextStitchOperationSet);
		return true;
	}

	public void endIncreaseIntoNextStitch(
			IncreaseIntoNextStitch incIntoNextStitch) {
		// remove the current repeat from the stack (so that new operation sets
		// aren't added to it)
		OperationSet operationSet = getOperationSetHelper()
				.getCurrentOperationSet();
		if (operationSet == null
				|| operationSet.getType() != Type.INC_INTO_NEXT_ST) {
			throw new IllegalStateException(
					"Must have a current operation set of type INC_INTO_NEXT_ST to work correctly");
		}
		if (getOperationSetHelper().isCurrentOperationSetLonely()) {
			getOperationSetHelper().renderCurrentOperationSet();
		}
		getOperationSetHelper().removeCurrentOperationSet();
	}

	public void renderDeclareFlatKnitting(DeclareFlatKnitting spec) {
		getWriterHelper().writeSentence(getMessage("operation.knit-flat"),
				true, getMessageHelper().shouldCapitalizeSentences());
	}

	public void renderDeclareRoundKnitting() {
		getWriterHelper().writeSentence(getMessage("operation.knit-round"),
				true, getMessageHelper().shouldCapitalizeSentences());
	}

	public void renderPickUpStitches(InlinePickUpStitches pickUpStitches) {
		doRenderPickUpStitches(
				new PickUpStitches(pickUpStitches.getNumberOfTimes(),
						pickUpStitches.getYarnIdRef(), pickUpStitches.getType()),
				false);
	}

	public void renderPickUpStitches(PickUpStitches pickUpStitches) {
		doRenderPickUpStitches(pickUpStitches, true);
	}

	protected void doRenderPickUpStitches(PickUpStitches pickUpStitches,
			boolean writeSentenceByDefault) {
		StringBuffer key = new StringBuffer("operation.pick-up-stitches");
		List<Object> args = new ArrayList<Object>();
		int numberOfTimes = defaultToOne(pickUpStitches.getNumberOfTimes());
		args.add(numberOfTimes);
		if (pickUpStitches.getYarnIdRef() != null) {
			Yarn yarn = this.renderingContext.getPatternRepository().getYarn(
					pickUpStitches.getYarnIdRef());
			key.append(".with-yarn");
			args.add(yarn.getSymbol());
		}
		if (pickUpStitches.getType() != null) {
			key.append(".").append(fromEnum(pickUpStitches.getType()));
		}
		getOperationSetHelper().writeOperation(
				getMessageHelper().getPluralizedMessageNoVarargs(
						key.toString(), numberOfTimes, args.toArray()),
				writeSentenceByDefault);
	}

	public void renderCastOn(InlineCastOn pickUpStitches) {
		doRenderCastOn(new CastOn(pickUpStitches.getNumberOfStitches(),
				pickUpStitches.getYarnIdRef(), pickUpStitches.getStyle()),
				null, false);
	}

	public void renderCastOn(CastOn castOn) {
		doRenderCastOn(castOn, null, true);
	}

	protected void doRenderCastOn(CastOn castOn, List<Needle> needles,
			boolean writeSentenceByDefault) {
		int numberToCastOn = castOn.getNumberOfStitches() == null ? 1 : castOn
				.getNumberOfStitches();
		List<String> keys = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		// using-needles?
		if (needles != null) {
			keys.add("using-needles");
			values.add(buildNeedleList(needles));
		}
		// using-method?
		String methodKey = castOn.getStyle();
		// since method is currently free form text, there may not be an entry
		// for it, so just use the key
		String method = getMessageHelper().getMessageWithDefault(
				"method.cast-on." + methodKey, methodKey);
		String operation;
		if (method != null) {
			keys.add("using-method");
			values.add(method);
		}
		keys.add("cast-on");
		values.add(String.valueOf(numberToCastOn));
		// annotation?
		if (StringUtils.trimToNull(castOn.getAnnotation()) != null) {
			keys.add("annotation");
			values.add(castOn.getAnnotation().trim());
		}
		operation = getMessageHelper().getPluralizedMessage(
				buildPropertyKey(keys), numberToCastOn, values.toArray());
		getOperationSetHelper().writeOperation(operation,
				writeSentenceByDefault);
	}

	private String buildPropertyKey(List<String> keys) {
		StringBuilder builder = new StringBuilder("operation");
		for (String key : keys) {
			builder.append(".").append(key);
		}
		return builder.toString();
	}

	public void renderBindOff(BindOff bindOff) {
		StringBuffer key = new StringBuffer("operation.bind-off");
		List<Object> args = new ArrayList<Object>();
		args.add(bindOff.getNumberOfStitches());
		if (bindOff.getYarnIdRef() != null) {
			Yarn yarn = this.renderingContext.getPatternRepository().getYarn(
					bindOff.getYarnIdRef());
			key.append(".with-yarn");
			args.add(yarn.getSymbol());
		}
		if (bindOff.getType() != null) {
			key.append(".").append(fromEnum(bindOff.getType()));
		}

		String stringToWrite = getMessageHelper()
				.getPluralizedMessageNoVarargs(key.toString(),
						bindOff.getNumberOfStitches(), args.toArray());
		getOperationSetHelper().writeOperation(stringToWrite);
	}

	public void renderBindOffAll(BindOffAll bindOff) {
		StringBuffer key = new StringBuffer("operation.bind-off-all");
		List<Object> args = new ArrayList<Object>();
		if (bindOff.getYarnIdRef() != null) {
			Yarn yarn = this.renderingContext.getPatternRepository().getYarn(
					bindOff.getYarnIdRef());
			key.append(".with-yarn");
			args.add(yarn.getSymbol());
		}
		if (bindOff.getType() != null) {
			key.append(".").append(fromEnum(bindOff.getType()));
		}
		String stringToWrite = getMessageHelper().getMessageNoVarargs(
				key.toString(), args.toArray());
		getOperationSetHelper().writeOperation(stringToWrite);
	}

	public void renderPlaceMarker() {
		getOperationSetHelper().writeOperation(
				getMessage("operation.place-marker"));
	}

	public void renderRemoveMarker() {
		getOperationSetHelper().writeOperation(
				getMessage("operation.remove-marker"));
	}

	public void renderGraftStitchesTogether(List<Needle> needles) {
		writeSentence(getMessage("operation.graft-stitches-together",
				buildNeedleList(needles)));
	}

	public void renderApplyNextRow(ApplyNextRow applyNextRow, String label) {
		getOperationSetHelper().writeOperation(
				getMessage("operation.apply-next-row", label));
	}

	private int defaultToOne(Integer value) {
		return value == null ? 1 : value.intValue();
	}

	public void beginInstructionGroup() {
		sectionCount = 0;
	}

	public void beginSection(Section section) {
		if (sectionCount > 0) {
			getWriterHelper().writeNewLine();
		}
		sectionCount++;
	}

	public void endSection() {
		if (!getWriterHelper().isBeginningOfParagraph()) {
			writeNewLine();
		}
	}

	public void endDirections() {
	}

	// internal helper functions

	private void renderSequentialOperation(String messageKey, Integer times) {
		int timesToIterate = times == null ? 1 : times.intValue();
		String operation = getMessage(messageKey);
		for (int i = 0; i < timesToIterate; i++) {
			getOperationSetHelper().writeOperation(operation);
		}
	}

	// convenience functions

	protected String getRowLabel(KnittingShape shape, List<Integer> rows,
			String yarnId, Information information) {
		StringBuffer result = new StringBuffer(getMessageHelper().getRowLabel(
				shape, rows, yarnId, renderingContext));
		if (information != null && information.getDetails() != null
				&& information.getDetails().size() > 0) {
			List<String> resolvedList = new ArrayList<String>(information
					.getDetails().size());
			for (Object details : information.getDetails()) {
				if (details instanceof Message) {
					Message message = (Message) details;
					String text = getMessageHelper().getMessageWithDefault(
							message.getMessageKey(), message.getLabel());
					resolvedList.add(text);
				} else {
					resolvedList.add(details.toString());
				}
			}
			result.append(" [")
					.append(getMessageHelper().buildList(resolvedList, true,
							true)).append("]");
		}
		return result.append(getMessage("operation.group-end-punctuation"))
				.append(" ").toString();
	}

	private void write(String string) {
		getWriterHelper().write(string);
	}

	private void writeSentence(String text) {
		getWriterHelper().writeSentence(text, true,
				getMessageHelper().shouldCapitalizeSentences());
	}

	private void writeLine(String text) {
		getWriterHelper().writeLine(text);
	}

	private void writeNewLine() {
		getWriterHelper().writeNewLine();
	}

	private String getMessage(String code, Object... args) {
		return getMessageHelper().getMessage(code, args);
	}

	private String buildList(List<String> objects) {
		return getMessageHelper().buildList(objects, false, false);
	}

	// helper initialization and access / unimportant methods

	protected MessageHelper getMessageHelper() {
		return this.messageHelper;
	}

	protected HeaderHelper getHeaderHelper() {
		return headerHelper;
	}

	protected WriterHelper getWriterHelper() {
		return writerHelper;
	}

	protected OperationSetHelper getOperationSetHelper() {
		return operationSetHelper;
	}

	public Instruction evaluateInstruction(Instruction instruction,
			RepeatInstruction repeatInstruction) {
		// returning null tells the controller that we don't want to superimpose
		// a different Instruction than the one that we were given
		return null;
	}

	public Instruction evaluateInstructionDefinition(Instruction instruction) {
		// returning null tells the controller that we don't want to superimpose
		// a different Instruction than the one that we were given
		return null;
	}

	protected void setWriterHelper(WriterHelper writerHelper) {
		this.writerHelper = writerHelper;
	}

	protected void setHeaderHelper(HeaderHelper headerHelper) {
		this.headerHelper = headerHelper;
	}

	protected void setMessageHelper(MessageHelper messageHelper) {
		this.messageHelper = messageHelper;
	}

	protected void setOperationSetHelper(OperationSetHelper operationSetHelper) {
		this.operationSetHelper = operationSetHelper;
	}

	public RenderingContext getRenderingContext() {
		return renderingContext;
	}

}
