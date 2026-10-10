package com.lcyl.framework.config;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;

/**
 * @ClassName QuartzConfig
 * @Description TODO
 * @Author GuiGui
 * @Date 2026-03-17 19:31
 * @Version 1.0
 */
@Configuration
public class QuartzConfig {

    @Autowired
    private DataSource dataSource; // 注入 Spring 配置的数据源
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        // 设置 Quartz 使用 Spring 的数据源
        factory.setDataSource(dataSource);
        // 其他配置：如是否覆盖已存在的任务、启动延迟等
        factory.setOverwriteExistingJobs(true);
        factory.setStartupDelay(10); // 启动延迟 10 秒
        return factory;
    }
    // 暴露 Scheduler 供动态操作任务
    @Bean
    public Scheduler scheduler() throws Exception {
        return schedulerFactoryBean().getScheduler();
    }
}
