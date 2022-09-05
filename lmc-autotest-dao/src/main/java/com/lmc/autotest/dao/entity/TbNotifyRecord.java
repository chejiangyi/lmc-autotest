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
 * es 消息表
 * </p>
 *
 * @author chejiangyi<chejiangyi@bmc.com>
 * @since 2021-05-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TbNotifyRecord implements Serializable {

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
     * 消息唯一标识
     */
    private String messageId;

    /**
     * 批量任务名
     */
    private String taskName;

    /**
     * 消息模板key
     */
    private String templateKey;

    /**
     * 消息接收人
     */
    private String receiveUser;

    /**
     * 消息参数json
     */
    private String paramJson;

    /**
     * 重试次数
     */
    private Integer retryCount;

    /**
     * 重试间隔
     */
    private String retryTimespan;

    /**
     * 发送状态
     */
    private Integer sendState;

    /**
     * 接收状态
     */
    private Integer receiveState;

    /**
     * 创建人
     */
    private LocalDateTime createUser;

    /**
     * 更新人
     */
    private LocalDateTime updateUser;


}
