package com.pc.util;

import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;
import java.util.regex.Pattern;

/**
 * Qutarz 触发器工具类,主要是构建指定参数的Trigger
 * Created by panrui on 2014/5/30.
 */
public class TriggerUtil {
    private static int count = 1;
    private TriggerBuilder triggerBuilder = null;
    private String[] parm = null;
    private Pattern pattern = Pattern.compile("([\\d,\\-\\*\\?/]+?\\s?)+");

    public TriggerUtil() {
    }

    /**
     * 构建CronTrigger类型触发器
     *
     * @param parm
     */
    public TriggerUtil(String... parm) {
        this.parm = parm;
        StringBuffer sb = new StringBuffer();

        if (parm.length == 1 && pattern.matcher(parm[0]).matches())
            triggerBuilder = TriggerBuilder.newTrigger().withIdentity("trigger" + count, "group" + count)
                    .withSchedule(CronScheduleBuilder.cronSchedule(parm[0]));
        else if (1 < parm.length && parm.length <= 6) {
            for (int i = 0; i < parm.length; i++) {
                if (!pattern.matcher(parm[i]).find())
                    parm[i] = "*";
                if (i == parm.length - 1)
                    sb = parm[3].trim().equals("?") ? sb.append(parm[i]) : sb.append("?");
                else
                    sb.append(parm[i] + " ");
            }
            triggerBuilder = TriggerBuilder.newTrigger().withIdentity("trigger" + count, "group" + count)
                    .withSchedule(CronScheduleBuilder.cronSchedule(sb.toString()));
        } else
            throw new IllegalArgumentException("构建CronScheduler时参数错误=>" + parm + ", 参数个数" + parm.length);
        count++;
    }

    /**
     * 构建SimpleTrigger类型触发器
     *
     * @param Interval 时间间隔
     * @param repeat   重复次数
     * @param dates    开始及结束日期 数组
     */
    public TriggerUtil(int Interval, int repeat, Date... dates) {
        triggerBuilder = TriggerBuilder.newTrigger().withIdentity("trigger" + count, "group" + count).startAt(dates[0]).endAt(dates[1])
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(Interval).withRepeatCount(repeat));
        count++;
    }

    public static void simpleTask(JobDataMap dataMap, Class<? extends Job> job, Date date) {
        SimpleTrigger simpleTrigger = (SimpleTrigger) TriggerBuilder.newTrigger().startAt(date).build();
        try {
            Scheduler scheduler = new StdSchedulerFactory().getScheduler();
            JobDetail jobDetail = JobBuilder.newJob(job).build();
            if (dataMap != null)
                jobDetail.getJobDataMap().putAll(dataMap);
            scheduler.scheduleJob(jobDetail, simpleTrigger);
            scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public String[] getParm() {
        return parm;
    }

    public void setParm(String... parm) {
        this.parm = parm;
    }

    /**
     * 取得相应的Trigger
     *
     * @param job
     * @return Trigger
     */
    public Trigger builderTrigger(JobDetail... job) {
        if (job.length > 0)
            return triggerBuilder.forJob(job[0]).build();
        return triggerBuilder.build();

    }


}
