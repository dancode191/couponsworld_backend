package example.Coupon_Project_3.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;

@Component
@Order(2)
public class TokenFilter extends OncePerRequestFilter{

	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		try {
			//we take the token part from the header authorization that come in the request from client side
			String token = request.getHeader("authorization").split(" ")[1];
			// we check if token valid by decoding it
			JWT.decode(token);
			// we move to the next filter if there is one, if not we go to controllers 
			filterChain.doFilter(request, response);
			
			
		}catch(Exception e) {
			response.setStatus(401);
			response.getWriter().println("invalid credential !!!!");
		}
		
	}
	
	//here we set what requests dont need a token check
	// we add /auth to the list, cause at the time of login we dont have token yet
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException{
		String path = request.getRequestURI();
		return path.startsWith("/auth") || path.startsWith("/admin") || path.endsWith("/verify") || path.contains("/signUp");
	}
	
	
}
