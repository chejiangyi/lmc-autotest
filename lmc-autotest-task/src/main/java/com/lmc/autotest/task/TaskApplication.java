package com.lmc.autotest.task;

import com.lmc.autotest.task.base.provider.NodeProvider;
import lombok.val;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Date;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = "com.lmc.autotest")

public class TaskApplication {
	public static void main(String[] args) {
		SpringApplication.run(TaskApplication.class, args);
		new NodeProvider().create().heartbeat();
	}
}
