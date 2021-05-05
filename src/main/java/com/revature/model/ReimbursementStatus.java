package com.revature.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ers_reimbursement_status")
public class ReimbursementStatus {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "reimb_status_id")
	private int statusID;
	
	@Column(name = "reimb_status", length=10)
	private String status;
	
	public ReimbursementStatus() {
		super();
	}

	public ReimbursementStatus(int statusID) {
		super();
		this.statusID = statusID;
		switch(statusID) {
			case 1: status = "Approved";
				break;
			case 2: status = "Denied";
				break;
			case 3: status = "Pending";
				break;
			default: status = "Pending";
				break;
		}
	}

	public int getStatusID() {
		return statusID;
	}

	public void setStatusID(int statusID) {
		this.statusID = statusID;
		setStatus(this.statusID);
	}
	
	public void setStatus(String status) {
		this.status = status;
		switch(status) {
			case "Approved": statusID = 1;
				break;
			case "Denied": statusID = 2;
				break;
			case "Pending": statusID = 3;
				break;
			default: 
				this.statusID = 3;
				status = "Pending";
				break;
		}
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(int statusID) {
		switch(statusID) {
			case 1: status = "Approved";
				break;
			case 2: status = "Denied";
				break;
			case 3: status = "Pending";
				break;
			default: status = "Pending";
				break;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + statusID;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReimbursementStatus other = (ReimbursementStatus) obj;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (statusID != other.statusID)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ReimbursementStatus [statusID=" + statusID + ", status=" + status + "]";
	}
	
	
}
