package com.elk.elkes.service;

import com.elk.elkes.entity.ProductEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import java.util.List;

/**
 * @Auther: Mr.Kong
 * @Date: 2020/5/8 15:09
 * @Description:
 */
public interface ProductSearchService {

    List<ProductEntity> search(String name, @PageableDefault(page = 0, value = 10) Pageable pageable);

}
