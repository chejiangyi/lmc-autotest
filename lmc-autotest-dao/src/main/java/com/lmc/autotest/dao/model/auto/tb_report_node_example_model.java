package com.lmc.autotest.dao.model.auto;


import java.io.Serializable;
import java.util.Date;

import lombok.*;

/**
 * tb_report_node_example 表自动实体映射,不要手工修改
 *
 * @author 车江毅
 * @since 2022-09-22 14:29:27
 * 自动生成:https://gitee.com/makejava/EasyCode/wikis/
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class tb_report_node_example_model implements Serializable {

    public Integer id;

    public String node;

    public Double cpu;

    public Double memory;

    public Double network_read;

    public Double network_write;

    public Integer active_threads;

    public Double throughput;

    public Double error;

    public Date create_time;
}
