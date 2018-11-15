package org.jabref.logic.importer.fetcher;

import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.regex.Pattern;

import org.jabref.logic.help.HelpFile;
import org.jabref.logic.importer.FetcherException;
import org.jabref.logic.importer.IdBasedParserFetcher;
import org.jabref.logic.importer.ImportFormatPreferences;
import org.jabref.logic.importer.Parser;
import org.jabref.logic.importer.ParserResult;
import org.jabref.logic.importer.fileformat.BibtexParser;
import org.jabref.logic.net.URLDownload;
import org.jabref.model.entry.BibEntry;

import org.apache.http.client.utils.URIBuilder;

/*
 * http://www.diva-portal.org/smash/aboutdiva.jsf?dswid=-3222
 * DiVA portal contains research publications and student theses from 40 Swedish universities and research institutions.
 */
public class DiVA implements IdBasedParserFetcher {

    private final ImportFormatPreferences importFormatPreferences;
    private static final Pattern LINK_TO_BIB_PATTERN = Pattern.compile("(http:\\/\\/www.diva-portal.org\\/smash\\/getreferences[?]referenceFormat[=]BibTex[&]pids=[^\"]*)");

    public DiVA(ImportFormatPreferences importFormatPreferences) {

        this.importFormatPreferences = importFormatPreferences;
    }

    @Override
    public String getName() {
        return "DiVA";
    }

    @Override
    public HelpFile getHelpPage() {
        return HelpFile.FETCHER_DIVA;
    }

    @Override
    public URL getURLForID(String identifier) throws URISyntaxException, MalformedURLException, FetcherException {
        URIBuilder uriBuilder = new URIBuilder("http://www.diva-portal.org/smash/getreferences");

        uriBuilder.addParameter("referenceFormat", "BibTex");
        uriBuilder.addParameter("pids", identifier);

        return uriBuilder.build().toURL();
    }

    @Override
    public Parser getParser() {
        return new BibtexParser(importFormatPreferences);
    }

    public boolean isValidId(String identifier) {
        return identifier.startsWith("diva2:");
    }

    public BibEntry getEntry(String id) {
        BibEntry newEntry = null;

        if (isValidId(id)) {
            URL citationPageURL;


            try {
                citationPageURL = getURLForID(id);
                System.out.println(citationPageURL.toString());

                newEntry = downloadEntry(citationPageURL.toString());

            } catch (IOException | FetcherException | URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        return newEntry;

    }

    private BibEntry downloadEntry(String link) throws FetcherException, IOException {
        String downloadedContent = new URLDownload(link).asString();

        BibtexParser parser = (BibtexParser) getParser();
        ParserResult result = parser.parse(new StringReader(downloadedContent));

        if ((result == null) || (result.getDatabase() == null)) {
            throw new FetcherException("Parsing entry from DiVA failed.");
        }

        else {
            Collection<BibEntry> entries = result.getDatabase().getEntries();

            BibEntry entry = entries.iterator().next();
            return entry;

            }
    }

}
