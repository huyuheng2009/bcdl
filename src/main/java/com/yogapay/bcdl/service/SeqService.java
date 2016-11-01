package com.yogapay.bcdl.service;

import java.sql.SQLException;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yogapay.bcdl.utils.Dao;

@Service
public class SeqService {
	@Resource
	private Dao dao;
	
	public long getSeq(String seqName) throws SQLException{
		Map<String, Object> cur = dao.findFirst("select * from sequence where name=?", seqName);
		System.out.println("current_value="+cur.get("current_value"));
		long currentValue =  (Long) cur.get("current_value");
		dao.update("update sequence set current_value = ? where name=?", new Object[]{currentValue+1,seqName});
		return currentValue;
	}
}
