package com.elk.elkes.reposiory;

import com.elk.elkes.entity.ProductEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @Auther: Mr.Kong
 * @Date: 2020/5/8 15:04
 * @Description:
 */
public interface ProductReposiory extends ElasticsearchRepository<ProductEntity, Long> {
}
