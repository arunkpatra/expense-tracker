package com.exp.tracker.services.api;

import org.springframework.webflow.execution.RequestContext;

/**
 * Report generation service that is responsible for generating settlement and
 * expense reports.
 * 
 * @author Arun Patra
 * 
 */
public interface ReportGenerationService
{

    /**
     * Generates a settlement report.
     * 
     * @param sid
     *            The settlement ID
     * @param ctx
     *            The requestContext
     */
    void generateSettlementReport(Long sid, RequestContext ctx);

    /**
     * Generates an expense report.
     * 
     * @param sid
     *            The settlement ID
     * @param ctx
     *            The requestContext
     */
    void generateExpenseReport(Long sid, RequestContext ctx);

    /**
     * Get the report for a settlement in PDF format.
     * 
     * @param sid
     *            The settlement ID
     * @param reportName
     *            The report name
     * @return byte[] the array of bytes representing the report data.
     */
    byte[] getReportForSettlement(Long sid, String reportName);

}
