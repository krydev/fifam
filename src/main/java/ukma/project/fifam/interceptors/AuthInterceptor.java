package ukma.project.fifam.interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import ukma.project.fifam.models.User;
import ukma.project.fifam.repos.UserRepo;
import ukma.project.fifam.utils.JwtUtil;

import java.util.Enumeration;
import java.util.Optional;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String header = request.getHeader("Authorization");
        if (header.length() == 0){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Auth header is missing");
            return false;
        }
        String userId = jwtUtil.getUserIdFromToken(header);
        Optional<User> user = userRepo.findById(Long.parseLong(userId));
        if (!user.isPresent()){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User not found");
            return false;
        }
        return true;
        //request.getHeaders("User").
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
