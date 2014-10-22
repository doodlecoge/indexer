package com.ajaxw.ds;

import java.util.ArrayList;
import java.util.List;

public class Columns {
	List<Column> cols = new ArrayList<Column>();

	public void addColumn(Column col) {
		
		for (Column c : this.cols) {
			if (c.equals(col))
				throw new IllegalArgumentException("Column " + col.getName()
						+ " exists.");
		}
		
		this.cols.add(col);
	}

	public void addColumn(String colName) {
		this.cols.add(new Column(colName));
	}
}
