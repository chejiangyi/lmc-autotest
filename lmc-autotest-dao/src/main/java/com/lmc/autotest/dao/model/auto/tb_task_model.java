package com.lmc.autotest.dao.model.auto;


import java.io.Serializable;
import java.util.Date;

import lombok.*;

/**
 * tb_task 表自动实体映射,不要手工修改
 *
 * @author 车江毅
 * @since 2022-09-13 19:01:15
 * 自动生成:https://gitee.com/makejava/EasyCode/wikis/
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class tb_task_model implements Serializable {

    public Integer id;
    /**
     * 任务
     */
    public String task;
    /**
     * 存储引擎
     */
    public String filter_store;
    /**
     * 表达式
     */
    public String corn;
    /**
     * 下次执行时间
     */
    public Date next_time;
    /**
     * 使用状态:使用,禁用
     */
    public String use_state;
    /**
     * 创建人
     */
    public String create_user;
    /**
     * 创建时间
     */
    public Date create_time;
    /**
     * 更新时间
     */
    public Date update_time;
    /**
     * 更新人
     */
    public String update_user;
    /**
     * 执行结果
     */
    public String exec_result;
    /**
     * 过滤筛选脚本
     */
    public String filter_script;
    /**
     * 过滤筛选表
     */
    public String filter_table;
    /**
     * 是否清理数据
     */
    public Boolean clear_data_first;
    /**
     * 执行节点
     */
    public String nodes;
    /**
     * 运行线程数
     */
    public Integer run_threads_count;
    /**
     * 运行前脚本
     */
    public String http_begin_script;
    /**
     * 运行后脚本
     */
    public String http_end_script;
    /**
     * 检测终止脚本
     */
    public String check_stop_script;
    /**
     * 运行时间
     */
    public Date run_heart_time;
}
