/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author cherrot
 */
@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"optionKey"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Options.findAll", query = "SELECT o FROM Options o"),
    @NamedQuery(name = "Options.findById", query = "SELECT o FROM Options o WHERE o.id = :id"),
    @NamedQuery(name = "Options.findByOptionKey", query = "SELECT o FROM Options o WHERE o.optionKey = :optionKey"),
    @NamedQuery(name = "Options.findByOptionValue", query = "SELECT o FROM Options o WHERE o.optionValue = :optionValue")})
public class Options implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(nullable = false, length = 64)
    private String optionKey;
    @Size(max = 255)
    @Column(length = 255)
    private String optionValue;

    public Options() {
    }

    public Options(Integer id) {
        this.id = id;
    }

    public Options(Integer id, String optionKey) {
        this.id = id;
        this.optionKey = optionKey;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOptionKey() {
        return optionKey;
    }

    public void setOptionKey(String optionKey) {
        this.optionKey = optionKey;
    }

    public String getOptionValue() {
        return optionValue;
    }

    public void setOptionValue(String optionValue) {
        this.optionValue = optionValue;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Options)) {
            return false;
        }
        Options other = (Options) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cherrot.govproject.model.Options[ id=" + id + " ]";
    }

}
