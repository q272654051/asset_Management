package com.manager.www.controller;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.manager.www.commons.util.JsonUtil;
import com.manager.www.commons.util.WebConstants;
import com.manager.www.service.IUserService;
import com.manager.www.vo.User;

/**
 * @author djx
 * @date 2015-11-19
 * @description
 */

@Controller
@RequestMapping(value="loginController")
public class LoginController {
    @Resource
    IUserService userService;
    
    @RequestMapping("/tologin")
    public void login(HttpServletRequest request, HttpServletResponse response,PrintWriter printWriter,User user){
        Map<String,Object> result_map = new HashMap<String,Object>();
        String username = request.getParameter("userName");
        String pwd = request.getParameter("pwd");
//        String username = user.getUserName();
//        String pwd = user.getPwd();
        try {
        		List<User> userlist = userService.findUserByName(username);
        		if (userlist.size()>0){
        			user = userlist.get(0);
        			if (user.getStatus()==1){
        				if (pwd.equals(user.getPwd())){
	                        result_map.put("success", true);
	                        result_map.put("msg", "登录成功");
	                        request.getSession().setAttribute("userId",user.getId()+"");         //存储登陆人id
	                        request.getSession().setMaxInactiveInterval(3600);
	                        WebConstants.setUser(request, user);
        				}else{
        					result_map.put("success", false);
        					result_map.put("msg", "密码错误，请重试");
        				}
                }else{
                    result_map.put("success", false);
                    result_map.put("msg", "该账户已被禁用,请联系管理员");
                }
            }else{
                result_map.put("success", false);
                result_map.put("msg", "没有该用户");
            }
        } catch (Exception e) {
            result_map.put("success", false);
            result_map.put("msg", "系统错误,请与管理员联系");
            e.printStackTrace();
        }finally{
            printWriter.print(JsonUtil.jsonObject(result_map, null, null));
            printWriter.flush();
            printWriter.close();
        }
    }
    
    @RequestMapping("/loginSuccess")
    public ModelAndView loginSuccess(HttpServletRequest request, HttpServletResponse response){
    	
        return new ModelAndView("index");
    }
    
}
