package Abby.demo.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import Abby.demo.Annotation.LoginRequire;
import Abby.demo.entity.User;
import Abby.demo.service.LikeService;
import Abby.demo.service.UserService;
import Abby.demo.util.HostHolder;
import Abby.demo.util.demoUtil;
@Controller
@RequestMapping("/user")
public class UserController {
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Value("${demo.path.upload}")
	private String uploadPath;
	
	@Value("${demo.path.domain}")
	private String domain;
	
	@Value("${server.servlet.context-path}")
	private String ContextPath;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private HostHolder hostHolder;
	
	@Autowired
	private LikeService likeService;
	
	@RequestMapping(path="/setting", method=RequestMethod.GET)
	@LoginRequire
	public String getSetting() {
		return "/site/setting";
	}
	
	@LoginRequire
	@RequestMapping(path="/upload",method = RequestMethod.POST)
	public String uploadHeader(MultipartFile headerImage, Model model) {   // 如果有多个文件
		if (headerImage==null) {
			model.addAttribute("error", "找不到图片！");
			return "/site/setting";
		}
		String fileName = headerImage.getOriginalFilename();
		String suffix = fileName.substring(fileName.lastIndexOf("."));
		if (StringUtils.isBlank(suffix)) {
			model.addAttribute("error", "文件格式不正确！");
			return "/site/setting";
		}
		
		// 生成随机文件名
		fileName = demoUtil.genUUID()+suffix;
		File destFile = new File(uploadPath+"/"+fileName);
		try {
			// 储存文件
			headerImage.transferTo(destFile);
			
			// 更新headerUrl （web访问路径）
			// http://localhost:8080/   
			// /community/user/header/xxx.png
			User user = hostHolder.getUser();
			String headerUrl = domain+ ContextPath+"/user/header/"+fileName;
			userService.updateHeader(user.getId(), headerUrl);
			
			return "redirect:/index";
			
			
		}  catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("文件上传出错");
			throw new RuntimeException("服务器文件上传异常",e);					
		}
		
	}
	
	// 包装一下变成web数据
	// 需要手动输出数据
	@RequestMapping(path="/header/{fileName}",method = RequestMethod.GET)
	public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response) {
		// 服务器存放路径
		fileName = uploadPath+"/"+fileName;
		String suffix = fileName.substring(fileName.lastIndexOf(".")+1);
		
		// 响应图片
		response.setContentType("image/"+suffix);
		try (
				FileInputStream fis = new FileInputStream(fileName);
				OutputStream os = response.getOutputStream();
			){
			byte[] buffer = new byte[1024];
			int b = 0;
			while((b=fis.read(buffer)) != -1) {
				os.write(buffer,0,b);
			}
			
		} catch (IOException e) {
			logger.error("读取头像失败"+e.getMessage());
		}	
	}
	
	@RequestMapping(path = "/profile/{userId}",method = RequestMethod.GET)
	public String getProfilePage(@PathVariable("userId") int userId, Model model) {
		User user = userService.findById(userId);
		if (user==null) {
			throw new RuntimeException("用户不存在");
		}
		
		int likeCount = likeService.findUserLikeCount(userId);
		model.addAttribute("user", user);
		model.addAttribute("likeCount", likeCount);
		
		return "site/profile";
	}

}
