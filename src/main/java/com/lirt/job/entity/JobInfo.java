package com.lirt.job.entity;

import lombok.Data;

@Data
public class JobInfo {
    private Long id;
    private String companyName;
    private String companyAddr;
    private String companyInfo;
    private String jobName;
    private String jobAddr;
    private String jobInfo;
    private Float salaryMin;
    private Float salaryMax;
    private String url;
    private String time;
}
