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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
 * @author cherrot
 */
@Entity
@Table(name = "term_taxonomy")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TermTaxonomy.findAll", query = "SELECT t FROM TermTaxonomy t"),
    @NamedQuery(name = "TermTaxonomy.findById", query = "SELECT t FROM TermTaxonomy t WHERE t.id = :id"),
    @NamedQuery(name = "TermTaxonomy.findByCount", query = "SELECT t FROM TermTaxonomy t WHERE t.count = :count"),
    @NamedQuery(name = "TermTaxonomy.findByTaxonomyParent", query = "SELECT t FROM TermTaxonomy t WHERE t.taxonomyParent = :taxonomyParent"),
    @NamedQuery(name = "TermTaxonomy.findByName", query = "SELECT t FROM TermTaxonomy t WHERE t.name = :name"),
    @NamedQuery(name = "TermTaxonomy.findByDescription", query = "SELECT t FROM TermTaxonomy t WHERE t.description = :description")})
public class TermTaxonomy implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    private Integer id;
    @Basic(optional = false)
    @NotNull
    private int count;
    @Column(name = "taxonomy_parent")
    private Integer taxonomyParent;
    @Size(max = 45)
    private String name;
    @Size(max = 255)
    private String description;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "termTaxonomy")
    private List<TermRelationships> termRelationshipsList;
    @JoinColumn(name = "term_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Terms termId;

    public TermTaxonomy() {
    }

    public TermTaxonomy(Integer id) {
        this.id = id;
    }

    public TermTaxonomy(Integer id, int count) {
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

    public Integer getTaxonomyParent() {
        return taxonomyParent;
    }

    public void setTaxonomyParent(Integer taxonomyParent) {
        this.taxonomyParent = taxonomyParent;
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
    public List<TermRelationships> getTermRelationshipsList() {
        return termRelationshipsList;
    }

    public void setTermRelationshipsList(List<TermRelationships> termRelationshipsList) {
        this.termRelationshipsList = termRelationshipsList;
    }

    public Terms getTermId() {
        return termId;
    }

    public void setTermId(Terms termId) {
        this.termId = termId;
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
        if (!(object instanceof TermTaxonomy)) {
            return false;
        }
        TermTaxonomy other = (TermTaxonomy) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cherrot.govproject.model.TermTaxonomy[ id=" + id + " ]";
    }

}
