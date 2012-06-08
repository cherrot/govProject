/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author Cherrot Luo<cherrot+dev@cherrot.com>
 */
@Controller
@RequestMapping("/errors")
public class ErrorController {

    @RequestMapping("/404")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handle404() {
        return "/errors/404";
    }

    @RequestMapping("/410")
    @ResponseStatus(HttpStatus.GONE)
    public String handle410() {
        return "/errors/410";
    }
}
