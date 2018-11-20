package se.kau.cs.serg.cabref.server;

import java.beans.Encoder;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.jabref.Globals;
import org.jabref.gui.exporter.BibtexExportFormat;
import org.jabref.gui.exporter.ExportAction;
import org.jabref.logic.exporter.BibTeXMLExportFormat;
import org.jabref.logic.exporter.BibtexDatabaseWriter;
import org.jabref.logic.exporter.ExportFormat;
import org.jabref.logic.exporter.FileSaveSession;
import org.jabref.logic.exporter.SaveException;
import org.jabref.logic.exporter.SavePreferences;
import org.jabref.logic.exporter.SaveSession;
import org.jabref.logic.importer.FetcherException;
import org.jabref.logic.importer.Parser;
import org.jabref.logic.importer.ParserResult;
import org.jabref.logic.importer.fetcher.DiVA;
import org.jabref.logic.importer.fileformat.BibtexParser;
import org.jabref.logic.net.URLDownload;
import org.jabref.model.database.BibDatabaseContext;
import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.FieldName;
import org.jabref.preferences.JabRefPreferences;

import se.kau.cs.serg.cabref.beans.BibEntryBean;
import spark.Response;
import spark.Spark;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

/**
 * Central server class that builds on JabRef and processes requests
 *
 */
public class CabRefServer {

	/**
	 * The final path that points to the reference file of this server
	 */
	private static final String filePath = "src/main/resources/bib/simple-file.bib";
	

	/**
	 * The file of accepted users
	 */
	private List<String> users;

	public CabRefServer() {

		// in case anything goes wrong, be sure to print the errors
		Spark.exception(Exception.class, (exception, request, response) -> {
			exception.printStackTrace();
		});

		// read all data into memory
		readEntriesFromFile();
		readUserDataFromFile();
	}

	/**
	 * Parses the user file. Gives an empty list if the file could not be parsed
	 */
	private void readUserDataFromFile() {
		try {
			users = Files.readAllLines(Paths.get("src/main/resources/config/users.txt"));
		} catch (IOException e) {
			System.out.println("Could not read users file");
			users = new LinkedList<>();
		}
	}

	/**
	 * Reads all entries from the database of this server or an empty result if
	 * the data could not be parsed
	 * 
	 * @return the bibliographic data stored by this server
	 */
	private ParserResult readEntriesFromFile() {
		JabRefPreferences prefs = JabRefPreferences.getInstance();
		BibtexParser parser = new BibtexParser(prefs.getImportFormatPreferences());

		try (InputStreamReader reader = new InputStreamReader(new FileInputStream(filePath))) {
			return parser.parse(reader);
		} catch (IOException e) {
			System.out.println("Could not load main reference file");
			return new ParserResult();
		}
	}

	/**
	 * Reads the bibliographic data stored by this server and returns only the
	 * entry data in bean format
	 * 
	 * @return a list of bibliographic entries stored by this server in bean
	 *         format
	 */
	public List<BibEntryBean> getEntries() {
		ParserResult parserResult = readEntriesFromFile();

		List<BibEntryBean> entries = new LinkedList<>();
		for (BibEntry entry : parserResult.getDatabase().getEntries()) {
			entries.add(new BibEntryBean(entry));
		}
		return entries;
	}

	/**
	 * Checks if a user token is stored in this server
	 * 
	 * @param user
	 *            the user token that should be checked
	 * @return true, if the user token is known by this server, false otherwise
	 */
	public boolean authenticate(String user) {
		return users.contains(user);
	}

	/**
	 * Gets the bibliographic data for a specific citation key in bean format
	 * 
	 * @param key
	 *            the citation key for which the data should be fetched
	 * @return the bibliographic data of the citation key in bean format or an
	 *         empty bean if no data could be found for this key
	 */
	public BibEntryBean getEntry(String key) {
		ParserResult parserResult = readEntriesFromFile();

		return new BibEntryBean(parserResult.getDatabase().getEntryByKey(key).orElse(new BibEntry()));
	}

	/**
	 * Removes the bibliographic data for a specific citation key from the data
	 * stored in this server
	 * 
	 * @param key
	 *            the citation key for which the data should be removed
	 */
	public synchronized void deleteEntry(String key) {
		ParserResult parserResult = readEntriesFromFile();

		Optional<BibEntry> entry = parserResult.getDatabase().getEntryByKey(key);
		if (entry.isPresent()) {
			parserResult.getDatabase().removeEntry(entry.get());
			writeDataToDisk(parserResult.getDatabaseContext());
		}

	}

	/**
	 * Updates the bibliographic data for a specific citation key
	 * 
	 * @param key
	 *            the key for which the data should be updated
	 * @param type
	 *            the new type of the entry
	 * @param author
	 *            the new author string of the entry
	 * @param title
	 *            the new title of the entry
	 * @param journal
	 *            the new journal of the entry
	 * @param volume
	 *            the new volume of the entry
	 * @param number
	 *            the new number of the entry
	 * @param year
	 *            the new year of the entry
	 */
	public synchronized void updateEntry(String key, String type, String author, String title, String journal,
			String volume, String number, String year) {
		ParserResult parserResult = readEntriesFromFile();

		Optional<BibEntry> optionalEntry = parserResult.getDatabase().getEntryByKey(key);

		if (optionalEntry.isPresent()) {
			BibEntry entry = optionalEntry.get();
			entry.setType(type);
			entry.setField(FieldName.AUTHOR, author);
			entry.setField(FieldName.TITLE, title);
			entry.setField(FieldName.JOURNAL, journal);
			entry.setField(FieldName.VOLUME, volume);
			entry.setField(FieldName.NUMBER, number);
			entry.setField(FieldName.YEAR, year);

			writeDataToDisk(parserResult.getDatabaseContext());

			return;
		}

	}

	/**
	 * Adds a new entry for a specific citation key to the database
	 * 
	 * @param key
	 *            the new key to be added
	 */
	public synchronized void addNewEntry(String key) {
		ParserResult parserResult = readEntriesFromFile();

		BibEntry entry = new BibEntry();
		entry.setCiteKey(key);
		parserResult.getDatabase().insertEntry(entry);

		writeDataToDisk(parserResult.getDatabaseContext());
	}

	/**
	 * Writes the current bibliographic data that is stored from memory to disk
	 * 
	 * @param context
	 *            the database context into which the data should be written
	 */
	private void writeDataToDisk(BibDatabaseContext context) {
		BibtexDatabaseWriter<SaveSession> databaseWriter = new BibtexDatabaseWriter<>(FileSaveSession::new);
		try {
			SaveSession session = databaseWriter.saveDatabase(context, new SavePreferences());
			session.commit(filePath);
		} catch (SaveException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void importFromDiVa(String id)
	{	
		JabRefPreferences jrp = JabRefPreferences.getInstance();
		DiVA divaImporter = new DiVA(jrp.getImportFormatPreferences());
		
		id = divaImporter.retrieveDiVaId(id);
		
		
		
		BibEntry newEntry = divaImporter.getEntry(id);
		
		
		ParserResult parserResult = readEntriesFromFile();
		parserResult.getDatabase().insertEntry(newEntry);
		
		writeDataToDisk(parserResult.getDatabaseContext());
		
		
		
	}
	
	public synchronized void export(String format, Response res) 
	{
		ParserResult parserResult = readEntriesFromFile();
		BibDatabaseContext context = parserResult.getDatabaseContext();		
		String exportString;		
		BibTeXMLExportFormat XMLformat = new BibTeXMLExportFormat();
		BibtexExportFormat BibFormat = new BibtexExportFormat();
		
		if(format.isEmpty()) {
			return;
		}
		
		try {
				switch(format) {
				case "XML":
					exportString = XMLformat.exportAsString(context, context.getEntries());
					writeHTTPResponse(res, exportString, "export.xml");
					break;
				case "Bibtex":
					exportString = BibFormat.exportAsString(context, context.getEntries());
					writeHTTPResponse(res, exportString, "export.bib");
					break;
				default: 
					break;
				}
		} catch (SaveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void writeHTTPResponse(Response res, String exportString, String filename) {
		HttpServletResponse raw = res.raw();
		res.header("Content-Disposition", "attachment; filename=" + filename); 
		res.type("application/force-download");
		try {
			raw.getOutputStream().write(exportString.getBytes());
			raw.getOutputStream().flush();
			raw.getOutputStream().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
