package org.jabref.gui.exporter;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import org.jabref.logic.exporter.ExportFormat;
import org.jabref.model.database.BibDatabaseContext;
import org.jabref.model.entry.BibEntry;

public class BibtexExportFormat extends ExportFormat {

    public String exportAsString(final BibDatabaseContext databaseContext, List<BibEntry> entries) {
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
