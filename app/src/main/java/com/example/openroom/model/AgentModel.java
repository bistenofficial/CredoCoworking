package com.example.openroom.model;

import com.google.gson.annotations.SerializedName;


public class AgentModel {

    public AgentModel(String surname, String name, String patronymic, String phone, String email, String password, String salt, Integer verification, Integer id, String dateOfBirth)
    {
        this.surname = surname;

        this.name = name;
        this.patronymic = patronymic;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.salt = salt;
        this.verification = verification;
        this.id = id;
        this.dateOfBirth = dateOfBirth;
    }
    @SerializedName("surname")
    private String surname;

    @SerializedName("name")
    private String name;

    @SerializedName("patronymic")
    private String patronymic;

    @SerializedName("phone")
    private String phone;

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    @SerializedName("salt")
    private String salt;

    @SerializedName("verification")
    private Integer verification;

    @SerializedName("id")
    private Integer id;

    @SerializedName("dateOfBirth")
    private String dateOfBirth;

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Object getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Integer getVerification() {
        return verification;
    }

    public void setVerification(Integer verification) {
        this.verification = verification;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
