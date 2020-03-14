package ukma.project.fifam.interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContext;
import ukma.project.fifam.models.User;
import ukma.project.fifam.repos.UserRepo;
import ukma.project.fifam.utils.JwtUtil;

import java.util.Enumeration;
import java.util.Optional;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Autowired
    private UserRepo userRepo;

    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equals("OPTIONS")) return true;
        String header = request.getHeader("Authorization");
        if (header == null || header.length() == 0){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Auth header is missing");
            return false;
        }
        header = header.replace("Bearer ", "");
        String s = new JwtUtil().getUserIdFromToken(header);
        Long userId = Long.parseLong(new JwtUtil().getUserIdFromToken(header));
        Optional<User> user = userRepo.findById(userId);
        if (!user.isPresent()){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User not found");
            return false;
        }
        request.setAttribute("userId", user.get().getId());
        return true;
    }

    @Override
    public void postHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception exception) throws Exception {
    }
}
