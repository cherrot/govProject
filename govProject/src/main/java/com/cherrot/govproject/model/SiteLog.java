/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author sai
 */
@Entity
@Table(name = "site_logs")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SiteLog.findAll", query = "SELECT s FROM SiteLog s"),
    @NamedQuery(name = "SiteLog.findById", query = "SELECT s FROM SiteLog s WHERE s.id = :id"),
    @NamedQuery(name = "SiteLog.findByLogDate", query = "SELECT s FROM SiteLog s WHERE s.logDate = :logDate"),
    @NamedQuery(name = "SiteLog.findByLogOperation", query = "SELECT s FROM SiteLog s WHERE s.logOperation = :logOperation")})
public class SiteLog implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
//    @NotNull
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "logDate", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date logDate;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "logOperation", nullable = false, length = 255)
    private String logOperation;
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private User user;

    public SiteLog() {
        logDate = new Date();
    }

//    public SiteLog(Integer id) {
//        this.id = id;
//    }

    public SiteLog(/*Integer id,*/ Date logDate, String logOperation) {
//        this.id = id;
        this.logDate = logDate;
        this.logOperation = logOperation;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getLogDate() {
        return logDate;
    }

    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }

    public String getLogOperation() {
        return logOperation;
    }

    public void setLogOperation(String logOperation) {
        this.logOperation = logOperation;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
        if (!(object instanceof SiteLog)) {
            return false;
        }
        SiteLog other = (SiteLog) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cherrot.govproject.model.SiteLog[ id=" + id + " ]";
    }

}
