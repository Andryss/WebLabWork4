package web.model;

import org.springframework.stereotype.Component;
import web.entities.Request;

// TODO: delete component
@Component
public class AreaCheckerImpl implements AreaChecker {
    @Override
    public boolean check(Request request) {
        double x = request.getX(),
                y = request.getY(),
                r = request.getR();
        return ((x >= 0 && y >= 0) && (x <= r && y <= r)) ||
                ((x <= 0 && y >= 0) && (y <= x + r/2)) ||
                ((x <= 0 && y <= 0) && (x * x + y * y <= r));
    }
}
