/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.exp.tracker.data.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "users")
@NamedQueries({
        @NamedQuery(name = "getAllUsers", query = "SELECT u FROM UserEntity u"),
        @NamedQuery(name = "deleteUser", query = "DELETE FROM UserEntity u WHERE u.id = :id"),
        @NamedQuery(name = "getAllUserNames", query = "SELECT u.username FROM UserEntity u"),
        @NamedQuery(name = "getUser", query = "SELECT u FROM UserEntity u WHERE u.username = :username"),
        @NamedQuery(name = "findUserMatch", query = "SELECT u FROM UserEntity u WHERE (u.username = :username)") })
public class UserEntity extends EncryptionHelper implements Serializable
{

    private static final long serialVersionUID = -6352029652777390448L;
    public static final int USER_ENABLED = 1;
    public static final int USER_DISABLED = 0;
    public static final String ALL_USERS = "All";
    public static final int PASSWORD_CHANGE_NEEDED = 1;
    public static final int PASSWORD_CHANGE_NOT_NEEDED = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "enabled")
    private int enabled;

    @Column(name = "pwdchangeneeded")
    private int passwordChangeNeeded;

    @Column(name = "emailid")
    private String emailId;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "middleinit")
    private String middleInit;

    @Column(name = "createddate")
    private Date createdDate;

    @Column(name = "lastupdateddate")
    private Date lastUpdatedDate;

    private String creditCardNumber;

    @OneToMany(targetEntity = AuthEntity.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "user")
    private List<AuthEntity> authSet;

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public int getEnabled()
    {
        return enabled;
    }

    public void setEnabled(int enabled)
    {
        this.enabled = enabled;
    }

    public List<AuthEntity> getAuthSet()
    {
        return authSet;
    }

    public void setAuthSet(List<AuthEntity> authSet)
    {
        this.authSet = authSet;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getEmailId()
    {
        return emailId;
    }

    public void setEmailId(String emailId)
    {
        this.emailId = emailId;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getMiddleInit()
    {
        return middleInit;
    }

    public void setMiddleInit(String middleInit)
    {
        this.middleInit = middleInit;
    }

    public Date getCreatedDate()
    {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate)
    {
        this.createdDate = createdDate;
    }

    public Date getLastUpdatedDate()
    {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Date lastUpdatedDate)
    {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public int getPasswordChangeNeeded()
    {
        return passwordChangeNeeded;
    }

    public void setPasswordChangeNeeded(int passwordChangeNeeded)
    {
        this.passwordChangeNeeded = passwordChangeNeeded;
    }

    public String getCreditCardNumber()
    {
        return decryptString(creditCardNumber);
    }

    @Column(name = "creditcardnumber")
    public void setCreditCardNumber(String creditCardNumber)
    {
        this.creditCardNumber = encryptString(creditCardNumber);
    }
}
