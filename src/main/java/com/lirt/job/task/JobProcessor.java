package com.lirt.job.task;

import com.lirt.job.entity.JobInfo;
import org.jsoup.Jsoup;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import javax.annotation.Resource;
import java.util.List;

@Component
public class JobProcessor implements PageProcessor {

    private String url = "https://search.51job.com/list/200200,000000,0000,00,9,99,java,2," +
            "2.html?lang=c&stype=1&postchannel=0000&workyear=99&cotype=99&degreefrom=99&jobterm=99&companysize=99&lonlat=0%2C0&radius=-1&ord_field=0&confirmdate=9&fromType=&dibiaoid=0&address=&line=&specialarea=00&from=&welfare=";

    @Override
    public void process(Page page) {
        List<Selectable> list = page.getHtml().css("div#resultList div.el").nodes();
        // 判断列表是否为空
        if (list.size() == 0) {
            // 如果为空，表示为招聘详情页，解析页面，获取招聘详情，保存数据
            this.saveJobInfo(page);
        } else {
            // 若果不为空，为列表页，解析详情页的url地址，放到任务队列中
            for (Selectable selectable : list) {
                String jobInfoUrl = selectable.links().toString();
                page.addTargetRequest(jobInfoUrl);
            }

            // 获取下一页url
            String nextUrl = page.getHtml().css("div.p_in li.bk").nodes().get(1).links().toString();
            page.addTargetRequest(nextUrl);
        }
    }


    private Site site = Site.me()
            .setCharset("gbk")
            .setTimeOut(10*1000)
            .setRetrySleepTime(3000)
            .setRetryTimes(3)
            .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36");

    @Override
    public Site getSite() {
        return site;
    }

    @Resource
    private MybatisPipeline mybatisPipeline;

    @Scheduled(initialDelay = 1000, fixedDelay = 10 * 1000)
    public void process() {
        Spider.create(new JobProcessor())
                .addUrl(url)
                .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(100000)))
                .thread(10)
                .addPipeline(this.mybatisPipeline)
                .run();
    }

    // 解析页面，获取招聘详情，保存数据
    private void saveJobInfo(Page page) {
        // 创建招聘信息
        JobInfo jobInfo = new JobInfo();

        Html html = page.getHtml();

        jobInfo.setCompanyName(html.css("div.cn p.cname a", "text").toString());
        jobInfo.setCompanyAddr(Jsoup.parse(html.css("div.bmsg").nodes().get(1).toString()).text());
        jobInfo.setCompanyInfo(html.css("div.tmsg", "text").toString());
        jobInfo.setJobName(html.css("div.cn h1", "text").toString());
        String jobAddr = html.css("div.cn p.ltype", "title").toString();
        jobInfo.setJobAddr(jobAddr.split("\\|")[0]);
        jobInfo.setJobInfo(Jsoup.parse(html.css("div.job_msg").toString()).text());
        Float[] salary = SalaryUtils.getSalary(html.css("div.cn strong", "text").toString());
        jobInfo.setSalaryMin(salary[0]);
        jobInfo.setSalaryMax(salary[1]);
        jobInfo.setUrl(page.getUrl().toString());
        String time = html.css("div.cn p.ltype", "text").regex("[0-9]{2}-[0-9]{2}").toString();
        jobInfo.setTime(time);
        //保存结果
        page.putField("jobInfo", jobInfo);
    }
}
