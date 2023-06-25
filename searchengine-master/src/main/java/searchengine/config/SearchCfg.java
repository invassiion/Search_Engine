package searchengine.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.net.URL;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "search-settings")
public class SearchCfg {

    private String query;
    private URL site;
    private int offset;
    private int limit;

}
