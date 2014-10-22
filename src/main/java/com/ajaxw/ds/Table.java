package com.ajaxw.ds;

import java.util.ArrayList;
import java.util.List;

import org.apache.bcel.generic.ARRAYLENGTH;

public class Table {
	private final int colNum;
	private List<Row> rows;

	{
		this.rows = new ArrayList<Row>();
	}

	public Table(int colNum) {
		if (colNum < 1)
			throw new IllegalArgumentException("colNum (" + colNum + ") < 1");

		this.colNum = colNum;
	}

	public void addRow(String line) {
		this.rows.add(new Row(line));
	}

	public List<Row> getRows() {
		return this.rows;
	}
}
