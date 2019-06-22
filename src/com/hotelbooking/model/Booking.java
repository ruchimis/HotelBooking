package com.hotelbooking.model;

public class Booking implements Comparable< Booking > {
	private String roomType;
	private int pax;
	private Double pricePerPerson;
	private String priceModel;
	
	public Booking(String roomType, int pax, Double pricePerPerson, String priceModel) {
		super();
		this.roomType = roomType;
		this.pax = pax;
		this.pricePerPerson = pricePerPerson;
		this.priceModel = priceModel;
	}
	
	public int getPax() {
		return pax;
	}
	public void setPax(int pax) {
		this.pax = pax;
	}
	public Double getPricePerPerson() {
		return pricePerPerson;
	}
	public void setPricePerPerson(Double pricePerPerson) {
		this.pricePerPerson = pricePerPerson;
	}
	
	public String getPriceModel() {
		return priceModel;
	}
	public void setPriceModel(String priceModel) {
		this.priceModel = priceModel;
	}
	public String getRoomType() {
		return roomType;
	}
	
	@Override
	public int compareTo(Booking obj) {
		int value1 = this.getPricePerPerson().compareTo(obj.getPricePerPerson());
		if(value1 == 0) {
			int value2 = Long.compare(new Long(obj.getPax()), new Long(this.getPax()));
			return value2;
		}
		return value1;
	}
	
	@Override
	public String toString() {
		return "[roomType=" + roomType + ", pax=" + pax + ", price=" + String.format("%.02f", pricePerPerson) + "/" + priceModel + "]\n";
	}
	
	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}
	
	
}
