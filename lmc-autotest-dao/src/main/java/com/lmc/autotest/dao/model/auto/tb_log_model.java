package com.lmc.autotest.dao.model.auto;


import java.io.Serializable;
import java.util.Date;

import lombok.*;

/**
 * tb_log 表自动实体映射,不要手工修改
 *
 * @author 车江毅
 * @since 2022-09-09 15:46:20
 * 自动生成:https://gitee.com/makejava/EasyCode/wikis/
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class tb_log_model implements Serializable {

    public Integer id;

    public String node;

    public String type;

    public Date create_time;

    public String message;
}
