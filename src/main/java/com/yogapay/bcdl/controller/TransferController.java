package com.yogapay.bcdl.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.yogapay.bcdl.op.MinSheng;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSON;
import com.yogapay.bcdl.op.PingAn;

@Controller
@RequestMapping(value = "/transfer")
public class TransferController extends BaseController {

	private static final Logger log = LoggerFactory
			.getLogger(TransferController.class);

	@Resource
	private PingAn pingAn;

    @Resource
    private MinSheng minSheng;

	@RequestMapping(value = "transferOne")
	public void transferOne(@RequestParam Map<String, Object> params,
			HttpServletResponse response) {
		log.info("-------------------transferOne----------------------");
		log.info("params======" + params);
		String res = null;
		try {
			if ("pingAn".equals(params.get("bankCode"))) {
				String result = pingAn.transferOne(params,true);
				res = pingAn.handleTransfer(result);
			}else if("minSheng".equals(params.get("bankCode"))){
                params.put("bankCode",params.get("_bankCode")); //解决bankCode字段冲突
                params.remove("_bankCode");
                if("0".equals(params.get("unionFlag"))){
                    String result = minSheng.transferOneInterBank(params);
                    res = minSheng.handleTransferOne(result);
                }else if("1".equals(params.get("unionFlag"))){
                    String result = minSheng.transferOneInnerBank(params);
                    res = minSheng.handleTransferOne(result);
                }else{
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("status", "fail");
                    map.put("info", "缺少参数 跨行标志unionFlag！");
                    log.info( "缺少参数 跨行标志unionFlag！");
                    res = JSON.toJSONString(map);
                }
            }
		} catch (Exception e) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("status", "fail");
			res = JSON.toJSONString(map);
			e.printStackTrace();
		} finally {
			log.info("response=" + res);
			outJson(res, response);
		}
	}

	@RequestMapping(value = "transferBigBatch")
	public void transferBigBath(@RequestParam Map<String, Object> params,
			HttpServletResponse response) {
		log.info("-------------------transferBigBatch----------------------");
		log.info("params======" + params);
		String res = null;
		try {
			if ("pingAn".equals(params.get("bankCode"))) {
				String result = pingAn.transferBigBatch(params,true);
				res = pingAn.handleTransfer(result);
			}else if("minSheng".equals(params.get("bankCode"))){
                if("0".equals(params.get("unionFlag"))){
                    String result = minSheng.transferBatchInterBank(params);
                    res = minSheng.handleTranferBatch(result);
                }else if("1".equals(params.get("unionFlag"))){
                    String result = minSheng.transferBatchInnerBank(params);
                    res = minSheng.handleTranferBatch(result);
                }else{
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("status", "fail");
                    map.put("info", "缺少参数 跨行标志unionFlag！");
                    log.info( "缺少参数 跨行标志unionFlag！");
                    res = JSON.toJSONString(map);
                }
            }
		} catch (Exception e) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("status", "fail");
			res = JSON.toJSONString(map);
			e.printStackTrace();
		} finally {
			log.info("response=" + res);
			outJson(res, response);
		}
	}

}
