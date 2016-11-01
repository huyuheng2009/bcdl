package com.yogapay.bcdl.service;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yogapay.bcdl.domain.TransferRecordInfo;
import com.yogapay.bcdl.utils.Dao;
@Service
public class TransferRecordInfoService {
	@Resource
	private Dao dao;
	
	public TransferRecordInfo findTransferRecordInfoBySThirdVoucher(String sThirdVoucher) throws SQLException{
		String sql = "select * from transfer_record_info tri where tri.sthird_voucher = ?";
		return dao.findFirst(TransferRecordInfo.class, sql, new Object[]{sThirdVoucher});
	}
	
	public List<TransferRecordInfo> findTransferRecordInfoByTransferRecordId(Long transferRecordId) throws SQLException{
		String sql = "select * from transfer_record_info tri where tri.transfer_record_id = ?";
		return dao.find(TransferRecordInfo.class, sql, transferRecordId);
	}
	
	public void update(TransferRecordInfo transferRecordInfo) throws SQLException{
		String sql = "update transfer_record_info set inacct_status = ?, front_log_no = ? where id = ?";
		dao.update(sql, new Object[]{transferRecordInfo.getInacctStatus(), transferRecordInfo.getFrontLogNo(), transferRecordInfo.getId()});
	}
}
