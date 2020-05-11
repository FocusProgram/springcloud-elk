package com.elk.elkes.entity;

import lombok.Data;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;

/**
 * @Auther: Mr.Kong
 * @Date: 2020/5/8 15:05
 * @Description: indexName指定es对应索引名，type指定数据类型
 */
@Document(indexName = "product", type = "product")
@Data
public class ProductEntity {
    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 类型ID
     */
    private Integer categoryId;
    /**
     * 名称
     */
    private String name;
    /**
     * 小标题
     */
    private String subtitle;
    /**
     * 主图像
     */
    private String mainImage;
    /**
     * 小标题图像
     */
    private String subImages;
    /**
     * 描述
     */
    private String detail;
    /**
     * 商品规格
     */
    private String attributeList;
    /**
     * 价格
     */
    private Double price;
    /**
     * 库存
     */
    private Integer stock;
    /**
     * 状态
     */
    private Integer status;

    /**
     * 创建人
     */
    private String createdBy;
    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 更新时间
     */
    private MappingMetaData.Timestamp updatedTime;
}
