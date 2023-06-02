package example.Coupon_Project_3.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Order(1)
public class CorsFilter extends OncePerRequestFilter{

	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		response.setHeader("Access-Control-Allow-Origin", "*"); // we allow to everyone to send us requests
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Allow-Methods", "OPTIONS, GET, POST, PUT, DELETE, HEAD"); // what method we allow to use
		response.setHeader("Access-Control-Allow-Headers", "authorization, Origin, Accept, content-type, Access-Control-Request-Method, Access-Control-Request-Headers");//what headers can be sent by client
		
		//here we check if its the first request, if it is we send accept as response, if not we move to the TokenFilter to check client token
		if(request.getMethod().equals("OPTIONS"))
			response.setStatus(HttpServletResponse.SC_ACCEPTED);
		else 
			filterChain.doFilter(request, response);
	}

}
