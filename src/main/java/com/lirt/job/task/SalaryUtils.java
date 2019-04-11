package com.lirt.job.task;

/**
 * 解析工资的工具类
 */
public class SalaryUtils {

    public static Float[] getSalary(String salary) {
        Float[] floats = new Float[2];
        // 截取最后去最后一个字符
        String lastStr = salary.substring(salary.length() - 1);
        String dw = salary.substring(salary.length()-3).split("/")[0];
        String[] gz = salary.substring(0, salary.length()-3).split("-");
        String min = gz[0];
        String max = gz[1];
        if (lastStr.equals("月")){
            // 如果为 千/月
            if (dw.equals("千")) {
                floats[0] = Float.parseFloat(min)*1000;
                floats[1] = Float.parseFloat(max)*1000;
            } else if (dw.equals("万")) {
                // 如果为 万/月
                floats[0] = Float.parseFloat(min)*10000;
                floats[1] = Float.parseFloat(max)*10000;
            }
        } else if (lastStr.equals("年")) {
            if (dw.equals("千")) {
                // 千/年
                floats[0] = Float.parseFloat(min)*1000/12;
                floats[1] = Float.parseFloat(max)*1000/12;
            } else if (dw.equals("万")) {
                // 万/年
                floats[0] = Float.parseFloat(min)*10000/12;
                floats[1] = Float.parseFloat(max)*10000/12;
            }
        }


        return floats;
    }

}
