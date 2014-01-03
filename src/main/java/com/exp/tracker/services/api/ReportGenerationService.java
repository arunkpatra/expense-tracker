/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
