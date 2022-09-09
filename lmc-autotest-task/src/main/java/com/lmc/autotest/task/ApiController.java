package com.lmc.autotest.task;
import com.lmc.autotest.core.ApiResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/")
public class ApiController  {


	@PostMapping("/opentask/")
	public ApiResponseEntity<Integer> openTask(Integer taskId) {
		try {
			AutoTestManager.Default.open(taskId);
			return ApiResponseEntity.success(1);
		}catch (Exception e){
			return ApiResponseEntity.fail(e.getMessage());
		}
	}

	@PostMapping("/closetask/")
	public ApiResponseEntity<Integer> closeTask(Integer taskId) {
		try {
			AutoTestManager.Default.close(taskId,"用户执行关闭");
			return ApiResponseEntity.success(1);
		}catch (Exception e){
			return ApiResponseEntity.fail(e.getMessage());
		}
	}

}