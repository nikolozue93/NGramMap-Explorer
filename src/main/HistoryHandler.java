package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import ngrams.NGramMap;
import ngrams.TimeSeries;
import plotting.Plotter;
import org.knowm.xchart.XYChart;

import java.util.ArrayList;
import java.util.List;

public class HistoryHandler extends NgordnetQueryHandler {
    NGramMap map;

    public HistoryHandler(NGramMap map){
        this.map = map;
    }

    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();

        ArrayList<TimeSeries> ts = new ArrayList<>();

        for(String word : words){
            ts.add(map.weightHistory(word, startYear, endYear));
        }

        // generating chart using words and retrieved timeseries from the above loop
        XYChart chart = Plotter.generateTimeSeriesChart(words, ts);
        String encoded = Plotter.encodeChartAsString(chart); // encoding the chart into 64 base string

        return encoded;
    }
}
