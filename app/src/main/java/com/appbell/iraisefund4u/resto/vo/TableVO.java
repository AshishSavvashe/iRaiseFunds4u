package com.appbell.iraisefund4u.resto.vo;

import java.util.ArrayList;

/**
 * Holder rows;
 */
public class TableVO extends ArrayList<RowVO> {

	private static final long serialVersionUID = 1L;

	public void addRow(RowVO row) {
		this.add(row);
	}

	public RowVO getRow(int index) {
		return this.get(index);
	}
	
	public int getRowCount(){
		return size();
	}
}
