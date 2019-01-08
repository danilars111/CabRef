package se.kau.cs.serg.cabref.beans;

import java.util.HashMap;
import java.util.Map.Entry;

import org.jabref.model.entry.BibEntry;

/**
 * Adapter class that takes information from a BibEntry instance from JabRef and
 * provides it in a simple beans style. This is needed so that the data can be
 * displayed nicely in Thymeleaf
 *
 */
public class BibEntryBean {

	private HashMap<String, String> fields;


	public BibEntryBean() {
		fields = new HashMap<String, String>();
	}

	public BibEntryBean(BibEntry entry) {
		fields = new HashMap<String, String>();
		fields.put("key", entry.getCiteKeyOptional().orElse(""));
		fields.put("type", entry.getType());
		
		for(Entry<String, String> entryField : entry.getFieldMap().entrySet()) {
			fields.put(entryField.getKey(), entry.getFieldOrAliasLatexFree(entryField.getKey()).orElse(""));
		}
	}

	public BibEntryBean(HashMap<String, String> values) {

	}

	public String getAuthor() {
		return fields.get("author");
	}

	public String getJournal() {
		return fields.get("journal");
	}

	public String getYear() {
		return fields.get("year");
	}

	public String getTitle() {
		return fields.get("title");
	}
	
	public String getBookTitle() {
		return fields.get("booktitle");
	}

	public String getVolume() {
		return fields.get("volume");
	}

	public String getNumber() {
		return fields.get("number");
	}

	public String getKey() {
		return fields.get("key");
	}

	public String getType() {
		return fields.get("type");
	}
	
	public String getEditor() {
		return fields.get("editor");
	}

	public String getSeries() {
		return fields.get("series");
	}

	public String getPages() {
		return fields.get("pages");
	}

	public String getAddress() {
		return fields.get("address");
	}

	public String getMonth() {
		return fields.get("month");
	}

	public String getOrganization() {
		return fields.get("organization");
	}

	public String getPublisher() {
		return fields.get("publisher");
	}
	
	public String getISSN() {
		return fields.get("issn");
	}

	public String getNote() {
		return fields.get("note");
	}

}
