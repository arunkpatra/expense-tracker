/*
 * Copyright 2002-2004 the original author or authors.
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

package com.exp.tracker.app.reports;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.exp.tracker.data.model.ExpenseReportDataBean;
import com.exp.tracker.data.model.SettlementBean;
import com.exp.tracker.data.model.UserSettlementBean;
import com.exp.tracker.services.api.SettlementService;

/**
 * Simple <code>Controller</code> implementation returning
 * data for report rendering. This implementation demonstrates
 * how JasperReports view can be integrated into your application
 * without placing a JasperReports dependency on your application code.
 * All data returned in the <code>ModelAndView</code> instances uses
 * standard Java classes.
 *
 * @author Rob Harrop
 */
@Controller
@RequestMapping("/reports")
public class ReportController extends MultiActionController {

    @Autowired
	private SettlementService settlementService;
	/**
	 * Returns model and view for settlement sub report
	 */
	@RequestMapping(value = "/settlementSubReport.pdf", method = RequestMethod.GET)
	public ModelAndView handleSettlementSubReport(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String settlementId = (String) request.getParameter("sid");
		ModelAndView mv = new ModelAndView("settlementSubReport", getModelForSettlementSubReport(settlementId));
		//
		System.out.println("Generating Settlement report for settlement # " + settlementId);
		//
		return mv;
	}
	
	/**
	 * Returns model and view for expense report.
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/expenseReport.pdf", method = RequestMethod.GET)
	public ModelAndView handleExpenseReport(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String settlementId = (String) request.getParameter("sid");
		ModelAndView mv = new ModelAndView("expenseReport", getModelForExpenseReport(settlementId));
		//
		System.out.println("Generating Expense report for settlement # " + settlementId);
		//
		return mv;
	}
	
	/**
	 * Returns model for expense report
	 * @param settlementId
	 * @return
	 */
	private Map<String, ?> getModelForExpenseReport(String settlementId) {
		Map<String, Object> model = new HashMap<String, Object>();
		// get data from back end
		Long sid = Long.parseLong(settlementId);
		List<ExpenseReportDataBean> erdbList = settlementService.getExpensesForSettlementId(sid);
		SettlementBean sb = settlementService.getSettlementById(sid);
		// put parameters
		model.put("reportTitle", "Expense Report");		
		model.put("startDate", sb.getStartDate());
		model.put("endDate", sb.getEndDate());
		model.put("totalVolume", sb.getVolume());
		// put detail record data
		model.put("dataSource", erdbList);
		// return
		return model;
	}
	/**
	 * Returns model data for settlement sub report.
	 * 
	 * @param settlementId
	 * @return
	 */
	private Map<String, ?> getModelForSettlementSubReport(String settlementId) {
		Map<String, Object> model = new HashMap<String, Object>();
		// get data from back end
		Long sid = Long.parseLong(settlementId);
		SettlementBean sb = settlementService.getSettlementById(sid);
		// set params
		model.put("reportTitle", "Settlement Report");		
		model.put("startDate", sb.getStartDate());
		model.put("endDate", sb.getEndDate());
		model.put("volume", sb.getVolume());
		//
		List<UserSettlementBean> usbList = sb.getUserSettlementList();
		// get data
		model.put("dataSource", usbList);
		return model;
	}

	public SettlementService getSettlementService() {
		return settlementService;
	}

	public void setSettlementService(SettlementService settlementService) {
		this.settlementService = settlementService;
	}
}