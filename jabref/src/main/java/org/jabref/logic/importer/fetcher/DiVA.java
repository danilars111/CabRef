package org.jabref.logic.importer.fetcher;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Iterator;
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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/*
 * http://www.diva-portal.org/smash/aboutdiva.jsf?dswid=-3222
 * DiVA portal contains research publications and student theses from 40 Swedish universities and research institutions.
 */
public class DiVA implements IdBasedParserFetcher {

    private final ImportFormatPreferences importFormatPreferences;



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

    public boolean isValidDiVaId(String identifier) {
        return identifier.startsWith("diva2:");
    }

    public BibEntry getEntry(String id) {
        BibEntry newEntry = null;

        if (isValidDiVaId(id)) {
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

    public String retrieveDiVaId(String id) {

        String searchURL = "http://diva-portal.org/smash/resultList.jsf?query=" + id;
        System.out.println(searchURL);

        if (!isValidId(id)) {
            return null;
        }

        else {
            Document doc = fetchHtmlPage(searchURL);
            boolean hasMatch = checkIfPageHasId(id, doc);

            if (hasMatch) {
                return parseDiVaId(id, doc);
            }

            else {
                return null;
            }
        }
    }

    private Document fetchHtmlPage(String url) {
        Document doc = new Document("");
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        url = "http://diva-portal.org" + doc.getElementsByClass("ui-datalist-item").first()
                .select("a").first().attr("href");
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        return doc;
    }

    private boolean checkIfPageHasId(String id, Document doc) {
        // Check if page contains ID


        System.out.println("ID to search for: " + id);
        Elements ids = doc.getElementsByClass("singleRow");
        for (Iterator<Element> i = ids.iterator(); i.hasNext();) {
            Element el = i.next();
            if (!el.getElementsContainingOwnText(id).isEmpty()) {
                return true;
            }
        }
        System.out.println("Didn't find any matching IDs under Identifiers on webpage");
        return false;
    }

    private String parseDiVaId(String id, Document doc) {
        // Parse diva2-id
        id = doc.getElementsContainingOwnText("DiVA, id:").first()
                .select("a").first().attr("href").split("=")[1];
        try {
            id = URLDecoder.decode(id, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return id;
    }


    private boolean isValidId(String id) {

        String validIds[] = {"diva2:", "10.", "978-", "979-", "oai:", "urn:"};

        for (int i = 0; i < validIds.length; i++) {
            if (id.startsWith(validIds[i])) {
                return true;
            }
        }
        return false;
    }

}
