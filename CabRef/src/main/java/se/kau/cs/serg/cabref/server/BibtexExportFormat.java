package se.kau.cs.serg.cabref.server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.jabref.logic.exporter.ExportFormat;
import org.jabref.logic.exporter.SaveException;
import org.jabref.model.database.BibDatabaseContext;
import org.jabref.model.entry.BibEntry;

public class BibtexExportFormat extends ExportFormat {

	public String exportAsString(final BibDatabaseContext databaseContext, List<BibEntry> entries) throws SaveException {
		String exportString = "";
		Objects.requireNonNull(databaseContext);
        Objects.requireNonNull(entries);
        
        for (BibEntry bibEntry : entries) {
            exportString += parseEntry(bibEntry);
        }
        return exportString;
	}
	
	private String parseEntry(BibEntry bibEntry) {		
		String stringEntry = "@" + bibEntry.getType().toLowerCase(Locale.ENGLISH) + "{";
		if(bibEntry.getCiteKeyOptional().isPresent()) {
			 stringEntry += bibEntry.getCiteKeyOptional().get() + "\n";
		} else {
			stringEntry += "\n";
		}
		
		Set<String> fieldNames = bibEntry.getFieldNames();
		for(Iterator<String> i = fieldNames.iterator(); i.hasNext(); ) {
			String fieldName = i.next();
			stringEntry += "\t" + fieldName + " = " + "{" + bibEntry.getField(fieldName).get() + "}";
			if(i.hasNext()) {
				stringEntry += ",\n";
			} else {
				stringEntry += "\n";
			}
		}
		return stringEntry += "}\n";
	}
	
	
	
}
