package com.ajaxw.ds;

public class Column {
	private String name;

	public Column(String name) {
		if (name == null)
			throw new IllegalArgumentException("name should not be null");
		
		this.name = name;
	}

	public String toString() {
		return this.name;
	}

	public String getName() {
		return this.name;
	}

	public boolean equals(Column col) {
		return this.name.equals(col.toString());
	}

	public boolean equals(String name) {
		return this.name.equals(name);
	}
}
