package se.kau.cs.serg.cabref.beans;

import java.util.HashMap;

import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.FieldName;
import org.thymeleaf.expression.Strings;

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
	
	private String booktitle;

	private String volume;

	private String number;

	private String editor;

	private String series;

	private String pages;

	private String address;

	private String month;

	private String organization;

	private String publisher;

	private String note;

	public BibEntryBean() {
		key = "";
		type = "";
		author = "";
		journal = "";
		year = "";
		title = "";
		booktitle = "";
		volume = "";
		number = "";
		editor = "";
		series = "";
		pages = "";
		address = "";
		month = "";
		organization = "";
		publisher = "";
		note = "";
	}

	public BibEntryBean(BibEntry entry) {
		this.key = entry.getCiteKeyOptional().orElse("");
		this.type = entry.getType();
		this.author = entry.getFieldOrAliasLatexFree(FieldName.AUTHOR).orElse("");
		this.year = entry.getFieldOrAliasLatexFree(FieldName.YEAR).orElse("");
		this.title = entry.getFieldOrAliasLatexFree(FieldName.TITLE).orElse("");
		this.volume = entry.getFieldOrAliasLatexFree(FieldName.VOLUME).orElse("");
		this.number = entry.getFieldOrAliasLatexFree(FieldName.NUMBER).orElse("");
		this.booktitle = entry.getFieldOrAliasLatexFree(FieldName.BOOKTITLE).orElse("");
		this.journal = entry.getFieldOrAliasLatexFree(FieldName.JOURNAL).orElse("");
		this.editor = entry.getFieldOrAliasLatexFree(FieldName.EDITOR).orElse("");
		this.series = entry.getFieldOrAliasLatexFree(FieldName.SERIES).orElse("");
		this.pages = entry.getFieldOrAliasLatexFree(FieldName.PAGES).orElse("");
		this.address = entry.getFieldOrAliasLatexFree(FieldName.ADDRESS).orElse("");
		this.month = entry.getFieldOrAliasLatexFree(FieldName.MONTH).orElse("");
		this.organization = entry.getFieldOrAliasLatexFree(FieldName.ORGANIZATION).orElse("");
		this.publisher = entry.getFieldOrAliasLatexFree(FieldName.PUBLISHER).orElse("");
		this.note = entry.getFieldOrAliasLatexFree(FieldName.NOTE).orElse("");
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
	
	public String getBookTitle() {
		return booktitle;
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
	
	public String getEditor() {
		return editor;
	}

	public String getSeries() {
		return series;
	}

	public String getPages() {
		return pages;
	}

	public String getAddress() {
		return address;
	}

	public String getMonth() {
		return month;
	}

	public String getOrganization() {
		return organization;
	}

	public String getPublisher() {
		return publisher;
	}

	public String getNote() {
		return note;
	}

}
