package com.lmc.autotest.api.model.response;

import com.lmc.autotest.api.model.request.CustomerAddReq;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CustomerDetailsResp extends CustomerAddReq {

    @ApiModelProperty("客户地址省名称")
    private String regionProvinceName;

    @ApiModelProperty("客户类型")
    private String type;
}
