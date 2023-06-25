package searchengine.services.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import searchengine.config.Site;
import searchengine.config.SitesList;
import searchengine.dto.statistics.DetailedStatisticsItem;
import searchengine.dto.statistics.StatisticsData;
import searchengine.dto.response.StatisticsResponse;
import searchengine.dto.statistics.TotalStatistics;
import searchengine.model.IndexedStatus;
import searchengine.model.SiteEntity;
import searchengine.model.repository.LemmaRepository;
import searchengine.model.repository.PageRepository;
import searchengine.model.repository.SiteRepository;
import searchengine.services.StatisticsService;

import java.lang.constant.Constable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private  final SiteRepository siteRepository;
    private  final PageRepository pageRepository;
    private  final LemmaRepository lemmaRepository;
    private final SitesList sites;

    @Override
    public StatisticsResponse getStatistics() {
    TotalStatistics totalStatistics = new TotalStatistics();
    totalStatistics.setSites(sites.getSites().size());
    List<Site> siteList = sites.getSites();
    List<DetailedStatisticsItem> detailedStatisticsItems = new ArrayList<>();
    totalStatistics.setIndexing(false);
    for (Site site : siteList){
        IndexedStatus indexedStatus = siteRepository.findSiteEntityByUrl(site.getUrl()).getStatus();
        if (indexedStatus != null && indexedStatus.equals(IndexedStatus.INDEXING)){
            totalStatistics.setIndexing(true);
        }
        DetailedStatisticsItem item = new DetailedStatisticsItem();
        item.setName(site.getName());
        item.setUrl(site.getUrl().toString());
        SiteEntity siteEntity = siteRepository.findSiteEntityByUrl(site.getUrl());
        int pages = pageRepository.CountBySite(siteEntity) == null ? 0 : pageRepository.CountBySite(siteEntity);
        item.setPages(pages);
        int lemmas = lemmaRepository.countBySite(siteEntity);
        item.setLemmas(lemmas);

        item.setStatus(indexedStatus == null ? "" : indexedStatus.toString());
        Constable errorSiteByUrl = siteRepository.findErrorByUrl(site.getUrl());
        item.setError(errorSiteByUrl.toString() == null ? "" : errorSiteByUrl.toString());
        item.setStatusTime(new Date().getTime());
        totalStatistics.setPages(totalStatistics.getPages() + pages);
        totalStatistics.setLemmas(totalStatistics.getLemmas() + lemmas);
        detailedStatisticsItems.add(item);
    }

    return getResponse(totalStatistics, detailedStatisticsItems);


    }

    private StatisticsResponse getResponse(TotalStatistics totalStatistics, List<DetailedStatisticsItem> detailedStatisticsItems) {
    StatisticsResponse response = new StatisticsResponse();
    StatisticsData data = new StatisticsData();
    data.setTotal(totalStatistics);
    data.setDetailed(detailedStatisticsItems);
    response.setStatistics(data);
    response.setResult(true);
    return response;
    }

}
