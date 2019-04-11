package com.lirt.job.task;

import com.lirt.job.entity.JobInfo;
import com.lirt.job.service.JobInfoService;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import javax.annotation.Resource;

@Component
public class MybatisPipeline implements Pipeline {

    @Resource
    private JobInfoService jobInfoService;

    @Override
    public void process(ResultItems resultItems, Task task) {
        // 获取数据
        JobInfo jobInfo = resultItems.get("jobInfo");
        if (jobInfo != null) {
            this.jobInfoService.save(jobInfo);
        }
    }
}
