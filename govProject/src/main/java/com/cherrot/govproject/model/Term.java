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
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author cherrot
 */
@Entity
@Table(name = "terms", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"slug"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Term.findAll", query = "SELECT t FROM Term t"),
    @NamedQuery(name = "Term.findById", query = "SELECT t FROM Term t WHERE t.id = :id"),
    @NamedQuery(name = "Term.findByName", query = "SELECT t FROM Term t WHERE t.name = :name"),
    @NamedQuery(name = "Term.findBySlug", query = "SELECT t FROM Term t WHERE t.slug = :slug"),
    @NamedQuery(name = "Term.findByTermGroup", query = "SELECT t FROM Term t WHERE t.termGroup = :termGroup")})
public class Term implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(nullable = false, length = 100)
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(nullable = false, length = 200)
    private String slug;
    private Integer termGroup;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "termId")
    private List<TermTaxonomy> termTaxonomyList;

    public Term() {
    }

    public Term(Integer id) {
        this.id = id;
    }

    public Term(Integer id, String name, String slug) {
        this.id = id;
        this.name = name;
        this.slug = slug;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Integer getTermGroup() {
        return termGroup;
    }

    public void setTermGroup(Integer termGroup) {
        this.termGroup = termGroup;
    }

    @XmlTransient
    public List<TermTaxonomy> getTermTaxonomyList() {
        return termTaxonomyList;
    }

    public void setTermTaxonomyList(List<TermTaxonomy> termTaxonomyList) {
        this.termTaxonomyList = termTaxonomyList;
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
        if (!(object instanceof Term)) {
            return false;
        }
        Term other = (Term) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cherrot.govproject.model.Term[ id=" + id + " ]";
    }

}
