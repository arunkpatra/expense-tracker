package com.exp.tracker.services.api;

import org.springframework.webflow.execution.RequestContext;

public interface ReportGenerationService {
	
	public void generateSettlementReport(Long sid, RequestContext ctx);
	
	public void generateExpenseReport(Long sid, RequestContext ctx);
	
	public byte[] getReportForSettlement(Long sid, String reportName);

}
