package com.eden.hackerdesktopapi.service.intf;


import com.eden.hackerdesktopapi.constant.enums.ProviderEnum;
import com.eden.hackerdesktopapi.model.Podcast;
import com.rometools.rome.io.FeedException;

import java.io.IOException;

/**
 * Podcast service interfaces
 *
 * @author Mo Xu
 * @date 2022/09/12
 */
public interface PodcastService {
    /**
     * Get podcast by provider type
     *
     * @param type Provider type from enum
     * @return {@link Podcast}
     */
    Podcast getPodcast(ProviderEnum type);

    /**
     * Update the accidental tech podcasts
     */
    void updateAccidentalTechPodcasts() throws FeedException, IOException;
}
