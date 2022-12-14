package com.lmc.autotest.task;
import com.free.bsf.core.util.ExceptionUtils;
import com.free.bsf.core.util.JsonUtils;
import com.lmc.autotest.core.ApiResponseEntity;
import com.lmc.autotest.task.base.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.DateUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/")
public class ApiController  {


	@PostMapping("/opentask/")
	public ApiResponseEntity<Integer> openTask(Integer taskId,String tranId,Integer index,Integer userid,String params) {
		try {
			if(tranId==null)
			{
				tranId= DateUtils.formatDate(new Date(),"yyyy_MM_dd_HH_mm_ss");;
			}
			if(index==null){
				index=0;
			}
			NodeManager.Default.open(taskId,tranId,index,userid, JsonUtils.deserialize(params,new HashMap<String,Object>().getClass()));
			return ApiResponseEntity.success(1);
		}catch (Exception e){
			return ApiResponseEntity.fail(ExceptionUtils.getDetailMessage(e));
		}
	}

	@PostMapping("/closetask/")
	public ApiResponseEntity<Integer> closeTask(Integer taskId) {
		try {
			NodeManager.Default.close(taskId,"用户执行关闭",false);
			return ApiResponseEntity.success(taskId);
		}catch (Exception e){
			return ApiResponseEntity.fail(ExceptionUtils.getDetailMessage(e));
		}
	}

	@RequestMapping("/test/")
	public ApiResponseEntity<Integer> test() {
		try {
			return ApiResponseEntity.success(1);
		}catch (Exception e){
			return ApiResponseEntity.fail(ExceptionUtils.getDetailMessage(e));
		}
	}

	@RequestMapping("/run/")
	public ApiResponseEntity<Integer> run() {
		try {
			return ApiResponseEntity.success(NodeManager.Default.getAutoTestProvider()==null?0:NodeManager.Default.getAutoTestProvider().getTaskid());
		}catch (Exception e){
			return ApiResponseEntity.fail(ExceptionUtils.getDetailMessage(e));
		}
	}

	@RequestMapping("/health/")
	public ApiResponseEntity<String> health() {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("\r\n最大内存:"+ IOUtils.maxMemory());
			sb.append("\r\n空余内存:"+ IOUtils.freeMemory());
			sb.append("\r\n使用内存:"+ IOUtils.totalMemory());
			return ApiResponseEntity.success(sb.toString());
		}catch (Exception e){
			return ApiResponseEntity.fail(ExceptionUtils.getDetailMessage(e));
		}
	}
}