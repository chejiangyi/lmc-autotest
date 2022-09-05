package com.lmc.autotest.provider.controller;
import com.free.bsf.core.util.WebUtils;
import com.lmc.business.api.web.response.ApiResponseEntity;
import com.lmc.business.api.web.response.IPage;
import com.lmc.autotest.api.model.request.CustomerAddReq;
import com.lmc.autotest.api.model.response.CustomerDetailsResp;
import com.lmc.autotest.api.provider.CustomerProvider;
import com.lmc.autotest.service.CustomerService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/")
public class CustomerController implements CustomerProvider {

	@Autowired(required = false)
	private CustomerService customerService;

	public ApiResponseEntity<IPage<CustomerDetailsResp>> getCustomer(Long customerId) {
			return null;
	}

	@GetMapping("/echo")
	@ApiOperation("禁用/启用")
	public ApiResponseEntity<Object> echo(String str) {
		return ApiResponseEntity.success();

	}

	@GetMapping("/test")
	public ApiResponseEntity<Object> test(String str) {
		return ApiResponseEntity.success(str);

	}

	@PostMapping("/test2")
	public ApiResponseEntity<Object> test2(String str) {
		return ApiResponseEntity.success(str);

	}

	@PostMapping("/test3")
	public ApiResponseEntity<Object> test3(MultipartFile file) {
		//CommonsMultipartResolver
		//ContentCachingRequestWrapper

		return ApiResponseEntity.success(file.getName());

	}
	@PostMapping("/test4")
	public void  test4(String str) {
		try {
			WebUtils.getResponse().setContentType("application/octet-stream;charset=UTF-8");
			WebUtils.getResponse().getOutputStream().write(str.getBytes());
			WebUtils.getResponse().getOutputStream().flush();
			WebUtils.getResponse().getOutputStream().close();
			return;
		}catch (Exception e){
		}
	}

	public ApiResponseEntity addCustomer(CustomerAddReq req) {
		return null;
	}

}