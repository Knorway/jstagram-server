package jstagram.server.config.security;

import org.springframework.core.MethodParameter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class JwtTokenArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isTargetAnnotation = parameter.getParameterAnnotation(JwtToken.class) != null;
        boolean isTokenUserClass = TokenUser.class.equals(parameter.getParameterType());
        return isTargetAnnotation && isTokenUserClass;
    }

    @Override
    public Object resolveArgument(
        MethodParameter parameter,
        ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest,
        WebDataBinderFactory binderFactory
    ) throws Exception {
        JwtAuthenticationToken userPrincipal = (JwtAuthenticationToken) webRequest.getUserPrincipal();
        return new TokenUser(userPrincipal.getName());
    }
}
