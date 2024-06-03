package com.edu.pucp.dp1.redex.model;

public class StorageCapacity {
    private int id;
	private Date start_time;
	private Date end_time;
	private int quantity;
	
	
	public StorageCapacity(int id, Date start_time, Date end_time, int quantity) {
		super();
		this.id = id;
		this.start_time = new Date(start_time.getTime());
		this.end_time = new Date(end_time.getTime());
		this.quantity = quantity;
	}
	
	public StorageCapacity(StorageCapacity storage_capacity) {
		this.id = storage_capacity.getId();
		if(storage_capacity.getStart_time()!=null)this.start_time = new Date(storage_capacity.getStart_time().getTime());
		if(storage_capacity.getEnd_time()!=null)this.end_time = new Date(storage_capacity.getEnd_time().getTime());
		this.quantity = storage_capacity.getQuantity();
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public Date getStart_time() {
		return start_time;
	}

	public void setStart_time(Date start_time) {
		this.start_time = start_time;
	}

	public Date getEnd_time() {
		return end_time;
	}

	public void setEnd_time(Date end_time) {
		this.end_time = end_time;
	}

	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
