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
@RequestMapping(value = "/query")
public class QueryController extends BaseController {

	private static final Logger log = LoggerFactory
			.getLogger(QueryController.class);

	@Resource
	private PingAn pingAn;
    @Resource
    private MinSheng minSheng;

	@RequestMapping(value = "transferOneQuery")
	public void transferOneQuery(@RequestParam Map<String, Object> params,
			HttpServletResponse response) {
		log.info("-------------------transferOneQuery----------------------");
		log.info("params======" + params);
		String res = null;
		try {
			if ("pingAn".equals(params.get("bankCode"))) {
				String result = pingAn.transferOneQuery(params,true);
				res = pingAn.handleTransferOneQuery(result);
			}else if("minSheng".equals(params.get("bankCode"))){
                String result = minSheng.transferOneQuery(params);
                res = minSheng.handleTransferOneQuery(result);
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

	@RequestMapping(value = "transferBatchQuery")
	public void transferBatchQuery(@RequestParam Map<String, Object> params,
			HttpServletResponse response) {
		log.info("-------------------transferBatchQuery----------------------");
		log.info("params======" + params);
		String res = null;
		try {
			if ("pingAn".equals(params.get("bankCode"))) {
				String result = pingAn.transferBathcQuery(params,true);
				res = pingAn.handleTransferBigBatchQuery(result);
			}else if("minSheng".equals(params.get("bankCode"))){
                if("0".equals(params.get("unionFlag"))){
                    String result = minSheng.transferBatchQueryInterBank(params);
                    res = minSheng.handleTransferBatchQueryInterBank(result);
                }else if("1".equals(params.get("unionFlag"))){
                    String result = minSheng.transferBatchQueryInnerBank(params);
                    res = minSheng.handleTransferBatchQueryInnerBank(result);
                }else{
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("status", "fail");
                    map.put("info", "缺少参数 跨行标志unionFlag");
                    log.info("缺少参数 跨行标志unionFlag!");
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

	@RequestMapping(value = "transDetailCurrent")
	public void transDetailCurrent(@RequestParam Map<String, Object> params,
			HttpServletResponse response) {
		log.info("-------------------transDetailCurrent----------------------");
		log.info("params======" + params);
		String res = null;
		try {
			if ("pingAn".equals(params.get("bankCode"))) {
				String result = pingAn.transDetailCurrent(params);
				res = pingAn.handleTransDetailCurrent(result);
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

	@RequestMapping(value = "transDetailHistory")
	public void transDetailHistory(@RequestParam Map<String, Object> params,
			HttpServletResponse response) {
		log.info("-------------------transDetailHistory----------------------");
		log.info("params======" + params);
		String res = null;
		try {
			if ("pingAn".equals(params.get("bankCode"))) {
				String result = pingAn.transDetailHistory(params);
				res = pingAn.handleTransDetailHistory(result);
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
