package Abby.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import Abby.demo.interceptor.AlphaInterceptor;

@Configuration
public class WebMVCConfig implements WebMvcConfigurer {
	
	@Autowired AlphaInterceptor alphaInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(alphaInterceptor)
			.excludePathPatterns("/**/*.css",	// static/**/*.css
							     "/**/*.js",
							     "/**/*.png",
							     "/**/*.jpg",
							     "/**/*.jpeg")
			.addPathPatterns("/register","/login"); 
	}
	
	
	

}