package com.knitml.renderer.chart;

import java.util.ArrayList;
import java.util.List;

import com.knitml.core.common.KnittingShape;
import com.knitml.core.common.Side;

/**
 * <p>
 * A Chart represents the graphical layout of a set of operations in a set of
 * rows. It will have taken into account the layout of the stitches as they
 * relate to each other (i.e. wrong-side rows will already have had their
 * operations flipped and inverted). However, each operation is represented by a
 * "logical" symbol, not the actual symbol used for display in the chart. In
 * addition, the rows and stitches themselves are in logical order (i.e. the
 * first row in the list is the first row worked, not the last).
 * 
 * <p>A {@link ChartWriter} will take this logical graph of operations and display
 * it according to the conventions of that writer. Most {@link ChartWriter}
 * implementations will want to start at the end of the graph and work their way
 * backwards so that it displays like a "normal" chart.
 * 
 * @see ChartWriter
 * @author Jonathan Whitall (fiddlerpianist@gmail.com)
 * 
 */
public class Chart {

	private KnittingShape shape = KnittingShape.FLAT;
	private int startingRowNumber = 1;
	private Side startingSide = Side.RIGHT;
	private List<List<ChartElement>> graph;
	private String title;

	public Chart() {
		this.graph = new ArrayList<List<ChartElement>>();
	}
	
	public Chart(List<List<ChartElement>> graph) {
		this.graph = graph;
	}

	public KnittingShape getShape() {
		return shape;
	}

	public void setShape(KnittingShape shape) {
		this.shape = shape;
	}

	public int getStartingRowNumber() {
		return startingRowNumber;
	}

	public void setStartingRowNumber(int startingRowNumber) {
		this.startingRowNumber = startingRowNumber;
	}

	public Side getStartingSide() {
		return startingSide;
	}

	public void setStartingSide(Side startingSide) {
		this.startingSide = startingSide;
	}

	public List<List<ChartElement>> getGraph() {
		return graph;
	}

	public void setGraph(List<List<ChartElement>> graph) {
		this.graph = graph;
	}

	public void addRow(List<ChartElement> row) {
		this.graph.add(row);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
