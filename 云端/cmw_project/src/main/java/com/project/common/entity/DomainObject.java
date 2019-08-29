package com.project.common.entity;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
public class DomainObject implements Serializable{
	private static final long serialVersionUID = 465213618595566962L;
	protected Integer id;
	private int flag;
	
	public static int DataStatusNormal = 0;
	public static int DataStatusDeleted = -1;
	public static final int LOGICAL_DELETION = 1 << 0;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name = "flag", nullable = false, columnDefinition = "tinyint default 0")
	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	@Transient
	public boolean isLogicalDeleted(){
		return (flag & LOGICAL_DELETION) != 0;
	}


	@Transient
	public void setLogicalDeletion(){
		flag |= LOGICAL_DELETION;
	}
	
}
