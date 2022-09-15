package com.eden.hackerdesktopapi.service.intf;

import com.eden.hackerdesktopapi.constant.consists.Result;
import com.eden.hackerdesktopapi.constant.enums.ProviderEnum;
import com.eden.hackerdesktopapi.model.News;
import com.rometools.rome.io.FeedException;
//import com.sun.corba.se.spi.ior.ObjectId;

import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * News service interfaces
 *
 * @author Mo Xu
 * @date 2022/08/31
 */
public interface NewsService {
    /**
     * Get weekly news by type
     *
     * @param type Provider type
     * @return {@link List}<{@link News}>
     */
    List<News> getWeeklyNews(ProviderEnum type);

    /**
     * Get all weekly news
     *
     * @return {@link Map}<{@link String}, {@link List}<{@link News}>>
     */
    Map<String, List<News>> getAllWeeklyNews();

    /**
     * Update hacker weekly news from feed
     */
    void updateHackerNewsWeekly() throws FeedException, IOException;

    /**
     * Update overflow weekly news from feed
     */
    void updateOverflowNewsWeekly() throws FeedException, IOException;

    /**
     * Update InfoQ weekly news from feed
     */
    void updateInfoQNewsWeekly() throws FeedException, IOException;

    /**
     * Update all news
     */
    void updateAllNews() throws FeedException, IOException;
}


