package com.exp.tracker.services.api;

import java.util.List;

import org.springframework.security.access.annotation.Secured;

import com.exp.tracker.data.entities.RoleEntity;
import com.exp.tracker.data.model.ExpenseReportDataBean;
import com.exp.tracker.data.model.SettlementBean;

public interface SettlementService {
	
	public Long createSettlement(SettlementBean sb);
	
	public List<SettlementBean> getSettlements();
	
	public int completeSettlement(Long sid);
	
	public SettlementBean getSettlementById(Long id);
	
	public List<ExpenseReportDataBean> getExpensesForSettlementId(Long id);
	
	//@PreAuthorize("hasAuthority('ROLE_SITE_ADMIN')")
	@Secured(RoleEntity.ROLE_SITE_ADMIN)
	public int deleteSettlement(Long sid);

}
