package com.hotelbooking.model;

import java.util.Date;

public class Room implements Comparable< Room > {
	
	private Long id;
	private String roomType;
	private Date startDate;
	private Date endDate;
	private int minGuests;
	private int maxGuests;
	private Double price;
	private String priceModel;
	private int currentOccupancy;
	
	
	public Room(Long id, String roomType, Date startDate, Date endDate, int minGuests, int maxGuests, Double price, String priceModel) {
		super();
		this.id = id;
		this.roomType = roomType;
		this.startDate = startDate;
		this.endDate = endDate;
		this.minGuests = minGuests;
		this.maxGuests = maxGuests;
		this.price = price;
		this.priceModel = priceModel;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRoomType() {
		return roomType;
	}
	public void setRoomType(String roomType) {
		this.roomType = roomType;
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
	public int getMinGuests() {
		return minGuests;
	}
	public void setMinGuests(int minGuests) {
		this.minGuests = minGuests;
	}
	public int getMaxGuests() {
		return maxGuests;
	}
	public void setMaxGuests(int maxGuests) {
		this.maxGuests = maxGuests;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getPriceModel() {
		return priceModel;
	}
	public void setPriceModel(String priceModel) {
		this.priceModel = priceModel;
	}
	public int getCurrentOccupancy() {
		return currentOccupancy;
	}
	public void setCurrentOccupancy(int currentOccupancy) {
		this.currentOccupancy = currentOccupancy;
	};
	
	@Override
	public int compareTo(Room obj) {
		return Double.compare(new Double(this.getPrice()), new Double(obj.getPrice()));
	}
	

}
