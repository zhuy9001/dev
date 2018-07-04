package com.zhuy.manager.controller;

import com.zhuy.entity.Product;
import com.zhuy.manager.service.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService service;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public Product addProduct(@RequestBody Product product) {
        logger.info("创建产品，参数：{}", product);
        Product result = service.addProduct(product);
        logger.info("创建产品，结果：{}", product);
        return result;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Product findOne(@PathVariable String id) {
        logger.info("查询单个产品信息，id={}", id);
        Product product = service.findOne(id);
        logger.info("查询单个产品信息，结果={}", id);
        return product;
    }

    public Page<Product> queryAll(String ids, BigDecimal minRewardRate, BigDecimal maxRewardRate, String status,
                                  @RequestParam(defaultValue = "0") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {
        logger.info("查询产品信息，ids={}，minRewardRate={}，maxRewardRate={}，status={}，pageNum={}，pageSize={}");
        List<String> idList = null, statusList = null;
        if (StringUtils.isNoneBlank(ids)) {
            idList = Arrays.asList(ids.split("."));
        }
        if (StringUtils.isNoneBlank(status)) {
            statusList = Arrays.asList(status.split("."));
        }
        Pageable pageable = new PageRequest(pageNum, pageSize);
        Page<Product> pages = service.queryAll(idList, minRewardRate, maxRewardRate, statusList, pageable);
        logger.info("查询产品信息，结果={}", pages);
        return pages;
    }
}
