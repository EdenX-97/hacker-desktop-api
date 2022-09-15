package com.eden.hackerdesktopapi.service.impl;

import com.eden.hackerdesktopapi.constant.enums.ResultEnum;
import com.eden.hackerdesktopapi.constant.exceptions.CustomizedException;
import com.eden.hackerdesktopapi.model.Feed;
import com.eden.hackerdesktopapi.repository.FeedRepository;
import com.eden.hackerdesktopapi.service.intf.RSSService;
import com.eden.hackerdesktopapi.utils.RSSUtil;
import com.eden.hackerdesktopapi.utils.RegexUtil;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
@Slf4j
public class RSSServiceImpl implements RSSService {
    private final FeedRepository feedRepository;

    @Autowired
    public RSSServiceImpl(FeedRepository feedRepository) {
        this.feedRepository = feedRepository;
    }

    @Override
    public void subscribeRSS(String url) throws IOException, FeedException {
        // Check and get feed from the url
        if (RegexUtil.checkURL(url)) {
            throw new CustomizedException(ResultEnum.INVALID_PARAM, "URL format incorrect");
        }
        SyndFeed feed = RSSUtil.getFeedFromRSSUrl(url);

        // Check if title exist
        if (feedRepository.existsFeedByTitle(feed.getTitle())) {
            throw new CustomizedException(ResultEnum.INVALID_PARAM, "This feed already exist");
        }

        // Set and save the feed
        Feed savedFeed = new Feed();
        savedFeed.setTitle(feed.getTitle());
        savedFeed.setLink(url);
        feedRepository.save(savedFeed);
    }
}
