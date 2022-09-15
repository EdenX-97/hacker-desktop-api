package com.eden.hackerdesktopapi.controller;


import com.eden.hackerdesktopapi.constant.consists.Result;
import com.eden.hackerdesktopapi.constant.enums.ProviderEnum;
import com.eden.hackerdesktopapi.service.intf.NewsService;
import com.eden.hackerdesktopapi.utils.ReturnResultUtil;
import com.rometools.rome.io.FeedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.io.IOException;

/**
 * Controller for news
 *
 * @author Mo Xu
 * @date 2022/09/02
 */
@RestController
@Validated
public class NewsController {
    private final NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    /**
     * Get this week's news by type
     *
     * @param type Provider type
     * @return {@link Result}<{@link ?}>
     */
    @GetMapping("/news/getWeeklyNews")
    public Result<?> getWeeklyNews(@RequestParam @NotNull ProviderEnum type) {
        return ReturnResultUtil.success(newsService.getWeeklyNews(type));
    }

    /**
     * Get all weekly news
     *
     * @return {@link Result}<{@link ?}>
     */
    @GetMapping("/news/getAllWeeklyNews")
    public Result<?> getAllWeeklyNews() {
        try {
            newsService.getAllWeeklyNews();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ReturnResultUtil.success(newsService.getAllWeeklyNews());
    }

    /**
     * Update hacker weekly news
     */
    @PostMapping("/news/updateHackerNewsWeekly")
    @Scheduled(cron = "${schedule.updateEveryFriday}")
    public void updateHackerNewsWeekly() throws FeedException, IOException {
        newsService.updateHackerNewsWeekly();
    }

    /**
     * Update overflow weekly news
     */
    @PostMapping("/news/updateOverflowNewsWeekly")
    @Scheduled(cron = "${schedule.updateEveryFriday}")
    public void updateOverflowNewsWeekly() throws FeedException, IOException {
        newsService.updateOverflowNewsWeekly();
    }

    /**
     * Update infoQ weekly news
     */
    @PostMapping("/news/updateInfoQNewsWeekly")
    @Scheduled(cron = "${schedule.updateEveryFriday}")
    public void updateInfoQNewsWeekly() throws FeedException, IOException {
        newsService.updateInfoQNewsWeekly();
    }

    /**
     *  Update all news weekly
     */
    @PostMapping("/news/updateAllNews")
    public void updateAllNews() throws FeedException, IOException {
        newsService.updateAllNews();
    }
}
