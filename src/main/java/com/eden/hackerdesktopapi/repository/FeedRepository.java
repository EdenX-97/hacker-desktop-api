package com.eden.hackerdesktopapi.repository;

import com.eden.hackerdesktopapi.model.Feed;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;


/**
 * Repository for feed
 *
 * @author Mo Xu
 * @date 2022/09/01
 */
@EnableScan
public interface FeedRepository extends CrudRepository<Feed, String> {
    Feed findFeedByTitle(String title);

    Boolean existsFeedByTitle(String title);
}
