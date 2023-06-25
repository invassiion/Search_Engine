package searchengine.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import searchengine.config.SearchCfg;
import searchengine.config.SitesList;
import searchengine.dto.response.ErrorResponse;
import searchengine.dto.response.SearchResponse;
import searchengine.dto.statistics.SearchDto;
import searchengine.dto.response.StatisticsResponse;
import searchengine.model.SiteEntity;
import searchengine.services.ApiService;
import searchengine.services.LemmaService;
import searchengine.services.SearchService;
import searchengine.services.StatisticsService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {
   private final StatisticsService statisticsService;
    private final ApiService apiService;
    private final LemmaService lemmaService;
    private final AtomicBoolean indexingProcessing = new AtomicBoolean(false);
    private final SitesList sitesList;
    private final SearchService searchService;

    @GetMapping("/statistics")
    public ResponseEntity<StatisticsResponse> statistics() {
        return ResponseEntity.ok(statisticsService.getStatistics());
    }
    @GetMapping("/startIndexing")
    public ResponseEntity startIndexing() {
        if(indexingProcessing.get()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("'result' : false, " +
                    "'error' : Индексация уже запущена");
        }
        else
        {
            indexingProcessing.set(true);
            Runnable start = () -> apiService.startIndexing(indexingProcessing);
            new Thread(start).start();
            return ResponseEntity.status(HttpStatus.OK).body("'result' : true");
        }
    }

    @GetMapping("/stopIndexing")
    public ResponseEntity stopIndexing() {
        if (!indexingProcessing.get()) {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("'result' : false, " +
                    "'error' : Индексация не запущена");
        }
        else
        {
            indexingProcessing.set(false);
            return ResponseEntity.status(HttpStatus.OK).body("'result' : true ");
        }
    }

    @GetMapping("/indexPage")
    public ResponseEntity indexPage(@RequestParam String refUrl) throws IOException {
        URL url = new URL(refUrl);
        SiteEntity siteEntity = new SiteEntity();
        try {
            sitesList.getSites().stream().filter(site -> url.getHost().equals(site.getUrl().getHost())).findFirst().map(site -> {
                siteEntity.setName(site.getName());
                siteEntity.setUrl(site.getUrl().toString());
                return siteEntity;
            }).orElseThrow();
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("result: false " +
                    "error: Данная страница находится за пределами сайтов " +
                    "указанных в конфигурационном файле");
        }
        apiService.refreshPage(siteEntity, url);
        return ResponseEntity.status(HttpStatus.OK).body("'result' : true ");
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(SearchCfg site) {
        List<SearchDto> listSearchDto = site.getSite() == null ?
                searchService.allSiteSearch(site) :
                searchService.siteSearch(site);
        return ResponseEntity.ok(listSearchDto.isEmpty() ?
                new ErrorResponse(false, "Поисковый запрос не найден или введен не верно") :
                new SearchResponse(true,listSearchDto.size(),listSearchDto));
    }

}
