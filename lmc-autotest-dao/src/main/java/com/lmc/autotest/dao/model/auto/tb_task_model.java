package com.lmc.autotest.dao.model.auto;


import java.io.Serializable;
import java.util.Date;

import lombok.*;

/**
 * tb_task 表自动实体映射,不要手工修改
 *
 * @author 车江毅
 * @since 2022-10-21 11:42:26
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
     * 运行时间
     */
    public Date run_heart_time;
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
     * 过滤筛选样本脚本
     */
    public String filter_script;
    /**
     * 第一次执行过滤错误脚本
     */
    public String first_filter_error_script;
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
     * 每个线程启动时的间隔时间
     */
    public Integer sleep_time_every_thread;
    /**
     * 节点数量
     */
    public Integer node_count;
    /**
     * 运行节点
     */
    public String run_nodes;
    /**
     * 是否使用http keepalive
     */
    public Boolean use_http_keepalive;
    /**
     * 创建用户id
     */
    public Integer create_user_id;
}
