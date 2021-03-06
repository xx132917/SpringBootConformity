package com.xx.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xx.pojo.JSONResult;
import com.xx.pojo.User;
import com.xx.service.impl.UserServiceImpl;
import com.xx.utils.JsonUtils;
import com.xx.utils.RedisOperator;

@RestController   //@RestController =@Controller+@ResponseBody
@RequestMapping("/user")
public class UserController {
	
	@Resource
	private UserServiceImpl userService;
	
	@Autowired
    private StringRedisTemplate redisTemplate;
	
	@Autowired
	private RedisOperator redis;
	
	String string = "2016-10-24 21:59:06";
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
	
	@RequestMapping("/getuser")
	public JSONResult getuser() throws ParseException {	
		User user=new User();
		user.setName(null);
		user.setPassword("123");
		user.setBirthday(string);
		return JSONResult.ok(user);
	}
	
	@RequestMapping("/all")
	public JSONResult getAllUser() {
		for(User user:userService.findAll()) {
			System.out.println(user.getName());
		}
		
		return JSONResult.ok(userService.findAll());
	}
	
	@RequestMapping("/add")
	public JSONResult add() {
		User user=new User();
		user.setName("222");
		user.setPassword("222");
		user.setAge(22);
		user.setBirthday("2018-01-01 00:00:00");
		user.setDescation("222");
		
		userService.add(user);
		return JSONResult.ok();
	}
	
	@RequestMapping("/update")
	public JSONResult update(){
		User user=new User();
		user.setId(2);
		user.setName("2223");
		user.setPassword("2223");
		user.setAge(22);
		user.setBirthday("2018-02-02 00:00:00");
		user.setDescation("2223");
		
		userService.update(user);
		return JSONResult.ok();
	}
	
	@RequestMapping("/findbyName")
	public JSONResult findbyName(){
		User user=new User();
		user.setName("2223");
		
		System.out.println(userService.findbyName(user).getBirthday());
		return JSONResult.ok();
	}
	
	@RequestMapping("/delete")
	public JSONResult delete(){
		User user=new User();
		user.setId(2);

		userService.delete(user);
		return JSONResult.ok();
	}
	
	//分頁
	@GetMapping("/pageuser")
	public JSONResult pageuser(Integer page,Integer pageSize) {		
		return JSONResult.ok(userService.pageuser(page, pageSize));
	}
	
	//redis
	@GetMapping("/redis")
	public JSONResult redisTest() {
		redisTemplate.opsForValue().set("user-cache", "你好，浪哥");
		User user=new User();
		user.setName("222");
		user.setPassword("222");
		user.setAge(22);
		user.setBirthday("2018-01-01 00:00:00");
		user.setDescation("222");
		redisTemplate.opsForValue().set("json:user", JsonUtils.objectToJson(user));
		User u=JsonUtils.jsonToPojo(redisTemplate.opsForValue().get("json:user"), User.class);
		return JSONResult.ok(u);
	}
	
	@GetMapping("/getredis")
	public JSONResult getredis() {
        
		User user = new User();
		user.setAge(18);
		user.setName("慕课网");
		user.setPassword("123456");
		user.setBirthday("123");
		
		User u1 = new User();
		u1.setAge(19);
		u1.setName("imooc");
		u1.setPassword("123456");
		u1.setBirthday("123");
		
		User u2 = new User();
		u2.setAge(17);
		u2.setName("hello imooc");
		u2.setPassword("123456");
		u2.setBirthday("123");
		
		List<User> userList = new ArrayList<>();
		userList.add(user);
		userList.add(u1);
		userList.add(u2);
		
		redis.set("json:info:userlist", JsonUtils.objectToJson(userList), 2000);
		
		String userListJson = redis.get("json:info:userlist");
		List<User> userListBorn = JsonUtils.jsonToList(userListJson, User.class);
		return JSONResult.ok(userListBorn);
	}
}
