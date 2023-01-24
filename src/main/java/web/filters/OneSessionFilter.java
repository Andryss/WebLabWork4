package web.filters;

import jakarta.servlet.Filter;

public interface OneSessionFilter extends Filter {
    void updateDetails(Long personId, String sessionId);
}
