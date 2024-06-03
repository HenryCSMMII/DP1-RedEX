package com.edu.pucp.dp1.redex.model;

public class Operator {
    private Integer dni;
	private String name;
	private String lastName;
	private String email;
	private Integer mobileNumber;
	
	public Operator() {
    }

    public Integer getDni() {
        return dni;
    }

    public void setDni(Integer opeDni) {
        this.dni = opeDni;
    }

    public String getName() {
        return name;
    }

    public void setName(String opeName) {
        this.name = opeName;
    }
    
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String opeLastName) {
        this.lastName = opeLastName;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String opeEmail) {
        this.email = opeEmail;
    }
    
    public Integer getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(Integer opeNumber) {
        this.mobileNumber = opeNumber;
    }

    public Operator(Integer opeDni, String opeName, String opeLastName, String opeEmail, Integer opeNumber) {
        this.dni = opeDni;
        this.name = opeName;
        this.lastName = opeLastName;
        this.email = opeEmail;
        this.mobileNumber = opeNumber;
    }
}
