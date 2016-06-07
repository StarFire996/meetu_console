package com.meetu.console.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("page")
@Controller
public class PageController {

    /**
     * 通用文件夹页面跳转
     * 
     * @param pageName
     * @return
     */
    @RequestMapping(value = "{pathName}/{pageName}", method = RequestMethod.GET)
    public String toPathPage(@PathVariable("pathName")String pathName,@PathVariable("pageName") String pageName) {
        return pathName+"/"+pageName;
    }
    
    /**
     * 通用文件夹页面跳转
     * 
     * @param pageName
     * @return
     */
    @RequestMapping(value = "rest/{pageName}", method = RequestMethod.GET)
    public String toPage(@PathVariable("pageName") String pageName) {
        return pageName;
    }

}
