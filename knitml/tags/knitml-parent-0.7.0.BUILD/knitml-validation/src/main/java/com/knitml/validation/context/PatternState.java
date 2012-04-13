package com.knitml.validation.context;

import java.util.List;

import com.knitml.core.model.operations.block.Instruction;
import com.knitml.core.model.operations.block.InstructionHolder;
import com.knitml.core.model.operations.block.Row;
import com.knitml.core.model.operations.inline.ApplyNextRow;
import com.knitml.core.model.operations.inline.Repeat;
import com.knitml.core.model.pattern.InstructionGroup;
import com.knitml.core.model.pattern.Section;
import com.knitml.validation.common.InvalidStructureException;

public interface PatternState {

	boolean isReplayMode();

	/**
	 * Sets the replay mode to true. Implementations should keep track of nested
	 * calls so that {@link #setReplayMode(false)} is eventually called as many
	 * times as {@link #setReplayMode(true)}. That is, calling
	 * {@link #setReplayMode(true)} twice and then {@link #setReplayMode(false)}
	 * means that {@link #isReplayMode()} should return true, until
	 * {@link #setReplayMode(false)} is called a second time.
	 * 
	 * @return true if within an instruction, false otherwise
	 */
	void setReplayMode(boolean replay);

	void setAsCurrent(InstructionGroup instructionGroup);

	void setAsCurrent(Section section);

	void setAsCurrent(Instruction instruction);

	void setAsCurrent(Row row, Integer currentlyExecutingRow);

	void clearCurrentRow();

	void nextRepeatOfCurrentInstruction();

	void clearCurrentInstruction();

	public int nextAvailableSectionNumber();

	/**
	 * Indicates whether the current state is within the execution of an
	 * Instruction operation.
	 * 
	 * @return true if within an instruction, false otherwise
	 */
	boolean isWithinInstruction();

	/**
	 * Indicates whether the current state is within the first row of the
	 * execution of an Instruction operation.
	 * 
	 * @return true if within the first row of an instruction, false otherwise
	 */
	public boolean isAtFirstRowWithinInstruction();

	List<Object> getLocationBreadcrumb();

	void useInstruction(Instruction instruction)
			throws InvalidStructureException;

	InstructionHolder getInstructionInUse(String id);

	int getHeaderRowNumber();

	void setHeaderRowNumber(int newRowNumber);

	/**
	 * Clients should call this method before attempting to call
	 * {@link InstructionHolder#getNextRow()} to implement the
	 * "active row for instruction" concept.
	 * 
	 * @param instructionId
	 * @return the active row for this instruction, or null if none is found
	 * @see #setActiveRowForInstruction(String, Row)
	 */
	Row getActiveRowForInstruction(String instructionId);

	/**
	 * <p>
	 * The "active row for instruction" concept was put in place to prevent
	 * inline {@link Repeat} operations from advancing any instructions
	 * referenced using {@link ApplyNextRow} more than once per row.
	 * 
	 * <p>
	 * For example, in the following pattern:
	 * 
	 * <pre>
	 * &lt;instruction id="stockinette"&gt;
	 *   &lt;row&gt;&lt;knit&gt;1&lt;/knit&gt;&lt;/row&gt;
	 *   &lt;row&gt;&lt;purl&gt;1&lt;/purl&gt;&lt;/row&gt;
	 * &lt;/instruction&gt;
	 * ...  
	 * 
	 * &lt;row&gt;
	 *   &lt;repeat until="times" value="4"&gt;
	 *   	&lt;apply-next-row instruction-ref="inst-ref"/&gt;
	 *   &lt;/repeat&gt;
	 * &lt;/row&gt;
	 * </pre>
	 * 
	 * the defined row should be equivalent to:
	 * 
	 * <pre>
	 * &lt;row&gt;&lt;knit&gt;4&lt;/knit&gt;&lt;/row&gt;
	 * </pre>
	 * 
	 * and <b>not</b>:
	 * 
	 * <pre>
	 * &lt;row&gt;
	 *   &lt;knit&gt;1&lt;/knit&gt;
	 *   &lt;purl&gt;1&lt;/purl&gt;
	 *   &lt;knit&gt;1&lt;/knit&gt;
	 *   &lt;purl&gt;1&lt;/purl&gt;
	 * &lt;/row&gt;
	 * </pre>
	 * 
	 * <p>
	 * Clients should call this method after calling
	 * {@link InstructionHolder#getNextRow()} to implement this behavior.
	 */
	void setActiveRowForInstruction(String instructionId, Row activeRow);

	/**
	 * Clients should call this method at the end of a row, after all possible
	 * calls to apply-next-row have been executed.
	 * 
	 * @see #setActiveRowForInstruction(String, Row)
	 */
	void clearActiveRowsForInstructions();

}
