package com.eden.hackerdesktopapi.repository;

import com.eden.hackerdesktopapi.model.Podcast;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;


/**
 * Repository for podcasts
 *
 * @author Mo Xu
 * @date 2022/09/02
 */
@EnableScan
public interface PodcastRepository extends CrudRepository<Podcast, String> {
}
