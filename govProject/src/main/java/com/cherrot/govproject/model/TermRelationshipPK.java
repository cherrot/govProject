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
public class TermRelationshipPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "object_id", nullable = false)
    private int objectId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "term_id", nullable = false)
    private int termId;

    public TermRelationshipPK() {
    }

    public TermRelationshipPK(int objectId, int termId) {
        this.objectId = objectId;
        this.termId = termId;
    }

    public int getObjectId() {
        return objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    public int getTermId() {
        return termId;
    }

    public void setTermId(int termId) {
        this.termId = termId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) objectId;
        hash += (int) termId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!( object instanceof TermRelationshipPK )) {
            return false;
        }
        TermRelationshipPK other = (TermRelationshipPK) object;
        if (this.objectId != other.objectId) {
            return false;
        }
        if (this.termId != other.termId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cherrot.govproject.model.TermRelationshipPK[ objectId=" + objectId + ", termId=" + termId + " ]";
    }

}
