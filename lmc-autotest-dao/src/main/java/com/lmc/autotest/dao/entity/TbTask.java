package com.lmc.autotest.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 批量消息任务
 * </p>
 *
 * @author chejiangyi<chejiangyi@bmc.com>
 * @since 2021-05-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TbTask implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否删除：0-不删除，1-删除
     */
    private Integer isDeleted;

    /**
     * 备注
     */
    private String remark;

    /**
     * 任务名
     */
    private String name;

    /**
     * 任务描述
     */
    private String taskDesc;

    /**
     * 模板key
     */
    private String templateKey;

    /**
     * 参数集合json
     */
    private String paramJson;

    /**
     * 触发时间
     */
    private LocalDateTime tiggerTime;

    /**
     * 执行时间
     */
    private LocalDateTime execTime;

    /**
     * 状态  1：未执行 2：执行中  3：已执行
     */
    private Integer state;

    /**
     * 批量接收的用户
     */
    private String receiveUser;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 更新人
     */
    private String updateUser;


}
