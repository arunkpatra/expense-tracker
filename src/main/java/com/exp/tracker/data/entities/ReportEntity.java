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
package com.exp.tracker.data.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="et_reports")
@NamedQueries({
	@NamedQuery(name="getReport", query="SELECT r FROM ReportEntity r " +
			"WHERE (r.settlementId = :sid) AND (r.reportName = :reportName)"),
	@NamedQuery(name="deleteReportsForSid", query="DELETE FROM ReportEntity r " +
			"WHERE (r.settlementId = :sid)")})
public class ReportEntity implements Serializable{
	private static final long serialVersionUID = 55095596780689514L;
	public static final String SETTLEMENT_REPORT = "settlement_report";
	public static final String EXPENSE_REPORT = "expense_report";
	public static final String PDF_REPORT_TYPE = "PDF";
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="createddate")
	private Date createdDate;
	
	@Column(name="reportcontent")
	@Lob
	private byte[] reportContent;
	
	@Column(name="reporttype")
	private String reportType;
	
	@Column(name="reportname")
	private String reportName;
	
	@Column(name="settlementid")
	private long settlementId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public byte[] getReportContent() {
		return reportContent;
	}

	public void setReportContent(byte[] reportContent) {
		this.reportContent = reportContent;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public long getSettlementId() {
		return settlementId;
	}

	public void setSettlementId(long settlementId) {
		this.settlementId = settlementId;
	}

}
