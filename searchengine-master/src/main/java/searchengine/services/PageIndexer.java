package searchengine.services;

import searchengine.model.PageEntity;

public interface PageIndexer {
    void indexHtml(String html, PageEntity indexingPage);
}