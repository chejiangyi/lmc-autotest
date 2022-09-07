package com.lmc.autotest.dao.model.auto;


import java.io.Serializable;
import java.util.Date;

import lombok.*;

/**
 * tb_node 表自动实体映射,不要手工修改
 *
 * @author 车江毅
 * @since 2022-09-07 20:31:51
 * 自动生成:https://gitee.com/makejava/EasyCode/wikis/
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class tb_node_model implements Serializable {

    public Integer id;

    public String node;

    public Double cpu;

    public Double memery;

    public Integer threads;

    public String ip;

    public Date heatbeat_time;

    public String prot;
}
