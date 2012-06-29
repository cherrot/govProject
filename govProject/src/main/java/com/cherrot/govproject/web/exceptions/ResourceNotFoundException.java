/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @ResponseStatus：
 * http://stackoverflow.com/questions/2066946/trigger-404-in-spring-mvc-controller
 * @ResponseStatus 还可以用在控制器方法中：
 * http://stackoverflow.com/questions/2238815/spring-mvc-3-respond-to-request-with-a-404?rq=1
 * @author Cherrot Luo<cherrot+dev@cherrot.com>
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
}
