package com.eden.hackerdesktopapi.service.intf;


import com.rometools.rome.io.FeedException;

import java.io.IOException;


/**
 * RSS service interfaces
 *
 * @author Mo Xu
 * @date 2022/09/12
 */
public interface RSSService {
    /**
     * Subscribe RSS
     *
     * @param url RSS url
     */
    void subscribeRSS(String url) throws IOException, FeedException;
}
