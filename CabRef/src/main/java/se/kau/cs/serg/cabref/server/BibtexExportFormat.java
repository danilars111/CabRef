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

import org.jabref.logic.exporter.ExportFormat;
import org.jabref.logic.exporter.SaveException;
import org.jabref.model.database.BibDatabaseContext;
import org.jabref.model.entry.BibEntry;

public class BibtexExportFormat extends ExportFormat {

	@Override
    public void performExport(final BibDatabaseContext databaseContext, final String resultFile, final Charset encoding,
            List<BibEntry> entries) throws SaveException {
		try {
			FileWriter fw = new FileWriter(resultFile);
			Objects.requireNonNull(databaseContext);
	        Objects.requireNonNull(entries);
	        
	        if (entries.isEmpty()) { // Only export if entries exist
	        	fw.close();
	            return;
	        }
	        
	        for (BibEntry bibEntry : entries) {
	            writeEntryToFile(bibEntry, fw);
	        }
	        
	        fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void writeEntryToFile(BibEntry bibEntry, FileWriter fw) {		
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
		stringEntry += "}\n";
		System.out.print(stringEntry);
		try {
			fw.write(stringEntry);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
}
