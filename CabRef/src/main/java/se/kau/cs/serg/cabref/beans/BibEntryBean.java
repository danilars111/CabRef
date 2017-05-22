package se.kau.cs.serg.cabref.beans;

import java.util.HashMap;

import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.FieldName;

/**
 * Adapter class that takes information from a BibEntry instance from JabRef and
 * provides it in a simple beans style. This is needed so that the data can be
 * displayed nicely in Thymeleaf
 *
 */
public class BibEntryBean {

	private String key;

	private String type;

	private String author;

	private String journal;

	private String year;

	private String title;

	private String volume;

	private String number;

	public BibEntryBean() {
		key = "";
		type = "";
		author = "";
		journal = "";
		year = "";
		title = "";
		volume = "";
		number = "";
	}

	public BibEntryBean(BibEntry entry) {
		this.key = entry.getCiteKeyOptional().orElse("");
		this.type = entry.getType();
		this.author = entry.getFieldOrAliasLatexFree(FieldName.AUTHOR).orElse("");
		this.journal = entry.getFieldOrAliasLatexFree(FieldName.JOURNAL).orElse("");
		this.year = entry.getFieldOrAliasLatexFree(FieldName.YEAR).orElse("");
		this.title = entry.getFieldOrAliasLatexFree(FieldName.TITLE).orElse("");
		this.volume = entry.getFieldOrAliasLatexFree(FieldName.VOLUME).orElse("");
		this.number = entry.getFieldOrAliasLatexFree(FieldName.NUMBER).orElse("");
	}

	public BibEntryBean(HashMap<String, String> values) {

	}

	public String getAuthor() {
		return author;
	}

	public String getJournal() {
		return journal;
	}

	public String getYear() {
		return year;
	}

	public String getTitle() {
		return title;
	}

	public String getVolume() {
		return volume;
	}

	public String getNumber() {
		return number;
	}

	public String getKey() {
		return key;
	}

	public String getType() {
		return type;
	}

}
