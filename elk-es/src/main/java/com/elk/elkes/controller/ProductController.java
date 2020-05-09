package com.elk.elkes.controller;

import com.elk.elkes.entity.ProductEntity;
import com.elk.elkes.service.ProductSearchService;
import com.qywk.cache.annotation.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @Auther: Mr.Kong
 * @Date: 2020/5/8 16:55
 * @Description:
 */


@RestController
@RequestMapping("elk")
public class ProductController {

    @Autowired
    private ProductSearchService productSearchService;

//    @MonitorRequest
    @PostMapping("search")
    @Cache(key = "usr")
    public List<ProductEntity> search(@RequestParam("name") String name) {
        Pageable pageable = PageRequest.of(0, 10);
        return productSearchService.search(name, pageable);
    }
}
