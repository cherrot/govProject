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
 * @author sai
 */
@Embeddable
public class TermRelationshipPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "post_id", nullable = false)
    private int postId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "term_id", nullable = false)
    private int termId;

    public TermRelationshipPK() {
    }

    public TermRelationshipPK(int postId, int termId) {
        this.postId = postId;
        this.termId = termId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
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
        hash += (int) postId;
        hash += (int) termId;
        return hash;
    }

    /**
     * Warning - this method won't work in the case the id fields are not set
     * @param object
     * @return
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TermRelationshipPK)) {
            return false;
        }
        TermRelationshipPK other = (TermRelationshipPK) object;
        if (this.postId != other.postId) {
            return false;
        }
        if (this.termId != other.termId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cherrot.govproject.model.TermRelationshipPK[ postId=" + postId + ", termId=" + termId + " ]";
    }

}
