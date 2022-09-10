package com.lmc.autotest.dao.model.auto;


import java.io.Serializable;
import java.util.Date;

import lombok.*;

/**
 * tb_sample_example 表自动实体映射,不要手工修改
 *
 * @author 车江毅
 * @since 2022-09-09 15:46:21
 * 自动生成:https://gitee.com/makejava/EasyCode/wikis/
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class tb_sample_example_model implements Serializable {

    public Long id;

    public String url;

    public String app_name;

    public String header;

    public String body;

    public Date create_time;

    public String fromip;

    public String traceId;

    public String method;
}
