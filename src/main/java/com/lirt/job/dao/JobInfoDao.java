package com.lirt.job.dao;

import com.lirt.job.entity.JobInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface  JobInfoDao {

    public void save(JobInfo jobInfo);

    public List<JobInfo> findJobInfo(JobInfo jobInfo);
}
