package org.appfuse.api;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.junit.Test;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.junit.Assert.*;

public class UserApiITest {
    private final static Log log = LogFactory.getLog(UserApiITest.class);
    private RestTemplate restTemplate = new RestTemplate();

    @Test
    public void testGetUsers() {
        ResponseEntity<User[]> result = restTemplate.getForEntity(getUsersPath(), User[].class);
        HttpStatus status = result.getStatusCode();
        User[] users = result.getBody();

        log.debug("Users found: " + users.length);
        assertTrue(users.length > 0);
        assertEquals(HttpStatus.OK, status);
    }

    public URI getUsersPath() {
        String contextPath = "/" + System.getProperty("context.path", "");
        String host = System.getProperty("cargo.host", "localhost");
        String port = System.getProperty("cargo.port", "8080");
        return URI.create("http://" + host + ":" + port + contextPath + "/api/users.json");
    }
}