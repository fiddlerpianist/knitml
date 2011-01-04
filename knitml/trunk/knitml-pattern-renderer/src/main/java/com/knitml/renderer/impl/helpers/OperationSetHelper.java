package com.knitml.renderer.impl.helpers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.knitml.core.common.Stack;
import com.knitml.renderer.impl.helpers.OperationSet.Type;

public class OperationSetHelper {

	// a stack to hold the operation sets; eases in rendering groupings of
	// operations
	private Stack<OperationSet> operationSetStack = new Stack<OperationSet>();
	private WriterHelper writerHelper;
	private MessageHelper messageHelper;

	private MessageHelper getMessageHelper() {
		return messageHelper;
	}

	private WriterHelper getWriterHelper() {
		return writerHelper;
	}

	public OperationSetHelper(WriterHelper writerHelper,
			MessageHelper messageHelper) {
		this.writerHelper = writerHelper;
		this.messageHelper = messageHelper;
	}

	public void writeOperation(String operationString) {
		writeOperation(operationString, false);
	}

	public void writeOperation(String operationString,
			boolean writeSentenceIfNoOperationSet) {
		// if there is an object on the stack, write to that
		if (!operationSetStack.empty() && !StringUtils.isBlank(operationString)) {
			OperationSet currentInstructionSet = operationSetStack.peek();
			currentInstructionSet.addOperationString(operationString);
		} else {
			if (writeSentenceIfNoOperationSet) {
				getWriterHelper().writeSentence(operationString, true,
						getMessageHelper().shouldCapitalizeSentences());
			} else {
				getWriterHelper().write(operationString);
			}
		}
	}

	public void writeOperation(SimpleInstruction operation) {
		if (isWithinOperationSet()) {
			OperationSet currentOperationSet = operationSetStack.peek();
			currentOperationSet.addOperation(operation);
		} else {
			if (operation.getType() == SimpleInstruction.Type.PURL) {
				writeOperation(getMessageHelper().getPluralizedMessage(
						"operation.purl", 1));
			} else if (operation.getType() == SimpleInstruction.Type.KNIT) {
				writeOperation(getMessageHelper().getPluralizedMessage(
						"operation.knit", 1));
			} else if (operation.getType() == SimpleInstruction.Type.WORK_EVEN) {
				writeOperation(getMessageHelper().getPluralizedMessage(
						"operation.work-even", 1));
			}
		}
	}

	public boolean isWithinOperationSet() {
		return !operationSetStack.empty();
	}

	public OperationSet getCurrentOperationSet() {
		return operationSetStack.peek();
	}

	public void addNewOperationSet(OperationSet newOperationSet) {
		operationSetStack.push(newOperationSet);
	}

	public OperationSet removeCurrentOperationSet() {
		return operationSetStack.pop();
	}

	/**
	 * The parameter 'potentiallySimple' is an indicator for the renderer as to
	 * whether the operation set comprises only of one operation, either which
	 * is 'Knit' or 'Purl'. Such operation sets may be rendered differently
	 * (i.e. "Row 1: Knit" vs. "Row 1: k to end") so we process the option
	 * differently. Of course, the properties file for the locale may choose to
	 * express a "knit-only" operation set in the exact same manner as "k to
	 * end."
	 * 
	 * @param operationSet
	 * @param potentiallySimple
	 *            flag which indicates if the operation set has the potential to
	 *            consist of a 'knit' or 'purl' only
	 * @return whether the operation set <i>was</i>, from the perspective of the
	 *         current recursive call, actually simple
	 */
	public void renderCurrentOperationSet() {
		renderOperationSet(getCurrentOperationSet(), true);
	}

	public boolean isCurrentOperationSetEmpty() {
		return getCurrentOperationSet().size() == 0;
	}

	private boolean renderOperationSet(OperationSet operationSet,
			boolean potentiallySimple) {
		if (operationSet.size() > 1) {
			potentiallySimple = false;
		}
		if (operationSet.getHead() != null) {
			getWriterHelper().write(
					StringUtils.capitalize(operationSet.getHead()));
		}

		Iterator<Object> it = operationSet.iterator();
		while (it.hasNext()) {
			Object operation = it.next();
			boolean useComma = true;
			if (operation instanceof RepeatOperationSet) {
				// a RepeatInstructionSet takes additional handling
				renderRepeatInstructionSet((RepeatOperationSet) operation,
						potentiallySimple);
			} else if (operation instanceof OperationSet) {
				OperationSet nestedOperationSet = (OperationSet) operation;
				if (nestedOperationSet.getType() == Type.INLINE_INSTRUCTION) {
					renderOperationSet(nestedOperationSet, potentiallySimple);
				} else if (nestedOperationSet.getType() == Type.USING_NEEDLE) {
					// an operation set of type USING_NEEDLE
					getWriterHelper().writeNewLine();
					getWriterHelper().incrementIndent();
					getWriterHelper().writeIndent();
					renderOperationSet(nestedOperationSet, potentiallySimple);
					getWriterHelper().decrementIndent();
					useComma = false;
				} else if (nestedOperationSet.getType() == Type.FROM_STITCH_HOLDER) {
					renderFromStitchHolderOperationSet((FromStitchHolderOperationSet) nestedOperationSet);
				} else {
					renderOperationSet(nestedOperationSet, potentiallySimple);
				}
			} else if (operation instanceof SimpleInstruction) {
				SimpleInstruction simpleInstruction = (SimpleInstruction) operation;
				String message;
				if (simpleInstruction.getType() == SimpleInstruction.Type.KNIT) {
					if (potentiallySimple) {
						message = getMessage("operation.knit-only");
					} else {
						message = getMessageHelper().getPluralizedMessage(
								"operation.knit", 1, StringUtils.EMPTY);
					}
				} else if (simpleInstruction.getType() == SimpleInstruction.Type.PURL) {
					if (potentiallySimple) {
						message = getMessage("operation.purl-only");
					} else {
						message = getMessageHelper().getPluralizedMessage(
								"operation.purl", 1, StringUtils.EMPTY);
					}
				} else if (simpleInstruction.getType() == SimpleInstruction.Type.WORK_EVEN) {
					if (potentiallySimple) {
						message = getMessage("operation.work-even-only");
					} else {
						message = getMessage("operation.work-even-no-args");
					}
				} else {
					throw new IllegalArgumentException(
							"Unhandled operation type "
									+ simpleInstruction.getType());
				}
				// some messages may take one argument
				getWriterHelper().write(message);
			} else {
				// it's something but it's not simple
				potentiallySimple = false;
				getWriterHelper().write(String.valueOf(operation));
			}
			if (it.hasNext() && useComma) {
				getWriterHelper().write(
						getMessage("operation.list-item-separator") + " ");
			}
		}
		String tail = operationSet.getTail();
		if (tail != null && operationSet.getType() == Type.INC_INTO_NEXT_ST) {
			// FIXME Hmm... I don't particularly like this
			getWriterHelper().write(tail);
		}
		else if (tail != null) {
			getWriterHelper().write(". " + StringUtils.capitalize(tail));
			if (!tail.endsWith(".")) {
				getWriterHelper().write(".");
			}
		}
		return potentiallySimple;
	}

	private String getMessage(String messageKey, Object... args) {
		return getMessageHelper().getMessage(messageKey, args);
	}

	private boolean renderRepeatInstructionSet(
			RepeatOperationSet repeatInstructionSet, boolean potentiallySimple) {
		boolean singularRepeat = false;
		if (repeatInstructionSet.size() == 1) {
			singularRepeat = true;
		}
		if (!singularRepeat || !repeatInstructionSet.isToEnd()) {
			potentiallySimple = false;
		}
		if (!singularRepeat) {
			getWriterHelper().write(getMessage("operation.repeat.begin"));
		}
		boolean simple = renderOperationSet(repeatInstructionSet,
				potentiallySimple);
		if (!singularRepeat) {
			getWriterHelper().write(getMessage("operation.repeat.end"));
		}
		if (repeatInstructionSet.getUntilInstruction() != null && !simple) {
			getWriterHelper().write(" ");
			getWriterHelper().write(repeatInstructionSet.getUntilInstruction());
		}
		return simple;
	}

	private void renderFromStitchHolderOperationSet(
			FromStitchHolderOperationSet operationSet) {
		getWriterHelper().startWritingToSegment("temp");
		renderOperationSet(operationSet, false);
		getWriterHelper().stopWritingToSegment("temp");
		String fromStitchHolderOperations = getWriterHelper()
				.getSegment("temp");
		StringBuffer messageKey = new StringBuffer(
				"operation.from-stitch-holder");
		List<String> values = new ArrayList<String>();
		values.add(fromStitchHolderOperations);
		if (operationSet.size() == 1) {
			messageKey.append(".single");
		} else {
			messageKey.append(".multiple");
		}
		if (!StringUtils.isBlank(operationSet.getLabel())) {
			messageKey.append(".with-label");
			values.add(operationSet.getLabel());
		}
		String message = getMessageHelper().getMessageNoVarargs(
				messageKey.toString(), values.toArray());
		getWriterHelper().write(message);
	}

}
