package com.knitml.core.model.directions.block;

import static org.apache.commons.lang.ArrayUtils.toPrimitive;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.math.Range;

import com.knitml.core.common.KnittingShape;
import com.knitml.core.common.RowDefinitionScope;
import com.knitml.core.common.Side;
import com.knitml.core.model.directions.BlockOperation;
import com.knitml.core.model.directions.CompositeOperation;
import com.knitml.core.model.directions.InlineOperation;
import com.knitml.core.model.directions.information.Information;

public class Row implements BlockOperation, CompositeOperation {
	
	protected Information information;
	protected Information followupInformation;
	
	// Inline operations are defined as operations that come under a row.
	// They may be atomic or composite.
	protected List<InlineOperation> operations = new ArrayList<InlineOperation>();
	
	protected String yarnIdRef;
	protected boolean informSide;
	protected boolean shortRow = false;
	protected boolean longRow = false;
	protected Side side;
	protected Boolean assignRowNumber = true;
	protected boolean resetRowCount = false;
	protected int[] numbers = new int[0];
	protected RowDefinitionScope subsequent;
	protected KnittingShape type; // "flat" or "round"
	
	private boolean assignRowNumbersAffected = false;
	private boolean numbersAffected = false;

	public String getYarnIdRef() {
		return yarnIdRef;
	}
	public boolean isInformSide() {
		return informSide;
	}
	public boolean isShortRow() {
		return shortRow;
	}
	public Boolean getAssignRowNumber() {
		return assignRowNumber;
	}
	public boolean isResetRowCount() {
		return resetRowCount;
	}
	public int[] getNumbers() {
		return numbers;
	}
	public KnittingShape getType() {
		return type;
	}
	public List<InlineOperation> getOperations() {
		return operations;
	}
	public Side getSide() {
		return side;
	}
	
	public void setAssignRowNumber(Boolean assignRowNumber) {
		if (!this.assignRowNumbersAffected) {
			this.assignRowNumber = assignRowNumber;
			this.assignRowNumbersAffected = true;
		}
	}
	public void setNumbers(int[] numbers) {
		if (!this.numbersAffected) {
			this.numbers = numbers;
			this.numbersAffected = true;
		}
	}
	public void setNumber(int number) {
		setNumbers(new int[] { number });
	}
	
	public Row() {
	}
	
	public Row (boolean assignRowNumber, int[] rowNumbers) {
		this.assignRowNumber = assignRowNumber;
		this.numbers = rowNumbers;
	}
	
	public Row(Row row, List<InlineOperation> operations) {
		this.yarnIdRef = row.getYarnIdRef();
		this.informSide = row.isInformSide();
		this.shortRow = row.isShortRow();
		this.side = row.getSide();
		this.assignRowNumber = row.getAssignRowNumber();
		this.resetRowCount = row.isResetRowCount();
		this.numbers = row.getNumbers();
		this.type = row.getType();
		this.operations = operations;
	}
	
	public Row(Range numbers, List<InlineOperation> operations) {
		super();
		List<Integer> rowList = new ArrayList<Integer>();
		for (int i = numbers.getMinimumInteger(); i <= numbers.getMaximumInteger(); i++) {
			rowList.add(i);
		}
		this.numbers = toPrimitive(rowList.toArray(new Integer[0]));
		this.operations = operations;
	}
	public Information getInformation() {
		return information;
	}
	public void setInformation(Information information) {
		this.information = information;
	}
	public Information getFollowupInformation() {
		return followupInformation;
	}
	public void setFollowupInformation(Information followupInformation) {
		this.followupInformation = followupInformation;
	}
	
	public void setOperations(List<InlineOperation> operations) {
		this.operations = operations;
	}
	public void setYarnIdRef(String yarnIdRef) {
		this.yarnIdRef = yarnIdRef;
	}
	public void setInformSide(boolean informSide) {
		this.informSide = informSide;
	}
	public void setShortRow(boolean shortRow) {
		this.shortRow = shortRow;
	}
	public void setSide(Side side) {
		this.side = side;
	}
	public void setResetRowCount(boolean resetRowCount) {
		this.resetRowCount = resetRowCount;
	}
	public RowDefinitionScope getSubsequent() {
		return subsequent;
	}
	public void setSubsequent(RowDefinitionScope subsequent) {
		this.subsequent = subsequent;
	}
	public void setType(KnittingShape type) {
		this.type = type;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(128);
		if (isShortRow()) {
			sb.append("Short ");
		}
		if (isLongRow()) {
			sb.append("Long ");
		}
		if (getType() == KnittingShape.ROUND) {
			sb.append("Round ");
		} else {
			sb.append("Row ");
		}
		if (getNumbers() != null) {
			sb.append(ArrayUtils.toString(getNumbers()));
			sb.append(" ");
		}
		if (getSide() != null) {
			sb.append("Side: ").append(getSide()).append(" ");
		}
		if (getInformation() != null) {
			sb.append("Information: ").append(getInformation()).append(" ");
		}
		if (getFollowupInformation() != null) {
			sb.append("Followup Information: ").append(getFollowupInformation()).append(" ");
		}
		sb.append(": ");
		sb.append(getOperations());
		return sb.toString();
	}
	public boolean isLongRow() {
		return longRow;
	}
	public void setLongRow(boolean longRow) {
		this.longRow = longRow;
	}

}
