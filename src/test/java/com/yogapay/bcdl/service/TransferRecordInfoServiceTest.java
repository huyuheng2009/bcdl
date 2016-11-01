/**
 * 项目: bcdl
 * 包名：com.yogapay.bcdl.service
 * 文件名: TransferRecordInfoService
 * 创建时间: 2014/8/21 10:28
 * 支付界科技有限公司版权所有，保留所有权利
 */
package com.yogapay.bcdl.service;

import com.yogapay.bcdl.domain.TransferRecordInfo;
import com.yogapay.bcdl.utils.junit.AbstractTest;
import org.junit.Test;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.List;

/**
 * @Todo:
 * @Author: Zhanggc
 */
public class TransferRecordInfoServiceTest extends AbstractTest {

    @Resource
    TransferRecordInfoService transferRecordInfoService;

    public void findTransferRecordInfoByTransferRecordId() throws SQLException{
        List<TransferRecordInfo> recordInfoList = transferRecordInfoService.findTransferRecordInfoByTransferRecordId(149l);
        System.out.print(recordInfoList);
    }
}
