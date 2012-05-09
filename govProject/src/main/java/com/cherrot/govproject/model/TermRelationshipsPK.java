/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author cherrot
 */
@Embeddable
public class TermRelationshipsPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "object_id")
    private int objectId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "taxonomy_id")
    private int taxonomyId;

    public TermRelationshipsPK() {
    }

    public TermRelationshipsPK(int objectId, int taxonomyId) {
        this.objectId = objectId;
        this.taxonomyId = taxonomyId;
    }

    public int getObjectId() {
        return objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    public int getTaxonomyId() {
        return taxonomyId;
    }

    public void setTaxonomyId(int taxonomyId) {
        this.taxonomyId = taxonomyId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) objectId;
        hash += (int) taxonomyId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TermRelationshipsPK)) {
            return false;
        }
        TermRelationshipsPK other = (TermRelationshipsPK) object;
        if (this.objectId != other.objectId) {
            return false;
        }
        if (this.taxonomyId != other.taxonomyId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cherrot.govproject.model.TermRelationshipsPK[ objectId=" + objectId + ", taxonomyId=" + taxonomyId + " ]";
    }

}
