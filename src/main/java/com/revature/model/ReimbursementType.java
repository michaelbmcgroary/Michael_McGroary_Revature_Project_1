package com.revature.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="ers_reimbursement_type")
public class ReimbursementType {


		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		@Column(name = "reimb_type_id")
		private int typeID;
		
		@Column(name = "reimb_type", length=10)
		private String type;
		
		public ReimbursementType() {
			super();
		}

		public ReimbursementType(int typeID) {
			super();
			this.typeID = typeID;
			switch(typeID) {
				case 1: type = "Lodging";
					break;
				case 2: type = "Travel";
					break;
				case 3: type = "Food";
					break;
				case 4: type = "Other";
					break;
				default: type = "Other";
					break;
			}
		}

		public ReimbursementType(String type) {
			this.type = type;
			switch(type) {
				case "Lodging": typeID = 1;
					break;
				case "Travel": typeID = 2;
					break;
				case "Food": typeID = 3;
					break;
				case "Other": typeID = 4;
					break;
				default: 
					typeID = 4;
					this.type = "Other";
					break;
			}
		}

		public int getTypeID() {
			return typeID;
		}

		public void setTypeID(int typeID) {
			this.typeID = typeID;
			setType(this.typeID);
		}

		public String getType() {
			return type;
		}

		public void setType(int typeID) {
			switch(typeID) {
				case 1: type = "Lodging";
					break;
				case 2: type = "Travel";
					break;
				case 3: type = "Food";
					break;
				case 4: type = "Other";
					break;
				default: type = "Other";
					break;
			}
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((type == null) ? 0 : type.hashCode());
			result = prime * result + typeID;
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
			ReimbursementType other = (ReimbursementType) obj;
			if (type == null) {
				if (other.type != null)
					return false;
			} else if (!type.equals(other.type))
				return false;
			if (typeID != other.typeID)
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "ReimbursementType [typeID=" + typeID + ", type=" + type + "]";
		}
		
		
		

}
