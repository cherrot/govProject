/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author cherrot
 */
@Controller
public class HomeController {

    @RequestMapping("/")
    public String home() {
        return "home";
    }
}
