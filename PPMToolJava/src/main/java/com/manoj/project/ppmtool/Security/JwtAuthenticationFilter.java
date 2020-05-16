package com.manoj.project.ppmtool.Security;

import com.manoj.project.ppmtool.domain.User;
import com.manoj.project.ppmtool.services.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CustomUserDetailService userDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

        try{
            String jwt = getJwtTokenString(httpServletRequest);
            if(StringUtils.hasText(jwt) && jwtTokenProvider.tokenValidator(jwt)){
                Long userId = jwtTokenProvider.getUserIdFromJwt(jwt);
                User user = userDetailService.loadUserById(userId);

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken
                        (user,null,user.getAuthorities());

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            }
        }catch (Exception e){
            logger.error("Could not set user authentication in security context",e);
        }

        filterChain.doFilter(httpServletRequest,httpServletResponse);

    }

    private String getJwtTokenString(HttpServletRequest request){
        String token = request.getHeader(SecurityConstant.HEADER_STRING);
        if(StringUtils.hasText(token) && token.startsWith(SecurityConstant.TOKEN_PREFIX) ){
            return token.substring(7,token.length());
        }

        return null;
    }
}
