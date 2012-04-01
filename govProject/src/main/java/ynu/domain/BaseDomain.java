/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ynu.domain;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;
/**
 *
 * @author cherrot
 * 实现了Serializable接口,以便JVM可以序列化PO实例
 */
public class BaseDomain implements Serializable {
    /**
     * 统一的toString()方法
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
