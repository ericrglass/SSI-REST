/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ssi_rest.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.github.ssi_rest.json.TwoDecimalSerializer;

/**
 *
 * @author The Glass Family
 */
@Entity
@Table(name = "DISCOUNT_CODE")
@NamedQueries({
    @NamedQuery(name = DiscountCode.FIND_ALL, query = "SELECT d FROM DiscountCode d"),
    @NamedQuery(name = DiscountCode.FIND_BY_DISCOUNT_CODE, query = "SELECT d FROM DiscountCode d WHERE d.discountCode = :discountCode"),
    @NamedQuery(name = DiscountCode.FIND_BY_RATE, query = "SELECT d FROM DiscountCode d WHERE d.rate = :rate")})
@JsonAutoDetect
public class DiscountCode implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String FIND_ALL = "DiscountCode.findAll";
    public static final String FIND_BY_DISCOUNT_CODE = "DiscountCode.findByDiscountCode";
    public static final String FIND_BY_RATE = "DiscountCode.findByRate";
    @Id
    @Basic(optional = false)
    @Column(name = "DISCOUNT_CODE", nullable = false)
    private Character discountCode;
    @JsonSerialize(using = TwoDecimalSerializer.class)
	private BigDecimal rate;

    public DiscountCode() {
    }

    public DiscountCode(Character discountCode) {
        this.discountCode = discountCode;
    }

    public Character getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(Character discountCode) {
        this.discountCode = discountCode;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (discountCode != null ? discountCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DiscountCode)) {
            return false;
        }
        DiscountCode other = (DiscountCode) object;
        if ((this.discountCode == null && other.discountCode != null) || (this.discountCode != null && !this.discountCode.equals(other.discountCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.github.restmvc.model.DiscountCode[ discountCode=" + discountCode + " ]";
    }

}
