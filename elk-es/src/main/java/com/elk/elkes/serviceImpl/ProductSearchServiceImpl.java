package com.elk.elkes.serviceImpl;

import com.elk.elkes.entity.ProductEntity;
import com.elk.elkes.reposiory.ProductReposiory;
import com.elk.elkes.service.ProductSearchService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: Mr.Kong
 * @Date: 2020/5/8 15:09
 * @Description:
 */
@Service
public class ProductSearchServiceImpl implements ProductSearchService {

    @Autowired
    private ProductReposiory productReposiory;

    @Override
    public List<ProductEntity> search(String name, @PageableDefault(page = 0, value = 10) Pageable pageable) {
        // int i = 1 / 0;
//        String user = null;
//        System.out.println(user.getBytes());
        // 1.拼接查询条件
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        // 2.模糊查询 name、 subtitle、detail含有 搜索关键字
        builder.must(QueryBuilders.multiMatchQuery(name, "name", "subtitle", "detail"));
        // 3.调用ES接口查询
        Page<ProductEntity> page = productReposiory.search(builder, pageable);
        // 4.获取集合数据
        List<ProductEntity> content = page.getContent();
        return content;
    }

}
