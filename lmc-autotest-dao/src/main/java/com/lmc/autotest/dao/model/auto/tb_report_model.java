package com.lmc.autotest.dao.model.auto;


import java.io.Serializable;
import java.util.Date;

import lombok.*;

/**
 * tb_report 表自动实体映射,不要手工修改
 *
 * @author 车江毅
 * @since 2022-09-07 20:31:51
 * 自动生成:https://gitee.com/makejava/EasyCode/wikis/
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class tb_report_model implements Serializable {

    public Object id;

    public String report_name;

    public Integer task_id;

    public String nodes;
    /**
     * 节点配置信息
     */
    public String nodes_info;

    public String filter_table;

    public String filter_store;
    /**
     * 开始时间
     */
    public Date begin_time;
    /**
     * 结束时间
     */
    public Date end_time;

    public Date create_time;
    /**
     * report_node 表
     */
    public String report_node_table;
    /**
     * report_url表
     */
    public String report_url_table;
}
