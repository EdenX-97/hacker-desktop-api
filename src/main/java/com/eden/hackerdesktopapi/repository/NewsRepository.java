package com.eden.hackerdesktopapi.repository;

import com.eden.hackerdesktopapi.model.News;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;


/**
 * Repository for news
 *
 * @author Mo Xu
 * @date 2022/09/02
 */
@EnableScan
public interface NewsRepository extends CrudRepository<News, String> {
}
