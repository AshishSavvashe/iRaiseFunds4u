package com.appbell.iraisefund4u.resto.vo;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Holder of server response; Contains; Error Messages Actual data in table/row
 * format
 */

public class ResponseVO {



	ArrayList<String> errors = new ArrayList<String>();
	ArrayList<String> messages = new ArrayList<String>();
	TableVO table = new TableVO();

	JSONObject jsonResponse;

	public ArrayList<String> getErrors() {
		return errors;
	}

	public void setErrors(ArrayList<String> errors) {
		this.errors = errors;
	}

	public void addError(String error) {
		errors.add(error);
	}

	/**
	 * Method for getting error message from web server
	 * 
	 * @return err
	 */
	public String getErrorMessage() {
		String err = "";
		for (int i = 0, l = errors.size(); i < l; i++) {
			err = err + errors.get(i);
		}
		return err;
	}

	public boolean hasErrors() {
		return errors.size() > 0;
	}

	public ArrayList<String> getMessages() {
		return messages;
	}

	public void setMessages(ArrayList<String> messages) {
		this.messages = messages;
	}

	public void addMessage(String message) {
		messages.add(message);
	}

	public String getSuccessMessage() {
		String suc = "";
		for (int i = 0, l = messages.size(); i < l; i++) {
			suc = suc + messages.get(i);
		}
		return suc;
	}

	public TableVO getTable() {
		return table;
	}

	public void setTable(TableVO table) {
		this.table = table;
	}


	public boolean hasRowInfo() {
		return getTable() != null && getTable().size() > 0 && getTable().getRow(0) != null;
	}

	public JSONObject getJsonResponse() {
		return jsonResponse;
	}

	public void setJsonResponse(JSONObject jsonResponse) {
		this.jsonResponse = jsonResponse;
	}
}
