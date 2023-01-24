package web.filters;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import web.security.PersonDetails;
import web.util.RequestMessageResponse;

import java.io.IOException;

public abstract class AuthSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        Object objToWrite;
        if (authentication.getPrincipal() instanceof UserDetails) {
            objToWrite = ((UserDetails) authentication.getPrincipal()).getAuthorities()
                    .stream().map(GrantedAuthority::getAuthority).toList();
        } else {
            objToWrite = new RequestMessageResponse("user " + authentication.getName() + " successfully authenticated");
        }
        writeJsonResponse(response, HttpStatus.OK.value(), objToWrite);

        Long personId = ((PersonDetails) authentication.getPrincipal()).getPerson().getId();
        String sessionId = ((WebAuthenticationDetails) authentication.getDetails()).getSessionId();
        oneSessionFilter().updateDetails(personId, sessionId);
    }

    public abstract OneSessionFilter oneSessionFilter();

    public abstract void writeJsonResponse(HttpServletResponse response, int status, Object obj) throws IOException;
}
