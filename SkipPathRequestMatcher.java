package th.co.ncr.connector.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j

public class SkipPathRequestMatcher implements RequestMatcher {  
    private OrRequestMatcher matchers;
    private RequestMatcher processingMatcher;

    public SkipPathRequestMatcher(List<String> pathsToSkip, String processingPath) {
        List<RequestMatcher> m = pathsToSkip.
        		stream().
        			map(path -> new AntPathRequestMatcher(path)).
        				collect(Collectors.toList());
        matchers = new OrRequestMatcher(m);
        processingMatcher = new AntPathRequestMatcher(processingPath);
    }

	
	@Override
	public boolean matches(HttpServletRequest request) {
		Boolean matchExceptionalPath = matchers.matches(request);
		if(matchExceptionalPath) {
			log.trace("request uri: "+request.getRequestURI()+" is match exceptional path: "+matchExceptionalPath);
            return false;
		}
		Boolean matchProcessPath = processingMatcher.matches(request) ? true : false;
    	log.trace("request uri: "+request.getRequestURI()+" is match processing path: "+matchProcessPath);
        return matchProcessPath;
	}


}
