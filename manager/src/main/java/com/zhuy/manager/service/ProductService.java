package com.zhuy.manager.service;

import com.zhuy.entity.Product;
import com.zhuy.entity.enums.ProductStatus;
import com.zhuy.manager.error.ErrorEnum;
import com.zhuy.manager.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductService {

    private Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository repository;

    public Product addProduct(Product product) {
        logger.debug("创建产品,参数：{}", product);
        checkProduct(product);
        setDefault(product);
        Product result = repository.save(product);
        logger.debug("创建产品,结果：{}", result);
        return result;
    }

    private void setDefault(Product product) {
        if (product.getCreateAt() == null) {
            product.setCreateAt(new Date());
        }
        if (product.getUpdateAt() == null) {
            product.setUpdateAt(new Date());
        }
        if (product.getStepAmount() == null) {
            product.setStepAmount(BigDecimal.ZERO);
        }
        if (product.getLockTerm() == null) {
            product.setLockTerm(0);
        }
        if (product.getStatus() == null) {
            product.setStatus(ProductStatus.AUDITING.name());
        }

    }

    /**
     * 产品数据校验
     * 1、非空数据
     * 2、收益率要0-30
     * 3、投资步长需要为整数
     *
     * @param product
     */
    private void checkProduct(Product product) {
        Assert.notNull(product.getId(), ErrorEnum.ID_NOT_NULL.getCode());


        Assert.isTrue(BigDecimal.ZERO.compareTo(product.getRewardRate()) < 0 && BigDecimal.valueOf(30).compareTo(product.getRewardRate()) >= 0, "收益率范围错误。");

        Assert.isTrue(BigDecimal.valueOf(product.getStepAmount().longValue()).compareTo(product.getStepAmount()) == 0, "投资步长应为整数。");

    }

    /**
     * 查询单个产品信息
     *
     * @param id
     * @return
     */
    public Product findOne(String id) {
        Assert.notNull(id, "编号不能为空。");
        logger.debug("查询单个产品信息,id={}", id);
        Product prduct = repository.findOne(id);
        logger.debug("查询单个产品信息,结果={}", prduct);
        return prduct;
    }


    public Page<Product> queryAll(List<String> idList, BigDecimal minRewardRate, BigDecimal maxRewardRate, List<String> statusList, Pageable pageable) {

        logger.debug("查询产品参数，idList={}，minRewardRate={}，maxRewardRate={}，statusList={}，pageable={}", idList, minRewardRate, maxRewardRate, statusList, pageable);
        Specification<Product> specification = new Specification<Product>() {
            @Override
            public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Expression<String> idCol = root.get("id");
                Expression<BigDecimal> rewardRateCol = root.get("rewardRate");
                Expression<String> statusCol = root.get("status");
                List<Predicate> predicates = new ArrayList<>();
                if (idList != null && idList.size() > 0) {
                    predicates.add(idCol.in(idList));
                }
                if (BigDecimal.ZERO.compareTo(minRewardRate) < 0) {
                    predicates.add(cb.ge(rewardRateCol, minRewardRate));
                }
                if (BigDecimal.ZERO.compareTo(maxRewardRate) < 0) {
                    predicates.add(cb.le(rewardRateCol, maxRewardRate));
                }
                if (statusList != null && statusList.size() > 0) {
                    predicates.add(statusCol.in(statusList));
                }
                query.where(predicates.toArray(new Predicate[0]));

                return null;
            }
        };
        Page<Product> products = repository.findAll(specification, pageable);
        logger.debug("查询结果，结果={}", products);
        return products;

    }


}
