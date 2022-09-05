package com.lmc.autotest.api.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;



@Data
@ToString
public class CustomerAddReq {

    @ApiModelProperty("客户ID")
    private Long id;
    @ApiModelProperty("客户名称")
    private String customerName; 
}
