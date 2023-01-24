package web.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import web.security.PersonDetails;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class OneSessionFilterImpl implements Filter, OneSessionFilter {
    private final Map<Long,String> personSessions = new HashMap<>();

    @Override
    public void updateDetails(Long personId, String sessionId) {
        personSessions.put(personId, sessionId);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("===================================");
        System.out.println(authentication);
        System.out.println(personSessions);
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            Long personId = ((PersonDetails) authentication.getPrincipal()).getPerson().getId();
            String sessionId = ((WebAuthenticationDetails) authentication.getDetails()).getSessionId();
            if (!personSessions.get(personId).equals(sessionId)) {
                failureHandler().onAuthenticationFailure(
                        (HttpServletRequest) request,
                        (HttpServletResponse) response,
                        new BadCredentialsException("user " + authentication.getName() + " is already authenticated")
                );
                return;
            }
        }
        chain.doFilter(request, response);
    }

    public abstract AuthenticationFailureHandler failureHandler();
}
