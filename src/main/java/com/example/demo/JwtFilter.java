package com.example.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.entities.User;
import com.example.demo.repositories.UsersRepository;
import com.example.demo.utils.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtFilter extends OncePerRequestFilter {
	
	@Autowired
	JwtUtil jwtUtil;
	@Autowired
	UsersRepository usersRepo;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authHeader=request.getHeader("Authorization");

		if(authHeader!=null && authHeader.startsWith("Bearer ")) {
			try {
				if(jwtUtil.validateToken(authHeader.substring(7))) {
						User user=usersRepo.findById(Integer.parseInt(jwtUtil.getIdFromToken(authHeader.substring(7)))).get();
						Collection<GrantedAuthority> authorities=new ArrayList<>();
						authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
						UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(user, null,authorities);
						SecurityContextHolder.getContext().setAuthentication(authToken);
						request.setAttribute("user", user);
				}else {
					SecurityContextHolder.getContext().setAuthentication(null);
				}
			}catch(NoSuchElementException e) {
				SecurityContextHolder.getContext().setAuthentication(null);
			}catch(ExpiredJwtException| UnsupportedJwtException| MalformedJwtException| SignatureException e ) {
				SecurityContextHolder.getContext().setAuthentication(null);
			}
		}else {
			SecurityContextHolder.getContext().setAuthentication(null);
		}
		filterChain.doFilter(request, response);
	}

}
