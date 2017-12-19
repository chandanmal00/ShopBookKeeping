package com.bookkeeping.model;

import com.bookkeeping.utilities.ControllerUtilities;
import com.google.gson.Gson;

/**
 * Created by chandan on 6/4/16.
 */
public class Person extends InventoryObj {

    private String firstName;
    private String lastName;
    private String nickName;
    private Location location;
    private NationalIdentity nationalIdentity;
    private String email;
    private Contact contact;


    //Default value for age if not provided
    private int age=-1;

    public Person(String nickName) {
        super();
        this.nickName = nickName;
        this.setUniqueKey(ControllerUtilities.formatUniqueKey(nickName));
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public NationalIdentity getNationalIdentity() {
        return nationalIdentity;
    }

    public void setNationalIdentity(NationalIdentity nationalIdentity) {
        this.nationalIdentity = nationalIdentity;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}


