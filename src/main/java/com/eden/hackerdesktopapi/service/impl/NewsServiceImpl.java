package com.eden.hackerdesktopapi.service.impl;


import com.eden.hackerdesktopapi.constant.enums.ProviderEnum;
import com.eden.hackerdesktopapi.constant.enums.ResultEnum;
import com.eden.hackerdesktopapi.constant.exceptions.CustomizedException;
import com.eden.hackerdesktopapi.model.Feed;
import com.eden.hackerdesktopapi.model.News;
import com.eden.hackerdesktopapi.model.Provider;
import com.eden.hackerdesktopapi.repository.FeedRepository;
import com.eden.hackerdesktopapi.repository.NewsRepository;
import com.eden.hackerdesktopapi.repository.ProviderRepository;
import com.eden.hackerdesktopapi.service.intf.NewsService;
import com.eden.hackerdesktopapi.utils.RSSUtil;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


@Service
@Slf4j
public class NewsServiceImpl implements NewsService {
    private final FeedRepository feedRepository;
    private final ProviderRepository providerRepository;
    private final NewsRepository newsRepository;

    @Autowired
    public NewsServiceImpl(FeedRepository feedRepository,
                           ProviderRepository providerRepository,
                           NewsRepository newsRepository) {
        this.feedRepository = feedRepository;
        this.providerRepository = providerRepository;
        this.newsRepository = newsRepository;
    }

    @Override
    public List<News> getWeeklyNews(ProviderEnum type) {
        // Check if type is News
        if (!type.getType().equals("News")) {
            // It is not news provider, return exception
            throw new CustomizedException(ResultEnum.INVALID_PARAM, "Not news provider");
        }

        // Only get news when provider exist
        List<News> news = new ArrayList<>();

        if (providerRepository.existsProviderByName(type.getName())) {
            // Get provider and delete old news
            Provider provider = providerRepository.findProviderByName(type.getName());

            if (provider.getNewsIds() != null) {
                List<String> newsIdList = provider.getNewsIds();

                // Get all news
                newsIdList.forEach(id -> news.add(newsRepository.findById(id).get()));
            }
        }

        // If exists, return all news, else return empty list
        return news;
    }

    @Override
    public Map<String, List<News>> getAllWeeklyNews() {
        Map<String, List<News>> allNews = new HashMap<>();
        Arrays.stream(ProviderEnum.values())
                .filter(type -> type.getType().equals("News"))
                .forEach(type -> {
            List<News> newsList = getWeeklyNews(type);
            allNews.put(type.getName(), newsList);
        });

        return allNews;
    }

    @Override
    public void updateHackerNewsWeekly() throws FeedException, IOException {
        // Get the provider, feed and its news id list
        Provider provider = getProviderAndDeleteOldNews(ProviderEnum.HACKERWEEKLYNEWS);
        provider.setType(ProviderEnum.HACKERWEEKLYNEWS.getType());
        Feed feed = feedRepository.findFeedByTitle(ProviderEnum.HACKERWEEKLYNEWS.getFeedTitle());

        List<String> newsIdList = new ArrayList<>();

        // Get the synd feed from url
        SyndFeed syndFeed = RSSUtil.getFeedFromRSSUrl(feed.getLink());

        // Process the news from html string
        syndFeed.getEntries().stream()
                .filter(entry -> entry.getTitle().startsWith("Hacker Newsletter #")) // Only need weekly news
                .limit(1)
                .forEach(entry -> {
                    // Parse and read html
                    Document document = Jsoup.parse(entry.getContents().toString());
                    Elements elements = Objects.requireNonNull(document.getElementById("content")) // Find the content id
                            .getElementsByTag("tr") // Find all tr tags
                            .get(2) // The third tags contains all news
                            .getElementsByTag("td") // All news are under tag td
                            .get(0).children(); // Get all news (with first two title/divider)

                    // Calculate the index of favorite news
                    AtomicInteger firstH2 = new AtomicInteger();
                    elements.stream()
                            .filter(element -> element.tagName().equals("h2")) // H2 should be the title like "Favorite"
                            .limit(1) // Find the first h2
                            .forEach(element -> firstH2.set(elements.indexOf(element))); // Calculate news number

                    AtomicInteger secondH2 = new AtomicInteger();
                    elements.stream()
                            .skip(firstH2.get() + 2) // Find the second h2
                            .filter(element -> element.tagName().equals("h2")) // H2 should be the title like "Favorite"
                            .limit(1)
                            .forEach(element -> secondH2.set(elements.indexOf(element))); // Calculate news number

                    // Process the news
                    elements.stream()
                            .skip(firstH2.get() + 2) // Skip title and divider
                            .limit(secondH2.get() - firstH2.get() - 2) // Only need favorite news
                            .forEach(element -> {
                                if (!element.children().isEmpty()) {
                                    Element elementTagA = element.getElementsByTag("a").get(0);

                                    // Create new news
                                    News news = new News();
                                    if (!elementTagA.text().equals("")) {
                                        news.setTitle(elementTagA.text());
                                        news.setLink(elementTagA.attributes().get("href"));
                                        News savedNews = newsRepository.save(news);
                                        // Add news id to provider
                                        newsIdList.add(savedNews.getId());
                                    }
                                }
                            });

                    // After all news processed, set and save the provider
                    provider.setNewsIds(newsIdList);
                    provider.setLastUpdatedDate(new Date());

                    providerRepository.save(provider);
                });
    }

    @Override
    public void updateOverflowNewsWeekly() throws FeedException, IOException {
        // Get the provider, feed and its news id list
        Provider provider = getProviderAndDeleteOldNews(ProviderEnum.OVERFLOWWEEKLYNEWS);
        provider.setType(ProviderEnum.OVERFLOWWEEKLYNEWS.getType());
        Feed feed = feedRepository.findFeedByTitle(ProviderEnum.OVERFLOWWEEKLYNEWS.getFeedTitle());

        List<String> newsIdList = new ArrayList<>();

        // Get the synd feed from url
        SyndFeed syndFeed = RSSUtil.getFeedFromRSSUrl(feed.getLink());

        // Process the news from html string
        syndFeed.getEntries().stream()
                .filter(entry -> entry.getTitle().startsWith("The Overflow #")) // Only need weekly news
                .limit(1).forEach(entry -> {
                    // Parse and read html
                    Document document = Jsoup.parse(entry.getContents().get(0).toString());
                    Elements elements = document.getElementsByTag("body").get(0).children();

                    // Calculate the index of favorite news
                    AtomicInteger firstH2 = new AtomicInteger();
                    elements.stream()
                            .filter(element -> element.tagName().equals("h2")) // H2 should be the title like "Favorite"
                            .limit(1) // Find the first h2
                            .forEach(element -> firstH2.set(elements.indexOf(element))); // Calculate news number

                    AtomicInteger lastPost = new AtomicInteger();
                    elements.stream()
                            .skip(firstH2.get() + 1) // Find the second h2
                            .filter(element -> element.tagName().equals("p")) // H2 should be the title like "Favorite"
                            .filter(element -> element.text().contains("A blast from the past"))
                            .forEach(element -> lastPost.set(elements.indexOf(element))); // Calculate news number

                    // Process the news
                    elements.stream()
                            .skip(firstH2.get() + 1) // Skip title and divider
                            .filter(element -> element.tagName().equals("p"))
                            .limit(lastPost.get() - firstH2.get() - 3) // Only need favorite news
                            .forEach(element -> {
                                Element elementTagA = element.getElementsByTag("a").get(0);

                                // Create new news
                                News news = new News();
                                news.setTitle(elementTagA.text());
                                news.setLink(elementTagA.attributes().get("href"));
                                News savedNews = newsRepository.save(news);

                                // Add news id to provider
                                newsIdList.add(savedNews.getId());
                            });

                    // After all news processed, set and save the provider
                    provider.setNewsIds(newsIdList);
                    provider.setLastUpdatedDate(new Date());

                    providerRepository.save(provider);
                });
    }

    @Override
    public void updateInfoQNewsWeekly() throws FeedException, IOException {
        // Get the provider, feed and its news id list
        Provider provider = getProviderAndDeleteOldNews(ProviderEnum.INFOQWEEEKLYNEWS);
        provider.setType(ProviderEnum.INFOQWEEEKLYNEWS.getType());
        Feed feed = feedRepository.findFeedByTitle(ProviderEnum.INFOQWEEEKLYNEWS.getFeedTitle());

        List<String> newsIdList = new ArrayList<>();

        // Get the synd feed from url
        SyndFeed syndFeed = RSSUtil.getFeedFromRSSUrl(feed.getLink());

        // Process the news from html string
        syndFeed.getEntries().stream()
                .filter(element -> {
                    // Get seven days before date
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());
                    calendar.add(Calendar.DAY_OF_MONTH, -7);
                    Date sevenDaysBeforeDate = calendar.getTime();
                    return element.getPublishedDate().after(sevenDaysBeforeDate);
                }).forEach(element -> {
                    // Create new news
                    News news = new News();
                    news.setTitle(element.getTitle());
                    news.setLink(element.getLink());
                    News savedNews = newsRepository.save(news);

                    // Add news id to provider
                    newsIdList.add(savedNews.getId());
                });

        // After all news processed, set and save the provider
        provider.setNewsIds(newsIdList);
        provider.setLastUpdatedDate(new Date());

        providerRepository.save(provider);
    }

    @Override
    public void updateAllNews() throws FeedException, IOException {
        updateHackerNewsWeekly();
    }

    private Provider getProviderAndDeleteOldNews(ProviderEnum type) {
        // Get the feed
        Feed feed = feedRepository.findFeedByTitle(type.getFeedTitle());

        // If hacer news provider not exist, create it and return
        if (!providerRepository.existsProviderByName(type.getName())) {
            Provider provider = new Provider();
            provider.setName(type.getName());
            provider.setFeedId(feed.getId());
            return provider;
        }

        // Only process the news providers
        if (type.getType().equals("News")) {
            // If provider exist and contains newsIds, delete all news and the id list
            if (providerRepository.existsProviderByNameAndNewsIdsIsNotNull(type.getName())) {
                // Get provider and its news id list
                Provider provider = providerRepository.findProviderByName(type.getName());
                List<String> newsIdList = provider.getNewsIds();

                newsIdList.forEach(id -> {
                    if (newsRepository.existsById(id)) {
                        newsRepository.deleteById(id);
                    }
                });

                return provider;
            }
        }

        // It is not news provider, return exception
        throw new CustomizedException(ResultEnum.INVALID_PARAM, "Not news provider");
    }
}
