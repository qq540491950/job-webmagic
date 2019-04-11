package com.lirt.job.service.impl;

import com.lirt.job.dao.JobInfoDao;
import com.lirt.job.entity.JobInfo;
import com.lirt.job.service.JobInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class JobInfoServiceImpl implements JobInfoService {

    @Resource
    private JobInfoDao jobInfoDao;

    @Override
    public void save(JobInfo jobInfo) {
        List<JobInfo> list = jobInfoDao.findJobInfo(jobInfo);
        if (list.size()==0) {
            jobInfoDao.save(jobInfo);
        }

    }

    @Override
    public List<JobInfo> findJobInfo(JobInfo jobInfo) {
        return jobInfoDao.findJobInfo(jobInfo);
    }
}
