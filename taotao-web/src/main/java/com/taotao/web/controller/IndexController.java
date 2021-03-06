package com.taotao.web.controller;

import com.taotao.web.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by yangdongan on 2017/6/6 0006.
 */
@Controller
public class IndexController {
    @Autowired
    private IndexService indexService;


    @RequestMapping(value = "index",method = RequestMethod.GET)
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView("index");

        //将广告数据封装到modelAndView里面
        //首页大广告
        modelAndView.addObject("indexAD1",indexService.queryIndexAd1());
        //首页右上角小广告
        modelAndView.addObject("indexAD2",indexService.queryIndexAd2());

        return modelAndView;
    }
}