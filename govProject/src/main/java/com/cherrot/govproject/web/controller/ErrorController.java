/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Cherrot Luo<cherrot+dev@cherrot.com>
 */
@Controller
@RequestMapping("/errors")
public class ErrorController {

    @RequestMapping("/{errorCode}")
    public String handleError(@PathVariable("errorCode")Integer errorCode) {
        return "/errors/" + errorCode;
    }
}
