package br.com.agenciacodeplus.socialcron.helpers;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class HttpHeadersHelper {
	
	private UriComponentsBuilder uriBuilder;
	
	public HttpHeadersHelper() {
		uriBuilder = UriComponentsBuilder.newInstance();
	}
	
	public HttpHeaders addLocationHeader(String uri, Object id) {
		UriComponents uriComponents = uriBuilder.path(uri + "/{id}").buildAndExpand(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uriComponents.toUri());
        return headers;
	}
	
}
