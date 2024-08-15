package com.example.shopit.security;

import com.example.shopit.dto.response.UserResponse;
import com.example.shopit.entity.User;
import com.example.shopit.repository.RepositoryAccessor;
import com.example.shopit.service.AuthService;
import com.example.shopit.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

public class AuthTokenFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthTokenFilter.class);
    @Autowired private JwtUtil jwtUtil;
    @Autowired private AuthService authService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, "Bearer ")) {
            filterChain.doFilter(request, response);
            LOGGER.error("----------Invalid token----------");
            return;
        }
        jwt = authHeader.substring(7);
        userEmail = jwtUtil.extractUserName(jwt);
        if (StringUtils.isNotEmpty(userEmail)
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = authService.loadUserByUsername(userEmail);
            UserResponse userDetailsResponseDto = jwtUtil.getUserAuthDetailsFromToken(jwt);
            Optional<User> userExists =
                    RepositoryAccessor.getUserRepository()
                            .findByIdAndIsActiveAndIsDeleted(userDetailsResponseDto.getId(), true, false);
            if (userExists.isPresent()) {
                User user = userExists.get();
                if (jwtUtil.validateToken(jwt)) {
                    UserResponse userDetailResponse =
                            UserResponse.builder()
                                    .id(user.getId())
                                    .email(user.getEmail())
                                    .build();
                    SecurityContext context = SecurityContextHolder.createEmptyContext();
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetailResponse, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    context.setAuthentication(authToken);
                    SecurityContextHolder.setContext(context);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}