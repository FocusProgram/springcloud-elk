package com.elk.elkes.entity;

import lombok.Data;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * @Auther: Mr.Kong
 * @Date: 2020/5/9 14:53
 * @Description:
 */
@Document(indexName = "user", type = "user")
@Data
public class User {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 更新时间
     */
    private MappingMetaData.Timestamp updateTime;
}
