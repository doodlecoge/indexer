package com.ajaxw.ds;

public class Row {
	private String line;

	public Row(String line) {
		this.line = line;
	}

	public String get(int idx) {
		String[] cols = this.line.split(",");
		if (idx >= cols.length || idx < 0)
			return null;

		return cols[idx].trim();
	}
}
