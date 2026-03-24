package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;

public class DummyHistoryHandler extends NgordnetQueryHandler {
    @Override
    public String handle(NgordnetQuery q) {
        return "";
    }
}
