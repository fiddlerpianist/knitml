package com.knitml.renderer.impl.basic;

import static com.knitml.core.common.EnumUtils.fromEnum;

import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.measure.Measure;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.Range;
import org.springframework.context.MessageSource;

import com.knitml.core.common.EnumUtils;
import com.knitml.core.common.KnittingShape;
import com.knitml.core.common.LoopToWork;
import com.knitml.core.common.StitchesOnNeedle;
import com.knitml.core.common.ValidationException;
import com.knitml.core.model.directions.block.CastOn;
import com.knitml.core.model.directions.block.DeclareFlatKnitting;
import com.knitml.core.model.directions.block.Instruction;
import com.knitml.core.model.directions.block.RepeatInstruction;
import com.knitml.core.model.directions.block.Row;
import com.knitml.core.model.directions.block.Section;
import com.knitml.core.model.directions.information.Information;
import com.knitml.core.model.directions.information.Message;
import com.knitml.core.model.directions.information.NumberOfStitches;
import com.knitml.core.model.directions.inline.ApplyNextRow;
import com.knitml.core.model.directions.inline.BindOff;
import com.knitml.core.model.directions.inline.BindOffAll;
import com.knitml.core.model.directions.inline.CrossStitches;
import com.knitml.core.model.directions.inline.Decrease;
import com.knitml.core.model.directions.inline.Increase;
import com.knitml.core.model.directions.inline.InlineInstruction;
import com.knitml.core.model.directions.inline.InlineInstructionRef;
import com.knitml.core.model.directions.inline.InlinePickUpStitches;
import com.knitml.core.model.directions.inline.Knit;
import com.knitml.core.model.directions.inline.Purl;
import com.knitml.core.model.directions.inline.Repeat;
import com.knitml.core.model.directions.inline.Slip;
import com.knitml.core.model.header.GeneralInformation;
import com.knitml.core.model.header.Needle;
import com.knitml.core.model.header.Supplies;
import com.knitml.core.model.header.Yarn;
import com.knitml.renderer.context.InstructionInfo;
import com.knitml.renderer.context.Renderer;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.impl.basic.OperationSet.Type;
import com.knitml.renderer.plural.PluralRuleFactory;
import com.knitml.renderer.plural.impl.DefaultPluralRuleFactory;

public class BasicTextRenderer implements Renderer {

	// This is only used for resolving yarn IDs.
	// FIXME yarn IDs should be resolved by the model rather than through the
	// repository
	private RenderingContext context;

	// Helpers which help with rendering and breaking out the functionality
	private WriterHelper writerHelper = new WriterHelper();
	private HeaderHelper headerHelper = new HeaderHelper(writerHelper);
	private OperationSetHelper operationSetHelper = new OperationSetHelper(
			writerHelper, null);
	private MessageHelper messageHelper;
	private PluralRuleFactory pluralRuleFactory = new DefaultPluralRuleFactory();

	private int sectionCount = 0;

	public void initialize() {
		this.messageHelper = new MessageHelper(this.messageSource,
				this.pluralRuleFactory, this.locale);
		this.headerHelper = new HeaderHelper(this.writerHelper);
		this.operationSetHelper = new OperationSetHelper(this.writerHelper,
				this.messageHelper);
	}

	// implemented operations

	// header events
	public void addYarn(Yarn yarn) {
	}

	public void renderGeneralInformation(GeneralInformation generalInformation) {
		getHeaderHelper().renderGeneralInformation(generalInformation);
	}

	public void renderSupplies(Supplies supplies) {
		getHeaderHelper().renderYarns(supplies);
		getHeaderHelper().renderNeedles(supplies);
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
		OperationSet operationSet;
		if (label != null) {
			operationSet = new OperationSet(
					OperationSet.Type.INLINE_INSTRUCTION);
			operationSet.setHead(label
					+ getMessage("operation.group-end-punctuation") + " ");
		} else {
			operationSet = new EmptyOperationSet();
		}
		getOperationSetHelper().addNewOperationSet(operationSet);
	}

	public void endInlineInstructionDefinition() {
		if (!(getOperationSetHelper().isCurrentOperationSetEmpty())) {
			getOperationSetHelper().renderCurrentOperationSet();
			writeNewLine();
			writeNewLine();
		}
		getOperationSetHelper().removeCurrentOperationSet();
	}

	public void beginInstructionDefinition(Instruction instruction, String label) {
		getWriterHelper().write(
				label + getMessage("operation.group-end-punctuation"));
	}

	public void endInstructionDefinition() {
		writeNewLine();
	}

	// body events
	public void endInstruction() {
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
						buildList(needles)), false);
		writeNewLine();
		getWriterHelper().incrementIndent();
		for (int i = 0; i < stitchesOnNeedles.size(); i++) {
			StitchesOnNeedle stitchesOnNeedle = stitchesOnNeedles.get(i);
			StringBuffer sb = new StringBuffer();
			sb.append(StringUtils.capitalize(stitchesOnNeedle.getNeedle()
					.toString()));
			sb.append(": ");
			sb.append(getNumberOfStitchesMessage(stitchesOnNeedle
					.getNumberOfStitches(), true));
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
				buildList(needles));
		writeSentence(operation);
	}

	public void renderCastOn(CastOn castOn) {
		int numberToCastOn = castOn.getNumberOfStitches() == null ? 1 : castOn
				.getNumberOfStitches();
		String methodKey = castOn.getStyle();
		// since method is currently free form text, there may not be an entry
		// for it, so just use the key
		String method = getMessageHelper().getMessageWithDefault(
				"method.cast-on." + methodKey, methodKey);
		String operation;
		if (method == null) {
			operation = getMessageHelper().getPluralizedMessage(
					"operation.cast-on", numberToCastOn);
		} else {
			operation = getMessageHelper().getPluralizedMessage(
					"operation.using-method.cast-on", numberToCastOn,
					new Object[] { method, numberToCastOn });
		}
		writeSentence(operation);
	}

	public boolean renderInlineInstructionRef(InlineInstructionRef ref,
			String label) {
		getOperationSetHelper().writeOperation(label);
		return true;
	}

	public void renderUsingNeedlesCastOn(List<Needle> needles, CastOn castOn) {
		if (needles == null || needles.size() == 0) {
			renderCastOn(castOn);
		}
		int numberToCastOn = castOn.getNumberOfStitches() == null ? 1 : castOn
				.getNumberOfStitches();
		String operation;
		String method = getMessage("method.cast-on." + castOn.getStyle());
		if (method == null) {
			operation = getMessageHelper().getPluralizedMessage(
					"operation.using-needles.cast-on", numberToCastOn,
					new Object[] { buildList(needles), numberToCastOn });
		} else {
			operation = getMessageHelper()
					.getPluralizedMessage(
							"operation.using-needles.using-method.cast-on",
							numberToCastOn,
							new Object[] { buildList(needles), method,
									numberToCastOn });
		}
		writeSentence(operation);
	}

	public void renderKnit(Knit knit) {
		renderKnit(knit, "operation.knit");
	}

	public void renderPurl(Purl purl) {
		renderKnit(purl, "operation.purl");
	}

	protected void renderKnit(Knit knit, String baseInstructionKey) {
		StringBuffer key = new StringBuffer(baseInstructionKey);
		List<Object> values = new ArrayList<Object>();
		// get numberOfStitches, or 1 if null
		Integer numberToWork = knit.getNumberOfTimes() != null ? knit
				.getNumberOfTimes() : 1;
		values.add(numberToWork);

		// get a "k"/"p" out of the way first
		if (knit.getNumberOfTimes() == null && knit.getYarnIdRef() == null
				&& knit.getLoopToWork() != LoopToWork.TRAILING) {
			SimpleInstruction operation = new SimpleInstruction(
					(knit instanceof Purl) ? SimpleInstruction.Type.PURL
							: SimpleInstruction.Type.KNIT);
			getOperationSetHelper().writeOperation(operation);
			return;
		}

		Yarn yarn = context.getPatternRepository().getYarn(knit.getYarnIdRef());
		if (yarn != null) {
			key.append(".with-yarn");
			values.add(yarn.getSymbol());
		}

		LoopToWork loopToWork = knit.getLoopToWork();
		if (loopToWork == LoopToWork.TRAILING) {
			key.append(".through-trailing-loop");
		}
		getOperationSetHelper().writeOperation(
				getMessageHelper().getPluralizedMessage(key.toString(),
						numberToWork, values.toArray()));
	}

	public void renderSlip(Slip slip) {
		int numberToWork = defaultToOne(slip.getNumberOfTimes());
		StringBuffer key = new StringBuffer("operation.slip");
		// i.e. "operation.slip-knitwise" or "operation.slip-purlwise"
		if (slip.getType() != null) {
			key.append("-").append(fromEnum(slip.getType()));
		}
		// i.e. "operation.slip.yarn-in-back" or
		// "operation.slip.yarn-in-front"
		if (slip.getYarnPosition() != null) {
			key.append(".yarn-in-").append(fromEnum(slip.getYarnPosition()));
		}
		getOperationSetHelper().writeOperation(
				getMessageHelper().getPluralizedMessage(key.toString(),
						numberToWork));
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
	
	public Instruction evaluateInstruction(Instruction instruction) {
		return instruction;
	}

	public void beginInstruction(Instruction instruction, String message) {
		beginInstructionGroup(message);
	}

	public void beginRepeat(Repeat repeat) {
		OperationSet currentOperationSet = getOperationSetHelper()
				.getCurrentOperationSet();
		if (currentOperationSet == null) {
			throw new IllegalStateException(
					"Cannot issue a repeat without being inside a row or operation");
		}
		OperationSet repeatOperationSet = new RepeatOperationSet();
		currentOperationSet.addOperationSet(repeatOperationSet);
		// add the repeat InstructionSet to the beginning of the stack. All
		// operations will peek at this object when writing operations
		getOperationSetHelper().addNewOperationSet(repeatOperationSet);
	}

	public void endRepeat(Repeat.Until until, Integer value) {
		OperationSet currentOperationSet = getOperationSetHelper()
				.getCurrentOperationSet();
		if (currentOperationSet == null) {
			throw new IllegalStateException(
					"Must have a currentRepeat attribute set to write a 'repeat until' operation");
		}
		if (!(currentOperationSet instanceof RepeatOperationSet)) {
			throw new IllegalStateException(
					"The working operation set must be a RepeatInstructionSet");
		}
		RepeatOperationSet currentRepeat = (RepeatOperationSet) currentOperationSet;
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
		getOperationSetHelper().removeCurrentOperationSet();
	}

	public void beginRow(Row row, KnittingShape shape) {
		List<Integer> rows = new ArrayList<Integer>();
		if (row.getNumbers() != null) {
			for (Integer rowNumber : row.getNumbers()) {
				rows.add(rowNumber);
			}
		}
		String yarnId = row.getYarnIdRef();
		if (!getWriterHelper().isBeginningOfParagraph()) {
			writeNewLine();
		}
		OperationSet rowOperationSet = new OperationSet(Type.ROW);
		// get the prefix for the row (i.e. the row label) and set it as the
		// head of the InstructionSet
		Information information = row.getInformation();
		if (row.getSide() != null) {
			information = new Information();
			if (row.getInformation() != null) {
				information.setDetails(row.getInformation().getDetails());
			}
			Message sideMessage = new Message();
			sideMessage.setMessageKey("operation." + EnumUtils.fromEnum(row.getSide()) + "-side-row-abbreviation");
			information.getDetails().add(sideMessage);
		}
		
		String rowHeader = getRowLabel(shape, rows, yarnId, information);
		rowOperationSet.setHead(rowHeader);
		// push this operation onto the stack
		getOperationSetHelper().addNewOperationSet(rowOperationSet);
	}

	public void endRow() {

		OperationSet rowOperationSet = getOperationSetHelper()
				.getCurrentOperationSet();
		if (rowOperationSet.getType() != OperationSet.Type.ROW) {
			throw new IllegalStateException(
					"Expected to pop a ROW type OperationSet but instead was type "
							+ rowOperationSet.getType());
		}
		getOperationSetHelper().renderCurrentOperationSet();
		getOperationSetHelper().removeCurrentOperationSet();
		writeNewLine();
	}

	public void renderRepeatInstruction(RepeatInstruction repeatInstruction,
			String label) {
		renderRepeatInstructionInternal(repeatInstruction, label);
	}

	public void renderRepeatInstruction(RepeatInstruction repeatInstruction,
			InstructionInfo instructionInfo) {
		String instructionIdentifier = instructionInfo.getLabel();
		if (instructionIdentifier == null) {
			instructionIdentifier = buildRowString(instructionInfo
					.getKnittingShape(), instructionInfo.getRowRange());
		}
		renderRepeatInstructionInternal(repeatInstruction,
				instructionIdentifier);
	}

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
			Object numberObject = ((Measure) repeatInstruction.getValue())
					.getValue();
			numberToPluralize = Double.valueOf(numberObject.toString());
			args.add(repeatInstruction.getValue());
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

	private String buildRowString(KnittingShape knittingShape, Range rowRange) {
		return getMessageHelper().buildRowString(knittingShape, rowRange);
	}

	public void renderDecrease(Decrease decrease) {
		Integer times = decrease.getNumberOfTimes();
		String style = EnumUtils.fromEnum(decrease.getType());
		renderSequentialOperation("operation.decrease." + style, times);
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
				"operation.number-of-stitches-in-row", numberOfStitches.getNumber());
		if (getOperationSetHelper().isWithinOperationSet()) {
			OperationSet currentInstructionSet = getOperationSetHelper()
					.getCurrentOperationSet();
			currentInstructionSet.setTail(message);
		} else {
			getWriterHelper().writeSentence(message, true);
		}
	}

	public void beginInformation() {
	}

	public void endInformation() {
	}

	public void renderMessage(String messageToRender) {
		getWriterHelper().writeSentence(messageToRender, false);
	}

	public void renderDesignateEndOfRow(KnittingShape currentKnittingShape) {
		OperationSet currentInstructionSet = getOperationSetHelper()
				.getCurrentOperationSet();
		currentInstructionSet.setTail(getMessage("operation.end-of-round"));
	}

	public void beginUsingNeedle(Needle needle) {
		OperationSet needleOperationSet = new OperationSet(Type.USING_NEEDLE);
		needleOperationSet.setHead(needle
				+ getMessage("operation.group-end-punctuation") + " ");
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

	public void renderDeclareFlatKnitting(DeclareFlatKnitting spec) {
		getWriterHelper()
				.writeSentence(getMessage("operation.knit-flat"), true);
	}

	public void renderDeclareRoundKnitting() {
		getWriterHelper().writeSentence(getMessage("operation.knit-round"),
				true);
	}

	public void renderPickUpStitches(InlinePickUpStitches pickUpStitches) {
		StringBuffer key = new StringBuffer("operation.pick-up-stitches");
		List<Object> args = new ArrayList<Object>();
		int numberOfTimes = defaultToOne(pickUpStitches.getNumberOfTimes());
		args.add(numberOfTimes);
		if (pickUpStitches.getYarnIdRef() != null) {
			Yarn yarn = this.context.getPatternRepository().getYarn(
					pickUpStitches.getYarnIdRef());
			key.append(".with-yarn");
			args.add(yarn.getSymbol());
		}
		if (pickUpStitches.getType() != null) {
			key.append(".").append(fromEnum(pickUpStitches.getType()));
		}
		getOperationSetHelper().writeOperation(
				getMessageHelper().getPluralizedMessageNoVarargs(
						key.toString(), numberOfTimes, args.toArray()));
	}

	public void renderBindOff(BindOff bindOff) {
		StringBuffer key = new StringBuffer("operation.bind-off");
		List<Object> args = new ArrayList<Object>();
		args.add(bindOff.getNumberOfStitches());
		if (bindOff.getYarnIdRef() != null) {
			Yarn yarn = this.context.getPatternRepository().getYarn(
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
			Yarn yarn = this.context.getPatternRepository().getYarn(
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
				buildList(needles)));
	}

	public void renderApplyNextRow(ApplyNextRow applyNextRow, String label) {
		getOperationSetHelper().writeOperation(
				getMessage("operation.apply-next-row", label));
	}

	private int defaultToOne(Integer value) {
		return value == null ? 1 : value.intValue();
	}

	public void setRenderingContext(RenderingContext context) {
		this.context = context;
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
		getWriterHelper().closeWriter();
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
				shape, rows, yarnId, context));
		if (information != null && information.getDetails() != null && information.getDetails().size() > 0) {
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
			result.append(" [").append(
					getMessageHelper().buildList(resolvedList, true)).append(
					"]");
		}
		return result.append(getMessage("operation.group-end-punctuation"))
				.append(" ").toString();
	}

	private void write(String string) {
		getWriterHelper().write(string);
	}

	private void writeSentence(String text) {
		getWriterHelper().writeSentence(text, true);
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

	private String buildList(List<?> objects) {
		return getMessageHelper().buildList(objects, false);
	}

	// helper initialization and access

	public void setWriter(Writer writer) {
		if (writer != null) {
			this.writerHelper = new WriterHelper(writer);
		}
		this.headerHelper = new HeaderHelper(this.writerHelper);
		this.operationSetHelper = new OperationSetHelper(this.writerHelper,
				this.messageHelper);
	}

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

	// two variables that can be set pre-initialization.
	// They are passed along to the MessageHelper.
	private MessageSource messageSource;
	private Locale locale = Locale.getDefault();

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public void setPluralRuleFactory(PluralRuleFactory pluralRuleFactory) {
		this.pluralRuleFactory = pluralRuleFactory;
	}

	public boolean requiresExplicitRepeats() {
		// TODO Auto-generated method stub
		return false;
	}

}
