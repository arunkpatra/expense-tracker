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
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "et_settlement")
@NamedQueries( {
		@NamedQuery(name = "getExpensesForSettlementId", query = 
				"SELECT e.id, "
				+ "e.amount, "
				+ "e.date, "
				+ "e.description, "
				+ "e.paidBy, "
				+ "ue.username, "
				+ "ue.shareAmount "
				+ "FROM ExpenseEntity e, UserExpenseEntity ue "
				+ "WHERE (e.settlementId = :settlementId) AND (e.id = ue.expense_id) ORDER BY e.date ASC, e.id ASC"),
		@NamedQuery(name = "paymentsForUser", query = "SELECT us.amount, "
				+ "us.settlementFlag, "
				+ "s.startDate, "
				+ "s.endDate, "
				+ "s.id, "
				+ "us.userName "
				+ "FROM UserSettlementEntity us, SettlementEntity s "
				+ "WHERE (us.userName = :userName) AND (us.settlement_id = s.id) ORDER BY s.endDate DESC"),
		@NamedQuery(name = "allPayments", query = "SELECT us.amount, "
				+ "us.settlementFlag, "
				+ "s.startDate, "
				+ "s.endDate, "
				+ "s.id, "
				+ "us.userName, "
				+ "us.id "
				+ "FROM UserSettlementEntity us, SettlementEntity s "
				+ "WHERE (us.settlement_id = s.id) ORDER BY s.endDate DESC"),
		@NamedQuery(name = "getAllSettlements", query = "SELECT s from SettlementEntity s ORDER BY s.endDate DESC"),
//		@NamedQuery(name = "addSettlementReportPdf", query = "UPDATE SettlementEntity s " +
//				"SET s.reportPdf = :settlementReportPdf " +
//				"WHERE s.id = :id"),
//		@NamedQuery(name = "addExpenseReportPdf", query = "UPDATE SettlementEntity s " +
//				"SET s.expenseReportPdf = :expenseReportPdf " +
//				"WHERE s.id = :id"),
		@NamedQuery(name = "getAllSettlementsForUser", query = "SELECT s from SettlementEntity s, UserSettlementEntity use " +
				"WHERE (s.id = use.settlement_id) AND " +
				"(use.userName = :userName) ORDER BY s.endDate DESC"),
		@NamedQuery(name = "deleteSettlementForId", query = "DELETE SettlementEntity s WHERE s.id = :sid"),
		@NamedQuery(name = "getSettlementForId", query = "SELECT s from SettlementEntity s WHERE s.id = :id " +
				"ORDER BY s.endDate DESC")})
public class SettlementEntity implements Serializable {

	private static final long serialVersionUID = -3307656260377225300L;
	public static final int SETTLEMENT_COMPLETED = 1;
	public static final int SETTLEMENT_NOT_COMPLETED = 0;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "cyclestartdate")
	private Date startDate;

	@Column(name = "cycleenddate")
	private Date endDate;

	@Column(name = "volume")
	private float volume;
	
	@Column(name="createddate")
	private Date createdDate;
	
	@Column(name="closeddate")
	private Date closedDate;
	
//	@Column(name="reportpdf")
//	@Lob
//	private byte[] reportPdf;
//
//	@Column(name="expensereportpdf")
//	@Lob
//	private byte[] expenseReportPdf;
	
	@Column(name = "settlementcompleted")
	private int settlementCompleted;
	
	@Column(name="accountmanager")
	private String accountManager;

	@OneToMany(targetEntity = UserSettlementEntity.class, 
			cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "settlement")
	private Set<UserSettlementEntity> userSettlementSet;

	/**
	 * There might be many expenses for a settlement
	 */
	@OneToMany(targetEntity = ExpenseEntity.class, 
			cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "settlement")
//	@GenericGenerator(name="uuid-gen", strategy = "uuid")
//	@CollectionId(columns = @Column(name = "COL_ID"), type = @Type(type = "string"), generator = "uuid-gen")
	private Set<ExpenseEntity> expenseSet;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public float getVolume() {
		return volume;
	}

	public void setVolume(float volume) {
		this.volume = volume;
	}

	public int getSettlementCompleted() {
		return settlementCompleted;
	}

	public void setSettlementCompleted(int settlementCompleted) {
		this.settlementCompleted = settlementCompleted;
	}

	public Set<UserSettlementEntity> getUserSettlementSet() {
		return userSettlementSet;
	}

	public void setUserSettlementSet(
			Set<UserSettlementEntity> userSettlementSet) {
		this.userSettlementSet = userSettlementSet;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getClosedDate() {
		return closedDate;
	}

	public void setClosedDate(Date closedDate) {
		this.closedDate = closedDate;
	}

//	public byte[] getReportPdf() {
//		return reportPdf;
//	}
//
//	public void setReportPdf(byte[] reportPdf) {
//		this.reportPdf = reportPdf;
//	}
//
//	public byte[] getExpenseReportPdf() {
//		return expenseReportPdf;
//	}
//
//	public void setExpenseReportPdf(byte[] expenseReportPdf) {
//		this.expenseReportPdf = expenseReportPdf;
//	}

	public String getAccountManager() {
		return accountManager;
	}

	public void setAccountManager(String accountManager) {
		this.accountManager = accountManager;
	}

	public Set<ExpenseEntity> getExpenseSet() {
		return expenseSet;
	}

	public void setExpenseSet(Set<ExpenseEntity> expenseSet) {
		this.expenseSet = expenseSet;
	}

}
