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
 * 消息模板
 * </p>
 *
 * @author chejiangyi<chejiangyi@bmc.com>
 * @since 2021-05-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TbTemplate implements Serializable {

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
     * 模板key
     */
    private String templateKey;

    /**
     * 模板描述
     */
    private String templateDesc;

    /**
     * 标题
     */
    private String subject;

    /**
     * 内容
     */
    private String content;

    /**
     * 隐藏内容
     */
    private String hide;

    /**
     * 账户1
     */
    private Long accountId1;

    /**
     * 账户2
     */
    private Long accountId2;

    /**
     * 账户3
     */
    private Long accountId3;

    /**
     * 重试间隔
     */
    private String retryTimespan;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 更新人
     */
    private String updateUser;

    /**
     * 模板在线状态  1：在线  2：不在线
     */
    private Integer state;

    /**
     * 发送超时时间，超时后将丢失
     */
    private Long effectiveTime;

    /**
     * 旧消息中心模板key支持
     */
    private String oldTemplateKey;


}
