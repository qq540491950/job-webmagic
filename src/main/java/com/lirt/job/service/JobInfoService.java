package com.lirt.job.service;

import com.lirt.job.entity.JobInfo;

import java.util.List;

public interface JobInfoService {

    /**
     * 保存工作信息
     * @param jobInfo
     */
    public void save(JobInfo jobInfo);

    /**
     * 查询工作信息
     * @param jobInfo
     * @return
     */
    public List<JobInfo> findJobInfo(JobInfo jobInfo);
}
