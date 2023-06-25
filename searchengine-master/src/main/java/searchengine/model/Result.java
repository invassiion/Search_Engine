package searchengine.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Result  implements  Comparable<Result>{

    private String uri;
    private String title;
    private String snippet;
    private float relevance;

    public Result(String path, double relevance) {
    }

    @Override
    public int compareTo(Result o) {
        return 0;
    }
    public String toString(){
        return  "Uri: " + uri + " Title: " + title + " Snippet: " + snippet + " Relevance: " + relevance + System.lineSeparator();
    }
}
