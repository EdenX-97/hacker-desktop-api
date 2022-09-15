package com.eden.hackerdesktopapi.service.impl;

import com.eden.hackerdesktopapi.constant.enums.ProviderEnum;
import com.eden.hackerdesktopapi.model.Feed;
import com.eden.hackerdesktopapi.model.News;
import com.eden.hackerdesktopapi.model.Podcast;
import com.eden.hackerdesktopapi.model.Provider;
import com.eden.hackerdesktopapi.repository.FeedRepository;
import com.eden.hackerdesktopapi.repository.PodcastRepository;
import com.eden.hackerdesktopapi.repository.ProviderRepository;
import com.eden.hackerdesktopapi.service.intf.PodcastService;
import com.eden.hackerdesktopapi.utils.RSSUtil;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


/**
 * Implementation of podcast service
 *
 * @author Mo Xu
 * @date 2022/09/12
 */
@Service
@Slf4j
public class PodcastServiceImpl implements PodcastService {
    private final FeedRepository feedRepository;
    private final ProviderRepository providerRepository;
    private final PodcastRepository podcastRepository;

    @Autowired
    public PodcastServiceImpl(FeedRepository feedRepository,
                           ProviderRepository providerRepository,
                           PodcastRepository podcastRepository) {
        this.feedRepository = feedRepository;
        this.providerRepository = providerRepository;
        this.podcastRepository = podcastRepository;
    }

    @Override
    public Podcast getPodcast(ProviderEnum type) {
        // Get the provider and its podcast id
        Provider provider = providerRepository.findProviderByName(type.getName());
        String podcastId = provider.getPodcastId();

        // Get the podcast
        Podcast podcast = null;
        if (podcastRepository.existsById(podcastId)) {
            podcast = podcastRepository.findById(podcastId).get();
        }

        // Return news
        return podcast;
    }

    @Override
    public void updateAccidentalTechPodcasts() throws FeedException, IOException {
        try {
            // Get the provider, feed and its news id list
            Provider provider = getProviderAndDeleteOldPodcast(ProviderEnum.ACCIDENTALTECHPODCAST);
            Feed feed = feedRepository.findFeedByTitle(ProviderEnum.ACCIDENTALTECHPODCAST.getFeedTitle());

            AtomicReference<String> savedPodcastId = new AtomicReference<>("");

            // Get the synd feed from url
            SyndFeed syndFeed = RSSUtil.getFeedFromRSSUrl(feed.getLink());

            // Get the image link of this podcast
            AtomicReference<String> imageLink = new AtomicReference<>("");
            syndFeed.getForeignMarkup().stream()
                    .filter(element -> element.getName().equals("image"))
                    .forEach(element -> {
                        imageLink.set(element.getAttributeValue("href"));
                    });

            // Process the podcast from html string
            syndFeed.getEntries().stream()
                    .limit(1).forEach((entry) -> {
                        // Create new podcast
                        Podcast podcast = new Podcast();
                        podcast.setTitle(entry.getTitle());
                        podcast.setLink(entry.getEnclosures().get(0).getUrl());
                        podcast.setImage(imageLink.get());

                        savedPodcastId.set(podcastRepository.save(podcast).getId());
                    });

            // After the podcast processed, set and save the provider
            provider.setPodcastId(savedPodcastId.get());
            provider.setLastUpdatedDate(new Date());

            providerRepository.save(provider);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private Provider getProviderAndDeleteOldPodcast(ProviderEnum type) {
        // Get the feed
        Feed feed = feedRepository.findFeedByTitle(type.getFeedTitle());

        // If hacer news provider not exist, create it
        if (!providerRepository.existsProviderByName(type.getName())) {
            Provider provider = new Provider();
            provider.setName(type.getName());
            provider.setFeedId(feed.getId());
            providerRepository.save(provider);
        }

        // Get provider and its podcast id
        Provider provider = providerRepository.findProviderByName(type.getName());
        String podcastId = provider.getPodcastId();

        // Delete now podcast
        if (podcastId != null && podcastRepository.existsById(podcastId)) {
            podcastRepository.deleteById(podcastId);
        }

        return provider;
    }
}
