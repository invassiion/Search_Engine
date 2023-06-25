package searchengine.dto.response;

import lombok.Getter;
import lombok.Setter;
import searchengine.dto.statistics.SearchDto;

import java.util.List;
@Getter
@Setter
public class SearchResponse {
    private boolean result;
    private int count;
    private List<SearchDto> data;

    public SearchResponse(boolean result, int count, List<SearchDto> data){
        this.result = result;
        this.count = count;
        this.data = data;
    }
}
