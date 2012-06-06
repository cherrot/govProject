/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author sai
 */
@Entity
@Table(name = "users")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
    @NamedQuery(name = "User.findById", query = "SELECT u FROM User u WHERE u.id = :id"),
    @NamedQuery(name = "User.findByLogin", query = "SELECT u FROM User u WHERE u.login = :login"),
    @NamedQuery(name = "User.findByPass", query = "SELECT u FROM User u WHERE u.pass = :pass"),
    @NamedQuery(name = "User.findByUserLevel", query = "SELECT u FROM User u WHERE u.userLevel = :userLevel"),
    @NamedQuery(name = "User.findByRegisterDate", query = "SELECT u FROM User u WHERE u.registerDate = :registerDate"),
    @NamedQuery(name = "User.findByDisplayName", query = "SELECT u FROM User u WHERE u.displayName = :displayName"),
    @NamedQuery(name = "User.findByUrl", query = "SELECT u FROM User u WHERE u.url = :url")})
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
//    @NotNull
    @Column(name = "id", nullable = false)
    private Integer id;
    //if the field contains email address consider using this annotation to enforce field validation
    @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="电子邮件无效")
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "login", nullable = false, length = 32)
    private String login;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "pass", nullable = false, length = 64)
    private String pass;
    @Basic(optional = false)
    @NotNull
    @Column(name = "userLevel", nullable = false)
    private int userLevel;
    @Basic(optional = false)
    @NotNull
    @Column(name = "registerDate", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date registerDate;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "displayName", nullable = false, length = 45)
    private String displayName;
//    //if the field contains email address consider using this annotation to enforce field validation
//    @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="电子邮件无效")
//    @Basic(optional = false)
//    @NotNull
//    @Size(min = 1, max = 100)
//    @Column(name = "email", nullable = false, length = 100)
//    private String email;
    @Size(max = 100)
    @Column(name = "url", length = 100)
    private String url;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<SiteLog> siteLogList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Post> postList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Usermeta> usermetaList;

    public User() {
    }

//    public User(Integer id) {
//        this.id = id;
//    }

    public User(/*Integer id,*/ String login, String pass, int userLevel, Date registerDate, String displayName/*, String email*/) {
//        this.id = id;
        this.login = login;
        this.pass = pass;
        this.userLevel = userLevel;
        this.registerDate = registerDate;
        this.displayName = displayName;
//        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @XmlTransient
    public List<SiteLog> getSiteLogList() {
        return siteLogList;
    }

    public void setSiteLogList(List<SiteLog> siteLogList) {
        this.siteLogList = siteLogList;
    }

    @XmlTransient
    public List<Post> getPostList() {
        return postList;
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
    }

    @XmlTransient
    public List<Usermeta> getUsermetaList() {
        return usermetaList;
    }

    public void setUsermetaList(List<Usermeta> usermetaList) {
        this.usermetaList = usermetaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /**
     * Warning - this method won't work in the case the id fields are not set
     * @param object
     * @return
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cherrot.govproject.model.User[ id=" + id + " ]";
    }

}
