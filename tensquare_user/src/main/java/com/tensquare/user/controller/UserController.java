package com.tensquare.user.controller;

import com.tensquare.user.pojo.User;
import com.tensquare.user.service.UserService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import utils.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
/**
 * 控制器层
 * @author Administrator
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	
	/**
	 * 查询全部数据
	 * @return
	 */
	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){
		return new Result(true,StatusCode.OK,"查询成功",userService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"查询成功",userService.findById(id));
	}


	/**
	 * 分页+多条件查询
	 * @param searchMap 查询条件封装
	 * @param page 页码
	 * @param size 页大小
	 * @return 分页结果
	 */
	@RequestMapping(value="/search/{page}/{size}",method=RequestMethod.POST)
	public Result findSearch(@RequestBody Map searchMap , @PathVariable int page, @PathVariable int size){
		Page<User> pageList = userService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<User>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",userService.findSearch(searchMap));
    }
	
	/**
	 * 增加
	 * @param user
	 */
	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody User user  ){
		userService.add(user);
		return new Result(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param user
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody User user, @PathVariable String id ){
		user.setId(id);
		userService.update(user);		
		return new Result(true,StatusCode.OK,"修改成功");
	}

	/**
	 * 删除
	 * @param id
	 *
	 * 思路：
	 * 		把验证信息头的相关的操作都挪到拦截器中取，此处只需要判断是管理员权限还是普通用户权限
	 * */
	 @RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	 public Result delete(@PathVariable String id, HttpServletRequest request){
		 // 获取请求域中的权限：要求管理员权限才能删除，所有获取只需要管理员权限还是普通用户权限
		 Claims claims = (Claims) request.getAttribute("admin_claims");
		 if (claims == null) {
		 	//没有管理员权限
			 return new Result(false,StatusCode.ACCESSERROR,"没有权限");
		 }
		 userService.deleteById(id);
		 return new Result(true,StatusCode.OK,"删除成功");
	 }

	
	/**
	 * 删除
	 * @param id
	 *
	 * 取出token解析成的Claims,把Claims中roles的值取出来，是admin就删除，否则没有权限

	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id,@RequestHeader(value = "Authorization",required = false) String authHeader){
		//1.判断是否带了消息头
		if (StringUtils.isEmpty(authHeader)) {
			return new Result(false,StatusCode.ACCESSERROR,"没有权限");
		}
		//2.判断消息头的格式是否匹配
		String token = authHeader.split(" ")[1]; // {Bearer token}
		if (StringUtils.isEmpty(token)) {
			return new Result(false,StatusCode.ACCESSERROR,"没有权限");
		}
		//3.判断token是否是合法的
		Claims claims = jwtUtil.parseJWT(token);
		if (claims == null) {
			return new Result(false,StatusCode.ACCESSERROR,"没有权限");
		}
		//4.判断claims中的roles是不是admin
		if (!"admin".equals(claims.get("roles"))){
			return new Result(false,StatusCode.ACCESSERROR,"没有权限");
		}
		userService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}
	 */

	/**
	 * 发送短信验证码，不是真的发送，而是把手机号写入消息队列中
	 * @param mobile
	 * @return
	 */
	@RequestMapping(value = "/sendsms/{mobile}",method = RequestMethod.POST)
	public Result sendSms(@PathVariable("mobile") String mobile) {
		userService.sendSms(mobile);
		return new Result(true,StatusCode.OK,"发送成功");
	}

	/**
	 * 用户注册
	 * @param user
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/register/{code}",method = RequestMethod.POST)
	public Result register(@RequestBody User user,@PathVariable("code") String code) {
		userService.register(user,code);
		return new Result(true,StatusCode.OK,"注册成功");
	}

	@Autowired
	private JwtUtil jwtUtil;

	/**
	 * 用户登录
	 * @param user
	 */
	@RequestMapping(value = "/login",method=RequestMethod.POST)
	public Result userLogin(@RequestBody User user){
		User sysUser = userService.userLogin(user.getMobile(), user.getPassword());
		// 签发token
		String token = jwtUtil.createJWT(sysUser.getId(), sysUser.getNickname(), "user");
		// 创建用户信息和token的对应关系
		Map<String,String> map = new HashMap<>();
		map.put("name",sysUser.getNickname());
		map.put("token",token);
		map.put("avatar",sysUser.getAvatar());
		return new Result(true,StatusCode.OK,"登录成功",map);
	}

	/**
	 * 更新粉丝数
	 * @param useId
	 * @param fans
	 */
	@RequestMapping(value = "/incfans/{userid}/{fans}",method = RequestMethod.POST)
	public void incFanscount(@PathVariable("userid") String useId,@PathVariable("fans") int fans) {
		userService.incFanscount(useId,fans);
	}

	/**
	 * 更新关注数
	 * @param useId
	 * @param follow
	 */
	@RequestMapping(value = "/incfollow/{userid}/{follow}",method = RequestMethod.POST)
	public void incFollowconut(@PathVariable("userid") String useId,@PathVariable("follow") int follow) {
		userService.incFollowconut(useId,follow);
	}

}
