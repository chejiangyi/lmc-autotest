package com.lmc.autotest.dao.model.auto;


import java.io.Serializable;
import java.util.Date;

import lombok.*;

/**
 * tb_job 表自动实体映射,不要手工修改
 *
 * @author 车江毅
 * @since 2022-10-27 12:14:07
 * 自动生成:https://gitee.com/makejava/EasyCode/wikis/
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class tb_job_model implements Serializable {

    public Integer id;
    /**
     * corn表达式
     */
    public String corn;
    /**
     * 脚本
     */
    public String jscript;

    public Date create_time;

    public String create_user;

    public Integer create_user_id;
    /**
     * 运行状态:启用,停用
     */
    public String state;

    public String title;
    /**
     * 描述
     */
    public String remark;
}
