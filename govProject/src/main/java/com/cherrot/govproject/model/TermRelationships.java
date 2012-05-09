/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.model;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author cherrot
 */
@Entity
@Table(name = "term_relationships")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TermRelationships.findAll", query = "SELECT t FROM TermRelationships t"),
    @NamedQuery(name = "TermRelationships.findByObjectId", query = "SELECT t FROM TermRelationships t WHERE t.termRelationshipsPK.objectId = :objectId"),
    @NamedQuery(name = "TermRelationships.findByTaxonomyId", query = "SELECT t FROM TermRelationships t WHERE t.termRelationshipsPK.taxonomyId = :taxonomyId"),
    @NamedQuery(name = "TermRelationships.findByTermOrder", query = "SELECT t FROM TermRelationships t WHERE t.termOrder = :termOrder")})
public class TermRelationships implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TermRelationshipsPK termRelationshipsPK;
    private Integer termOrder;
    @JoinColumn(name = "taxonomy_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private TermTaxonomy termTaxonomy;
    @JoinColumn(name = "object_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Posts posts;
    @JoinColumn(name = "object_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Links links;

    public TermRelationships() {
    }

    public TermRelationships(TermRelationshipsPK termRelationshipsPK) {
        this.termRelationshipsPK = termRelationshipsPK;
    }

    public TermRelationships(int objectId, int taxonomyId) {
        this.termRelationshipsPK = new TermRelationshipsPK(objectId, taxonomyId);
    }

    public TermRelationshipsPK getTermRelationshipsPK() {
        return termRelationshipsPK;
    }

    public void setTermRelationshipsPK(TermRelationshipsPK termRelationshipsPK) {
        this.termRelationshipsPK = termRelationshipsPK;
    }

    public Integer getTermOrder() {
        return termOrder;
    }

    public void setTermOrder(Integer termOrder) {
        this.termOrder = termOrder;
    }

    public TermTaxonomy getTermTaxonomy() {
        return termTaxonomy;
    }

    public void setTermTaxonomy(TermTaxonomy termTaxonomy) {
        this.termTaxonomy = termTaxonomy;
    }

    public Posts getPosts() {
        return posts;
    }

    public void setPosts(Posts posts) {
        this.posts = posts;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (termRelationshipsPK != null ? termRelationshipsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TermRelationships)) {
            return false;
        }
        TermRelationships other = (TermRelationships) object;
        if ((this.termRelationshipsPK == null && other.termRelationshipsPK != null) || (this.termRelationshipsPK != null && !this.termRelationshipsPK.equals(other.termRelationshipsPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cherrot.govproject.model.TermRelationships[ termRelationshipsPK=" + termRelationshipsPK + " ]";
    }

}
