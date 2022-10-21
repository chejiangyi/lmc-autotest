package com.lmc.autotest.dao.model.auto;


import java.io.Serializable;
import java.util.Date;

import lombok.*;

/**
 * tb_user 表自动实体映射,不要手工修改
 *
 * @author 车江毅
 * @since 2022-10-21 13:49:23
 * 自动生成:https://gitee.com/makejava/EasyCode/wikis/
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class tb_user_model implements Serializable {

    public Integer id;

    public String name;

    public String pwd;

    public Date create_time;
    /**
     * 0:普通用户 1:管理员
     */
    public Integer role;
    /**
     * 限制节点数
     */
    public Integer limit_node_count;
}
