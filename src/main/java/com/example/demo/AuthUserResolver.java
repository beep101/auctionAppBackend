package com.example.demo;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.example.demo.entities.User;
import com.example.demo.exceptions.UnauthenticatedException;

@Component
public class AuthUserResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		
		if(parameter.getParameterAnnotation(AuthUser.class)!=null&&parameter.getParameterType().equals(User.class)) {
			return true;
		}
		return false;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		try {
			return (User)webRequest.getAttribute("user",RequestAttributes.SCOPE_REQUEST);
		}catch(ClassCastException ex) {
			throw new UnauthenticatedException();
		}
	}

}
