package main;

import ngrams.NGramMap;
import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import ngrams.TimeSeries;

import java.util.List;
import java.lang.StringBuilder;

public class HistoryTextHandler extends NgordnetQueryHandler {
    private NGramMap ngm;

    public HistoryTextHandler(NGramMap ngm){
        this.ngm = ngm;
    }


    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();

        StringBuilder sb = new StringBuilder();

        for(String word : words){
            sb.append(word).append(":").append(" "); // appending initial format, first word, then : and then space
            TimeSeries weightHistory = ngm.weightHistory(word, startYear, endYear); // retrieving weightHistory of given word from NGramMap

            sb.append(weightHistory.toString()); // appending this weightHistory with toString
            sb.append("\n");
        }

        return sb.toString();
    }
}
