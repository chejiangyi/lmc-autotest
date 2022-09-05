package com.lmc.autotest.service;

import java.time.LocalDateTime;

import com.lmc.autotest.dao.entity.TbCustomer;
import com.lmc.autotest.dao.mapper.TbCustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerService {

	@Autowired(required = false)
	private TbCustomerMapper customerMapper;

	/**
	 * 获取客户详细信息
	 * 
	 * @param id
	 * @return
	 */
	public TbCustomer getCustomer(Long id) {
		TbCustomer customer = null;
		customer = customerMapper.selectById(id);
		customer.setUpdateTime(LocalDateTime.now());

		return customer;
	}

	/**
	 * 修改客户信息
	 * 
	 * @param customer
	 */
	public void modify(TbCustomer customer) {
		customerMapper.updateById(customer);
	}



}
