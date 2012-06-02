/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.model;

import java.io.Serializable;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author sai
 */
@Entity
@Table(name = "link_categories")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LinkCategory.findAll", query = "SELECT l FROM LinkCategory l"),
    @NamedQuery(name = "LinkCategory.findById", query = "SELECT l FROM LinkCategory l WHERE l.id = :id"),
    @NamedQuery(name = "LinkCategory.findByCount", query = "SELECT l FROM LinkCategory l WHERE l.count = :count"),
    @NamedQuery(name = "LinkCategory.findByName", query = "SELECT l FROM LinkCategory l WHERE l.name = :name"),
    @NamedQuery(name = "LinkCategory.findByDescription", query = "SELECT l FROM LinkCategory l WHERE l.description = :description")})
public class LinkCategory implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
//    @NotNull
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "count", nullable = false)
    private int count;
    @Size(max = 100)
    @Column(name = "name", length = 100)
    private String name;
    @Size(max = 255)
    @Column(name = "description", length = 255)
    private String description;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "linkCategory")
    private List<Link> linkList;

    public LinkCategory() {
    }

    public LinkCategory(Integer id) {
        this.id = id;
    }

    public LinkCategory(Integer id, int count) {
        this.id = id;
        this.count = count;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlTransient
    public List<Link> getLinkList() {
        return linkList;
    }

    public void setLinkList(List<Link> linkList) {
        this.linkList = linkList;
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
        if (!(object instanceof LinkCategory)) {
            return false;
        }
        LinkCategory other = (LinkCategory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cherrot.govproject.model.LinkCategory[ id=" + id + " ]";
    }

}
