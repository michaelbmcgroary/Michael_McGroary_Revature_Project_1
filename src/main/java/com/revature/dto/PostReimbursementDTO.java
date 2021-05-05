package com.revature.dto;

import java.io.ByteArrayInputStream;
import com.revature.model.ReimbursementType;
import com.revature.model.User;

public class PostReimbursementDTO {

	private String amount;
	private String description;
	private User author;
	private ByteArrayInputStream blob;
	private ReimbursementType type;
	private int id;
	
	public PostReimbursementDTO() {
		super();
	}
	
	
	public PostReimbursementDTO(String amount, String description, int type, ByteArrayInputStream blob) {
		super();
		this.amount = amount;
		this.description = description;
		this.type = new ReimbursementType(type);
		this.blob = blob;
	}

	
	

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getAmount() {
		return amount;
	}


	public void setAmount(String amount) {
		this.amount = amount;
	}


	public void setAuthor(User author) {
		this.author = author;
	}


	public ByteArrayInputStream getBlob() {
		return blob;
	}

	public void setBlob(ByteArrayInputStream blob) {
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


	public void setAuthorID(User author) {
		this.author = author;
	}


	public ReimbursementType getType() {
		return type;
	}


	public void setType(ReimbursementType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "PostReimbursementDTO [amount=" + amount + ", description="
				+ description + ", author=" + author + ", blob=" + blob + ", type=" + type + "]";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result + ((blob == null) ? 0 : blob.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + id;
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
		PostReimbursementDTO other = (PostReimbursementDTO) obj;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		if (blob == null) {
			if (other.blob != null)
				return false;
		} else if (!blob.equals(other.blob))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id != other.id)
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}



	
	
	
	
	
}
