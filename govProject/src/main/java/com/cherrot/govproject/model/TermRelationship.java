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
    @NamedQuery(name = "TermRelationship.findAll", query = "SELECT t FROM TermRelationship t"),
    @NamedQuery(name = "TermRelationship.findByObjectId", query = "SELECT t FROM TermRelationship t WHERE t.termRelationshipPK.objectId = :objectId"),
    @NamedQuery(name = "TermRelationship.findByTaxonomyId", query = "SELECT t FROM TermRelationship t WHERE t.termRelationshipPK.taxonomyId = :taxonomyId"),
    @NamedQuery(name = "TermRelationship.findByTermOrder", query = "SELECT t FROM TermRelationship t WHERE t.termOrder = :termOrder")})
public class TermRelationship implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TermRelationshipPK termRelationshipPK;
    private Integer termOrder;
    @JoinColumn(name = "taxonomy_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private TermTaxonomy termTaxonomy;
    @JoinColumn(name = "object_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Post post;
    @JoinColumn(name = "object_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Link link;

    public TermRelationship() {
    }

    public TermRelationship(TermRelationshipPK termRelationshipPK) {
        this.termRelationshipPK = termRelationshipPK;
    }

    public TermRelationship(int objectId, int taxonomyId) {
        this.termRelationshipPK = new TermRelationshipPK(objectId, taxonomyId);
    }

    public TermRelationshipPK getTermRelationshipPK() {
        return termRelationshipPK;
    }

    public void setTermRelationshipPK(TermRelationshipPK termRelationshipPK) {
        this.termRelationshipPK = termRelationshipPK;
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

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Link getLink() {
        return link;
    }

    public void setLink(Link link) {
        this.link = link;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (termRelationshipPK != null ? termRelationshipPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TermRelationship)) {
            return false;
        }
        TermRelationship other = (TermRelationship) object;
        if ((this.termRelationshipPK == null && other.termRelationshipPK != null) || (this.termRelationshipPK != null && !this.termRelationshipPK.equals(other.termRelationshipPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cherrot.govproject.model.TermRelationship[ termRelationshipPK=" + termRelationshipPK + " ]";
    }

}
