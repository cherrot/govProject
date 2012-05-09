/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author cherrot
 */
@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Links.findAll", query = "SELECT l FROM Links l"),
    @NamedQuery(name = "Links.findById", query = "SELECT l FROM Links l WHERE l.id = :id"),
    @NamedQuery(name = "Links.findByUrl", query = "SELECT l FROM Links l WHERE l.url = :url"),
    @NamedQuery(name = "Links.findByName", query = "SELECT l FROM Links l WHERE l.name = :name"),
    @NamedQuery(name = "Links.findByTarget", query = "SELECT l FROM Links l WHERE l.target = :target"),
    @NamedQuery(name = "Links.findByDescription", query = "SELECT l FROM Links l WHERE l.description = :description")})
public class Links implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    private String url;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    private String target;
    @Size(max = 255)
    private String description;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "links")
    private List<TermRelationships> termRelationshipsList;

    public Links() {
    }

    public Links(Integer id) {
        this.id = id;
    }

    public Links(Integer id, String url, String name, String target) {
        this.id = id;
        this.url = url;
        this.name = name;
        this.target = target;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlTransient
    public List<TermRelationships> getTermRelationshipsList() {
        return termRelationshipsList;
    }

    public void setTermRelationshipsList(List<TermRelationships> termRelationshipsList) {
        this.termRelationshipsList = termRelationshipsList;
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
        if (!(object instanceof Links)) {
            return false;
        }
        Links other = (Links) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cherrot.govproject.model.Links[ id=" + id + " ]";
    }

}
