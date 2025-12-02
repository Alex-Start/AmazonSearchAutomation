package base;

import pages.HomePageAction;
import pages.SearchResultsPageAction;

public class Action {
    private HomePageAction homePageAction;
    private SearchResultsPageAction searchResultsPageAction;

    public HomePageAction homePage() {
        if (homePageAction != null) {
            return homePageAction;
        }
        return homePageAction = new HomePageAction();
    }

    public SearchResultsPageAction searchResult() {
        if (searchResultsPageAction != null) {
            return searchResultsPageAction;
        }
        return searchResultsPageAction = new SearchResultsPageAction();
    }

}
