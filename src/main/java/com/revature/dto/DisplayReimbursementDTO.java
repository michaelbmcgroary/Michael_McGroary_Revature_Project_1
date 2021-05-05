package com.revature.dto;

import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.revature.model.Reimbursement;
import com.revature.model.User;

public class DisplayReimbursementDTO {

	private int reimbID;
	private double amount;
	private String status;
	private String author;
	private String resolver;
	private String submitTime;
	private String resolveTime;
	private String type;
	private String description;
	private boolean blobExists;
	
	public DisplayReimbursementDTO() {
		super();
	}
	
	public DisplayReimbursementDTO(Reimbursement reimb) throws SQLException {
		User user;
		this.reimbID = reimb.getReimbursementID();
		this.amount = reimb.getAmount();
		this.status = reimb.getStatusString();
		user = reimb.getAuthor();
		this.author = user.getLastName() + ", " + user.getFirstName();
		user = reimb.getResolver();
		if(user != null) {
			this.resolver = user.getLastName() + ", " + user.getFirstName();
		} else {
			this.resolver = "N/A";
		}
		this.type = reimb.getTypeString();
		this.submitTime = reimb.getSubmittedTime().toString();
		Timestamp ts = reimb.getResolvedTime();
		if(ts != null) {
			this.resolveTime = ts.toString();
		} else {
			this.resolveTime = "N/A";
		}
		this.description = reimb.getDescription();
		if(reimb.getBlob() != null && reimb.getBlob().length() != 0) {
			blobExists = true;
		} else {
			blobExists = false;
		}
	}
	
	

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getReimbID() {
		return reimbID;
	}

	public void setReimbID(int reimbID) {
		this.reimbID = reimbID;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAuthor() {
		return author;
	}
	
	public boolean isBlobExists() {
		return blobExists;
	}

	public void setBlobExists(boolean blobExists) {
		this.blobExists = blobExists;
	}

	public void setSubmitTime(String submitTime) {
		this.submitTime = submitTime;
	}

	public void setResolveTime(String resolveTime) {
		this.resolveTime = resolveTime;
	}

	public void setAuthor(String author) {
		if(author != null) {
			this.author = author;
		} else {
			this.resolveTime = "N/A";
		}
	}

	public String getResolver() {
		return resolver;
	}

	public void setResolver(String resolver) {
		this.resolver = resolver;
	}

	public String getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Timestamp submitTime) {
		this.submitTime = submitTime.toString();
	}

	public String getResolveTime() {
		return resolveTime;
	}

	public void setResolveTime(Timestamp resolveTime) {
		if(resolveTime != null) {
			this.resolveTime = resolveTime.toString();
		} else {
			this.resolveTime = "N/A";
		}
	}

	public void setBlob(Blob blob) {
		if(blob != null) {
			blobExists = true;
		} else {
			blobExists = false;
		}
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(amount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result + reimbID;
		result = prime * result + ((resolveTime == null) ? 0 : resolveTime.hashCode());
		result = prime * result + ((resolver == null) ? 0 : resolver.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((submitTime == null) ? 0 : submitTime.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		DisplayReimbursementDTO other = (DisplayReimbursementDTO) obj;
		if (Double.doubleToLongBits(amount) != Double.doubleToLongBits(other.amount))
			return false;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		if (reimbID != other.reimbID)
			return false;
		if (resolveTime == null) {
			if (other.resolveTime != null)
				return false;
		} else if (!resolveTime.equals(other.resolveTime))
			return false;
		if (resolver == null) {
			if (other.resolver != null)
				return false;
		} else if (!resolver.equals(other.resolver))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (submitTime == null) {
			if (other.submitTime != null)
				return false;
		} else if (!submitTime.equals(other.submitTime))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DisplayReimbursementDTO [reimbID=" + reimbID + ", amount=" + amount + ", status=" + status + ", author="
				+ author + ", resolver=" + resolver + ", submitTime=" + submitTime + ", resolveTime=" + resolveTime
				+ ", type=" + type + "]";
	}
	
	
	
	
}
