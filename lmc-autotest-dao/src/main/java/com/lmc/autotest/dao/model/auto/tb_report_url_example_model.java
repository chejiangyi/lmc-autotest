package com.lmc.autotest.dao.model.auto;


import java.io.Serializable;
import java.util.Date;

import lombok.*;

/**
 * tb_report_url_example 表自动实体映射,不要手工修改
 *
 * @author 车江毅
 * @since 2022-09-08 22:44:00
 * 自动生成:https://gitee.com/makejava/EasyCode/wikis/
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class tb_report_url_example_model implements Serializable {

    public Integer id;

    public String url;

    public String node;
    /**
     * 访问次数
     */
    public Double visit_num;
    /**
     * 吞吐量/s
     */
    public Double throughput;
    /**
     * 错误量/s
     */
    public Double error;
    /**
     * avg 访问耗时/s
     */
    public Double visit_time;
    /**
     * 网络读/s
     */
    public Double network_read;

    public Date create_time;
    /**
     * 网络写/s
     */
    public Double network_write;
}
