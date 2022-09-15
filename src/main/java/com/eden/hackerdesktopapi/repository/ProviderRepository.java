package com.eden.hackerdesktopapi.repository;

import com.eden.hackerdesktopapi.constant.enums.ProviderEnum;
import com.eden.hackerdesktopapi.model.Provider;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;


/**
 * Repository of provider
 *
 * @author Mo Xu
 * @date 2022/09/02
 */
@EnableScan
public interface ProviderRepository extends CrudRepository<Provider, String> {
    Boolean existsProviderByName(String name);

    Provider findProviderByName(String name);
}
