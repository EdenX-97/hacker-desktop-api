package com.eden.hackerdesktopapi.utils;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;


/**
 * Util for RSS
 *
 * @author Mo Xu
 * @date 2022/09/01
 */
@Component
public class RSSUtil {
    public static SyndFeed getFeedFromRSSUrl(String url)
            throws IOException, FeedException {
        XmlReader reader = new XmlReader(new URL(url));
        return new SyndFeedInput().build(reader);
    }
}
