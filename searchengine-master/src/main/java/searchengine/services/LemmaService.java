package searchengine.services;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public interface LemmaService {
    Map<String,Integer> getLemmasFromText(String text) throws IOException;
    void getLemmasFromUrl(URL url) throws IOException;

    List<Integer> findLemmaIndexInText(String content, String lemma) throws IOException;
    List<String> getLemma(String word) throws IOException;
}