package com.tensquare.user.service;

import com.tensquare.user.dao.UserDao;
import com.tensquare.user.pojo.User;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import utils.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 服务层
 * 
 * @author Administrator
 *
 */
@Service
public class UserService {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private IdWorker idWorker;

	@Autowired
	private BCryptPasswordEncoder encoder;

	/**
	 * 查询全部列表
	 * @return
	 */
	public List<User> findAll() {
		return userDao.findAll();
	}

	
	/**
	 * 条件查询+分页
	 * @param whereMap
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<User> findSearch(Map whereMap, int page, int size) {
		Specification<User> specification = createSpecification(whereMap);
		PageRequest pageRequest =  PageRequest.of(page-1, size);
		return userDao.findAll(specification, pageRequest);
	}

	
	/**
	 * 条件查询
	 * @param whereMap
	 * @return
	 */
	public List<User> findSearch(Map whereMap) {
		Specification<User> specification = createSpecification(whereMap);
		return userDao.findAll(specification);
	}

	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	public User findById(String id) {
		return userDao.findById(id).get();
	}

	/**
	 * 增加
	 * @param user
	 */
	public void add(User user) {
		user.setId( idWorker.nextId()+"" );
		// 密码加密
		String md5Password = encoder.encode(user.getPassword());
		// 把加密后的给user赋值
		user.setPassword(md5Password);
		userDao.save(user);
	}

	/**
	 * 修改
	 * @param user
	 */
	public void update(User user) {
		userDao.save(user);
	}

	/**
	 * 删除
	 * @param id
	 */
	public void deleteById(String id) {
		userDao.deleteById(id);
	}

	/**
	 * 动态条件构建
	 * @param searchMap
	 * @return
	 */
	private Specification<User> createSpecification(Map searchMap) {

		return new Specification<User>() {

			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
                // ID
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                	predicateList.add(cb.like(root.get("id").as(String.class), "%"+(String)searchMap.get("id")+"%"));
                }
                // 手机号码
                if (searchMap.get("mobile")!=null && !"".equals(searchMap.get("mobile"))) {
                	predicateList.add(cb.like(root.get("mobile").as(String.class), "%"+(String)searchMap.get("mobile")+"%"));
                }
                // 密码
                if (searchMap.get("password")!=null && !"".equals(searchMap.get("password"))) {
                	predicateList.add(cb.like(root.get("password").as(String.class), "%"+(String)searchMap.get("password")+"%"));
                }
                // 昵称
                if (searchMap.get("nickname")!=null && !"".equals(searchMap.get("nickname"))) {
                	predicateList.add(cb.like(root.get("nickname").as(String.class), "%"+(String)searchMap.get("nickname")+"%"));
                }
                // 性别
                if (searchMap.get("sex")!=null && !"".equals(searchMap.get("sex"))) {
                	predicateList.add(cb.like(root.get("sex").as(String.class), "%"+(String)searchMap.get("sex")+"%"));
                }
                // 头像
                if (searchMap.get("avatar")!=null && !"".equals(searchMap.get("avatar"))) {
                	predicateList.add(cb.like(root.get("avatar").as(String.class), "%"+(String)searchMap.get("avatar")+"%"));
                }
                // E-Mail
                if (searchMap.get("email")!=null && !"".equals(searchMap.get("email"))) {
                	predicateList.add(cb.like(root.get("email").as(String.class), "%"+(String)searchMap.get("email")+"%"));
                }
                // 兴趣
                if (searchMap.get("interest")!=null && !"".equals(searchMap.get("interest"))) {
                	predicateList.add(cb.like(root.get("interest").as(String.class), "%"+(String)searchMap.get("interest")+"%"));
                }
                // 个性
                if (searchMap.get("personality")!=null && !"".equals(searchMap.get("personality"))) {
                	predicateList.add(cb.like(root.get("personality").as(String.class), "%"+(String)searchMap.get("personality")+"%"));
                }
				
				return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));

			}
		};

	}

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private RedisTemplate redisTemplate;

	/**
	 * 发送验证码：就是验证码写入消息队列中
	 * @param mobile  手机号
	 */
	public void sendSms(String mobile) {
		// 生成一个6位随机数（100000~999999）
		Random random = new Random();
		Integer code = random.nextInt(999999);
		// 保证了code最多只能是99999
		if (code < 100000) {
			// 当加完之后，最大数只能是199999
			code = code + 100000;
		}
		// 需要将手机和验证码写入 rabbit 队列中
		HashMap<String, String> map = new HashMap<>();
		map.put("mobile",mobile);
		map.put("code",code.toString());  // 上面吧int改成了Integer 改变方便
		rabbitTemplate.convertAndSend("sms",map);
		// 还需要把 手机号和验证码存到redis中,并且十分钟过期
		redisTemplate.opsForValue().set("sms_"+mobile,code.toString(),10, TimeUnit.MINUTES);
		/// == 方便后面使用
		System.out.println(code);
	}

	/**
	 * 用户注册
	 *   注意：校验验证码操作交给控制器来做
	 *   完善用户信息
	 *   实现用户保存
	 */
	public void register(User user,String code){
		// 需要从redis 中获取验证码
		// 知识点：redisTemplate.opsForValue().get("sms_" + user.getMobile()).toString();
		// 不要toString() 因为万一取得对象为空就造成麻烦了
		/*String sysCode = (String) redisTemplate.opsForValue().get("sms_" + user.getMobile());
		// 判断验证码是否为空，或者过期，以及手机号输入有误
		if (StringUtils.isEmpty(sysCode)) {
			throw new RuntimeException("手机号有误或者验证码已经过期！");
		}
		// 比较验证码是否一致
		if (!sysCode.equals(code)) {
			throw new RuntimeException("验证码有误，请确认后在注册！");
		}*/
		// 添加注册信息
		user.setId(String.valueOf(idWorker.nextId()));
		user.setFollowcount(0); // 关注数
		user.setFanscount(0); // 粉丝数
		user.setOnline(0L); // 在线时长
		user.setRegdate(new Date()); // 注册日期
		user.setUpdatedate(new Date()); // 更新日期
		user.setLastdate(new Date()); // 最后登录的日期

		// 密码加密
		String md5Password = encoder.encode(user.getPassword());
		user.setPassword(md5Password);
		// 注册 --- 保存
		userDao.save(user);
	}

	/**
	 * 用户登录
	 * @param mobile
	 * @param password
	 * @return
	 */
	public User userLogin(String mobile,String password) {
		// 根据手机号去查询用户
		User user = userDao.findByMobile(mobile);
		// 判断是否由此用户
		if (user == null) {
			throw new RuntimeException("用户不存在");
		}
		// 校验密码
		boolean check = encoder.matches(password,user.getPassword());
		if (!check) {
			throw new RuntimeException("登录密码错误");
		}
		return user;
	}

}
