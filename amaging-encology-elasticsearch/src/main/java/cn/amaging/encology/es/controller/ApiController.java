package cn.amaging.encology.es.controller;

import cn.amaging.encology.es.model.User;
import cn.amaging.encology.es.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by DuQiyu on 2018/6/14.
 */
@RestController
@RequestMapping("/api/v1")
public class ApiController {

    @Resource
    private UserService userService;

    @RequestMapping(value = "/fullSearch", method = RequestMethod.GET)
    public Iterable<User> fullSearch(@RequestParam Map<String, String> params) {
        return userService.fullSearch(params);
    }

    @RequestMapping(value = "/conditionSearch", method = RequestMethod.GET)
    public Iterable<User> conditionSearch(@RequestParam Map<String, String> params) {
        return userService.conditionSearch(params);
    }

    @RequestMapping(value = "/uniqueSearch/{uid}", method = RequestMethod.GET)
    public Iterable<User> uniqueSearch(@PathVariable String uid) {
        return userService.uniqueSearch(uid);
    }

}
