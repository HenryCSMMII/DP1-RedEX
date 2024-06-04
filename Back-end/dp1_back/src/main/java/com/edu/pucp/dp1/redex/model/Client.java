package com.edu.pucp.dp1.redex.model;

public class Client {
    private Integer dni;
	private String name;
	private String lastName;
	private String email;
	private Integer mobileNumber;
	
	public Client(Client client) {
		this.dni = client.getDni();
		this.name = client.getName();
		this.lastName = client.getLastName();
		this.email = client.getEmail();
		this.mobileNumber = client.getMobileNumber();
    }

    public Integer getDni() {
        return dni;
    }

    public void setDni(Integer clientDni) {
        this.dni = clientDni;
    }

    public String getName() {
        return name;
    }

    public void setName(String clientName) {
        this.name = clientName;
    }
    
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String clientLastName) {
        this.lastName = clientLastName;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String clientEmail) {
        this.email = clientEmail;
    }
    
    public Integer getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(Integer clientNumber) {
        this.mobileNumber = clientNumber;
    }

    public Client(Integer clientDni, String clientName, String clientLastName, String clientEmail, Integer clientNumber) {
        this.dni = clientDni;
        this.name = clientName;
        this.lastName = clientLastName;
        this.email = clientEmail;
        this.mobileNumber = clientNumber;
    }

}
