package com.yogapay.bcdl.service;

import java.sql.SQLException;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yogapay.bcdl.domain.TransferRecord;
import com.yogapay.bcdl.utils.Dao;

@Service
public class TransferRecordService {
	@Resource
	private Dao dao;

	public TransferRecord findTransferRecordById(Long id) throws SQLException {
		String sql = "select * from transfer_record tr where tr.id = ?";
		return dao.findFirst(TransferRecord.class, sql, new Object[] { id });
	}
	
	public TransferRecord findTransferRecordByThirdVoucher(String thirdVoucher) throws SQLException{
		String sql = "select * from transfer_record tr where tr.third_voucher = ?";
		return dao.findFirst(TransferRecord.class, sql, new Object[] { thirdVoucher });
	}
	

//	public void update(TransferRecord transferRecord) throws SQLException {
//		String sql = "update transfer_record set transfer_no = ?, third_voucher = ?, create_time =? where id = ?";
//		dao.update(
//				sql,
//				new Object[] { transferRecord.getTransferNo(),
//						transferRecord.getThirdVoucher(),
//						transferRecord.getCreateTime(), transferRecord.getId() });
//	}

}
