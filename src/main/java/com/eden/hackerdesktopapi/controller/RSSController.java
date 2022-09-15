package com.eden.hackerdesktopapi.controller;

import com.eden.hackerdesktopapi.constant.consists.Result;
import com.eden.hackerdesktopapi.service.intf.RSSService;
import com.eden.hackerdesktopapi.utils.ReturnResultUtil;
import com.rometools.rome.io.FeedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.io.IOException;


/**
 * Controller for RSS
 *
 * @author Mo Xu
 * @date 2022/09/12
 */
@RestController
@Validated
public class RSSController {
    private final RSSService rssService;

    @Autowired
    public RSSController(RSSService rssService) {
        this.rssService = rssService;
    }

    /**
     * Subscribe RSS
     *
     * @param url RSS url
     * @return {@link Result}<{@link ?}>
     */
    @PostMapping("/rss/subscribeRSS")
    public Result<?> subscribeRSS(@RequestParam @NotNull String url) throws FeedException, IOException {
        rssService.subscribeRSS(url);
        return ReturnResultUtil.success("Subscribe RSS success");
    }
}
