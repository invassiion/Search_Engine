package searchengine.services.implement;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.morphology.Morphology;
import org.springframework.stereotype.Service;

import searchengine.config.SearchCfg;
import searchengine.dto.statistics.GetSearchDto;
import searchengine.dto.statistics.SearchDto;
import searchengine.model.*;
import searchengine.model.repository.IndexxRepository;
import searchengine.model.repository.LemmaRepository;
import searchengine.model.repository.PageRepository;
import searchengine.model.repository.SiteRepository;
import searchengine.services.LemmaService;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService implements searchengine.services.SearchService {

    private final Morphology morphology;
    private final LemmaRepository lemmaRepository;
    private final PageRepository pageRepository;
    private final IndexxRepository indexxRepository;
    private final SiteRepository siteRepository;
    private final LemmaService lemmaService;
    private final static int newFixedTheadPool = Runtime.getRuntime().availableProcessors();


    @SneakyThrows
    @Override
    public List<SearchDto> allSiteSearch(SearchCfg searchCfg) {
        List<SiteEntity> siteEntities = siteRepository.findAll();
        HashMap<PageEntity, Float> entityPageFloatHashMap = new HashMap<>();
        Set<String> lemmaSet = getLemmaSet(searchCfg.getQuery());
        for (SiteEntity siteEntity : siteEntities) {
            List<LemmaEntity> foundLemmaList = getLemmaListFromSite(lemmaSet, siteEntity);
            if (foundLemmaList.isEmpty()) {
                log.debug("Поисковый запрос" + siteEntity.getName() + "обработан. Ответ пустой.");
                continue;
            }
            log.info("Поисковый запрос" + siteEntity.getName() + "обработан. Ответ получен.");
            entityPageFloatHashMap.putAll(getPageList(foundLemmaList, siteEntity, searchCfg.getLimit()));
        }
        if (entityPageFloatHashMap.isEmpty()) {
            return new ArrayList<>();
        }
        HashMap<PageEntity, Float> resultMap = getResultMap(entityPageFloatHashMap, searchCfg.getLimit());

        return getSearchDto(resultMap, lemmaSet);
    }

    @SneakyThrows
    @Override
    public List<SearchDto> siteSearch(SearchCfg searchCfg) {
        SiteEntity site = siteRepository.findSiteEntityByUrl(searchCfg.getSite());
        Set<String> lemmaSet = getLemmaSet(searchCfg.getQuery());
        List<LemmaEntity> foundLemmaList = getLemmaListFromSite(lemmaSet, site);
        if (foundLemmaList.isEmpty()) {
            log.debug("Поисковый запрос обработан. Ответ пустой.");
            return new ArrayList<>();
        }
        log.info("Поисковый запрос обработан. Ответ получен.");
        HashMap<PageEntity, Float> pageFloatHashMap = getPageList(foundLemmaList, site, searchCfg.getLimit());
        HashMap<PageEntity, Float> resultMap = getResultMap(pageFloatHashMap, searchCfg.getLimit());
        if (pageFloatHashMap.isEmpty()) {
            return new ArrayList<>();
        }
        return getSearchDto(resultMap, lemmaSet);
    }

    private List<LemmaEntity> getLemmaListFromSite(Set<String> words, SiteEntity siteEntity) {
        List<LemmaEntity> lemmaEntities = lemmaRepository.selectLemmaBySyte(words, siteEntity);
        lemmaEntities.sort(Comparable::compareTo);
        return lemmaEntities;
    }

    private HashMap<PageEntity, Float> getPageList(List<LemmaEntity> lemmaEntities, SiteEntity siteEntity, int limit) {
        HashMap<PageEntity, Float> resultMap = new HashMap<>();
        String firstLemma = lemmaEntities.stream().findFirst().get().getLemma();
       List<PageEntity> pageEntities = pageRepository.ByLemma(firstLemma, siteEntity);
        if (pageEntities.isEmpty()) {
            return new HashMap<>();
        } else {
            if (lemmaEntities.size() == 1) {
                for (PageEntity pageEntity : pageEntities) {
                    IndexEntity indexEntity = indexxRepository.findByPageAndLemma(pageEntity, lemmaEntities.get(0));
                    resultMap.put(pageEntity, indexEntity.getRank());
                }
                return getSortedMap(resultMap, limit);
            }
            List<IndexEntity> indexEntities = indexxRepository.findByPagesAndLemmas(lemmaEntities, pageEntities);
            Map<PageEntity, IndexEntity> collect1 = new HashMap<>();
            for (IndexEntity indexEntity : indexEntities) {
                collect1.put(indexEntity.getPageEntity(), indexEntity);
            }

            for (LemmaEntity lemmaEntity : lemmaEntities) {
                if (lemmaEntity.getLemma().equals(firstLemma)) {
                    continue;
                }

                for (PageEntity pageEntity : pageEntities) {
                    if (collect1.get(pageEntity) == null) {
                        resultMap.remove(pageEntity);
                        pageEntities.remove(pageEntity);
                    } else {
                        float rank = resultMap.get(pageEntity) == null ? 0 : resultMap.get(pageEntity);
                        resultMap.put(pageEntity, collect1.get(pageEntity).getRank() + rank);
                    }
                }
            }
            return getSortedMap(resultMap, limit);
        }

    }

    private HashMap<PageEntity, Float> getResultMap(HashMap<PageEntity, Float> entityPageFloatHashMap, int limit) {
        Float maxRank = entityPageFloatHashMap.entrySet().stream().findFirst().get().getValue();
        entityPageFloatHashMap.replaceAll((k, v) -> v / maxRank);
        return entityPageFloatHashMap.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .limit(limit).collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a, LinkedHashMap::new));

    }

    private List<SearchDto> getSearchDto(HashMap<PageEntity, Float> entityPageFloatHaskMap, Set<String> lemmasEntity) throws ExecutionException, InterruptedException {
        List<SearchDto> resultList = new ArrayList<>();
        List<Future> tasks = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(newFixedTheadPool);
        for (Map.Entry<PageEntity, Float> entry : entityPageFloatHaskMap.entrySet()) {
            GetSearchDto searchDto = new GetSearchDto(entry, lemmasEntity, morphology, lemmaService);
            var submit = executorService.submit(searchDto);
            tasks.add(submit);
        }
        for (Future future : tasks) {
            SearchDto searchDto = (SearchDto) future.get();
            resultList.add(searchDto);
        }
        return resultList;
    }

    private Set<String> getLemmaSet(String searchText) {
        String[] splitText = searchText.split("\\s+");
        Set<String> result = new HashSet<>();
        for (String word : splitText) {
            List<String> lemma = morphology.getNormalForms(word);
            result.addAll(lemma);
        }
        return result;
    }

    private HashMap<PageEntity, Float> getSortedMap(HashMap<PageEntity, Float> resultMap, int limit) {
        return resultMap.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .limit(limit).collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a, LinkedHashMap::new));

    }

}
