package de.akra.coronatestmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * The frontend is a "typical" single page application which does its own
 * routing, the task of this Spring server is to provide matching data. In
 * order to make the deployment as easy as possible ("run this jar" instead
 * of "please configure this nginx / apache / whatever") the spring server
 * comes with the compiled Angular application.
 *
 * This controller simply assumes that every request that does not contain
 * a dot will be redirected to the entry point of the frontend application.
 */
@Controller
public class SinglePageAppController {
    @RequestMapping(value = "/{path:[^\\.]*}")
    public String redirect() {
        return "forward:/";
    }
}