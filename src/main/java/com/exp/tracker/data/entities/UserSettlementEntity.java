package com.exp.tracker.data.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "et_user_settlement")
@NamedQueries( {
		@NamedQuery(name = "getUserSettlementById", query = "SELECT use from UserSettlementEntity use "
				+ "WHERE use.id = :userSettlementId"),
		@NamedQuery(name = "getUnsettledUserSettlementsForSettlementId", query = "SELECT use from UserSettlementEntity use "
				+ "WHERE (use.settlement_id = :settlementId) AND (use.settlementFlag = 0)") })
public class UserSettlementEntity implements Serializable {

	private static final long serialVersionUID = 2574201400839693407L;
	public static final int SETTLEMENT_COMPLETED = 1;
	public static final int SETTLEMENT_NOT_COMPLETED = 0;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "settlement_id")
	private Long settlement_id;

	@Column(name = "amount")
	private float amount;

	@Column(name = "settlementcompleted")
	private int settlementFlag;

	@Column(name = "username")
	private String userName;

	@Column(name = "setteleddate")
	private Date settledDate;
	
	@Column(name="usershare")
	private Float userShare;
	
	@Column(name="userpaid")
	private Float userPaid;

	@ManyToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "settlement_id", referencedColumnName = "id", insertable = false, updatable = false)
	private SettlementEntity settlement;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSettlement_id() {
		return settlement_id;
	}

	public void setSettlement_id(Long settlementId) {
		settlement_id = settlementId;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public int getSettlementFlag() {
		return settlementFlag;
	}

	public void setSettlementFlag(int settlementFlag) {
		this.settlementFlag = settlementFlag;
	}

	public SettlementEntity getSettlement() {
		return settlement;
	}

	public void setSettlement(SettlementEntity settlement) {
		this.settlement = settlement;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getSettledDate() {
		return settledDate;
	}

	public void setSettledDate(Date settledDate) {
		this.settledDate = settledDate;
	}

	public Float getUserShare() {
		return userShare;
	}

	public void setUserShare(Float userShare) {
		this.userShare = userShare;
	}

	public Float getUserPaid() {
		return userPaid;
	}

	public void setUserPaid(Float userPaid) {
		this.userPaid = userPaid;
	}

}
