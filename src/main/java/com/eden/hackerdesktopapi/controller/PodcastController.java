package com.eden.hackerdesktopapi.controller;

import com.eden.hackerdesktopapi.constant.consists.Result;
import com.eden.hackerdesktopapi.constant.enums.ProviderEnum;
import com.eden.hackerdesktopapi.service.intf.PodcastService;
import com.eden.hackerdesktopapi.utils.ReturnResultUtil;
import com.rometools.rome.io.FeedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.io.IOException;


/**
 * Controller for podcasts
 *
 * @author Mo Xu
 * @date 2022/09/12
 */
@RestController
@Validated
public class PodcastController {
    private final PodcastService podcastService;

    @Autowired
    public PodcastController(PodcastService podcastService) {
        this.podcastService = podcastService;
    }

    /**
     * Get the podcast by provider type
     *
     * @param type Provider type from enum
     * @return {@link Result}<{@link ?}>
     */
    @GetMapping("/podcast/getPodcast")
    public Result<?> getPodcast(@RequestParam @NotNull ProviderEnum type) {
        return ReturnResultUtil.success(podcastService.getPodcast(type));
    }

    /**
     * Update accidental tech podcasts
     */
    @PostMapping("/podcast/updateAccidentalTechPodcasts")
    public void updateAccidentalTechPodcasts() throws FeedException, IOException {
        podcastService.updateAccidentalTechPodcasts();
    }
}
