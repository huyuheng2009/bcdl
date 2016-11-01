/**
 * 项目: bcdl
 * 包名：com.yogapay.bcdl
 * 文件名: Test
 * 创建时间: 2014/7/30 16:42
 * 支付界科技有限公司版权所有，保留所有权利
 */
package com.yogapay.bcdl;

import com.yogapay.boss.utils.Md5;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Todo:
 * @Author: Zhanggc
 */
public class Test {
    public static void main(String[] args) throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sf.parse("2014-08-22 17:53:11");
        Calendar limit = Calendar.getInstance();
        limit.set(Calendar.HOUR_OF_DAY,date.getHours());
        limit.set(Calendar.MINUTE,date.getMinutes());
        limit.set(Calendar.SECOND, date.getSeconds());

        long limitTime = limit.getTimeInMillis();
        long systemTime = System.currentTimeMillis();
        System.out.println(limitTime > systemTime);
    }
}
