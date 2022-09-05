package com.lmc.autotest.api.provider;

import com.lmc.business.api.web.response.ApiResponseEntity;
import com.lmc.business.api.web.response.IPage;
import com.lmc.autotest.api.ApplicationInfo;
import com.lmc.autotest.api.model.request.CustomerAddReq;
import com.lmc.autotest.api.model.response.CustomerDetailsResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


/**
 * FeignClient命名规则说明:
 * name 为提供服务的${spring.application.name}名称,不能写错,否则使用api的客户端在注册中心无法找到对应的服务。
 * 		比如bmc-demo-provider 为bmc-demo项目provider模块提供的服务
 * path 为api的相对路径,格式:${server.servlet.context-path|/相对路径
 * 		比如/api/customer
 */
@Api("客户管理服务")
@FeignClient(name = ApplicationInfo.Project,path = ApplicationInfo.ContextPath)
public interface  CustomerProvider {

	@GetMapping(value="/customer/get")
	@ApiOperation("获取客户信息")
	ApiResponseEntity<IPage<CustomerDetailsResp>> getCustomer(@RequestParam("customerId") Long customerId);

	@PostMapping(value="/customer/add")
	@ApiOperation("新增客户(保存草稿)")
	ApiResponseEntity addCustomer(@RequestBody CustomerAddReq req);
}
