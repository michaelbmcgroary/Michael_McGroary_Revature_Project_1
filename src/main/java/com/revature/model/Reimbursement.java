package com.revature.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.sql.rowset.serial.SerialBlob;

import org.hibernate.Session;

import com.revature.dto.PostReimbursementDTO;
import com.revature.exception.DatabaseException;
import com.revature.util.ConnectionUtil;
import com.revature.util.SessionUtility;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;


@Entity
@Table(name="ers_reimbursement")
public class Reimbursement {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "reimb_id")
	private int reimbursementID;
	
	@Column(name = "reimb_amount")
	private double amount;
	
	@Column(name = "reimb_submitted")
	private Timestamp submittedTime;
	
	@Column(name = "reimb_resolved")
	private Timestamp resolvedTime;
	
	@Column(name = "reimb_description")
	private String description;
	
	@Column(name = "reimb_reciept")
	private Blob blob;
	
	@ManyToOne
	@JoinColumn(name = "reimb_author")
	private User author;
	
	@ManyToOne
	@JoinColumn(name = "reimb_resolver")
	private User resolver;
	
	@ManyToOne
	@JoinColumn(name = "reimb_status_id")
	private ReimbursementStatus status;
	
	@ManyToOne
	@JoinColumn(name = "reimb_type_id")
	private ReimbursementType type;
	
	public Reimbursement() {
		super();
	}
	
	public Reimbursement(int reimbursementID, double amount, String description, Blob blob, User author, User resolver, ReimbursementStatus status, ReimbursementType type, Timestamp submittedTime, Timestamp resolvedTime) {
		super();
		this.reimbursementID = reimbursementID;
		this.amount = amount;
		this.description = description;
		this.blob = blob;
		this.author = author;
		this.resolver = resolver;
		this.status = status;
		this.type = type;
		this.submittedTime = submittedTime;
		this.resolvedTime = resolvedTime;
	}
	
	public Reimbursement(PostReimbursementDTO dto) throws DatabaseException {
		this.description = dto.getDescription();
		this.author = dto.getAuthor();
		if(dto.getBlob() != null) {
			try {
				
				this.blob = new SerialBlob(dto.getBlob().readAllBytes());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		
		this.type = dto.getType();
		this.status = new ReimbursementStatus(3);
	}

	public int getReimbursementID() {
		return reimbursementID;
	}

	public void setReimbursementID(int reimbursementID) {
		this.reimbursementID = reimbursementID;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Blob getBlob() {
		return blob;
	}

	public void setBlob(Blob blob) {
		this.blob = blob;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public User getResolver() {
		return resolver;
	}

	public void setResolver(User resolver) {
		this.resolver = resolver;
	}

	public ReimbursementStatus getStatus() {
		return status;
	}
	
	public String getStatusString() {
		return status.getStatus();
	}

	public void setStatus(ReimbursementStatus status) {
		this.status = status;
	}
	
	public void setStatus(int status) {
		this.status = new ReimbursementStatus(status);
	}

	public ReimbursementType getType() {
		return type;
	}
	
	public String getTypeString() {
		return type.getType();
	}

	public void setType(ReimbursementType type) {
		this.type = type;
	}

	public Timestamp getSubmittedTime() {
		return submittedTime;
	}

	public void setSubmittedTime(Timestamp submittedTime) {
		this.submittedTime = submittedTime;
	}

	public Timestamp getResolvedTime() {
		return resolvedTime;
	}

	public void setResolvedTime(Timestamp resolvedTime) {
		this.resolvedTime = resolvedTime;
	}

	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(amount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + reimbursementID;
		result = prime * result + ((resolvedTime == null) ? 0 : resolvedTime.hashCode());
		result = prime * result + ((resolver == null) ? 0 : resolver.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((submittedTime == null) ? 0 : submittedTime.hashCode());
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
		Reimbursement other = (Reimbursement) obj;
		if (Double.doubleToLongBits(amount) != Double.doubleToLongBits(other.amount))
			return false;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (reimbursementID != other.reimbursementID)
			return false;
		if (resolvedTime == null) {
			if (other.resolvedTime != null)
				return false;
		} else if (!resolvedTime.equals(other.resolvedTime))
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
		if (submittedTime == null) {
			if (other.submittedTime != null)
				return false;
		} else if (!submittedTime.equals(other.submittedTime))
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
		return "Reimbursement [reimbursementID=" + reimbursementID + ", amount=" + amount + ", submittedTime="
				+ submittedTime + ", resolvedTime=" + resolvedTime + ", description=" + description + ", author="
				+ author + ", resolver=" + resolver + ", status=" + status + ", type=" + type + "]";
	}
	
	
	
	
}
