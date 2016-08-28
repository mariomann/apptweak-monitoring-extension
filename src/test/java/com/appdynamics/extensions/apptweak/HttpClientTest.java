package com.appdynamics.extensions.apptweak;

import by.stub.server.StubbyManager;
import by.stub.server.StubbyManagerFactory;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.Header;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class HttpClientTest {

    HttpClient httpClient = new HttpClient();

    StubbyManagerFactory stubbyManagerFactory;
    StubbyManager stubbyManager;

    @Test
    public void getOnLocalhostReturns200() throws IOException {
        HttpClient.Response<String> actual = httpClient.get("http://localhost:8882/");

        assertThat(actual.getStatusCode(), is(200));
        assertThat(actual.getBody(), is(equalTo("Hello basc.io")));
    }

    @Test
    public void getWithParameterOnLocalhostReturns200() {
        Map<String, String> query = new HashMap<>();
        query.put("lol", "rofl");
        query.put("hehe", "hihi");
        HttpClient.Response<String> actual = httpClient.get("http://localhost:8882/", query);

        assertThat(actual.getStatusCode(), is(200));
        assertThat(actual.getBody(), is(equalTo("<lol>rofl</lol><hehe>hihi</hehe>")));
    }

    @Test
    public void postOnLocalhostReturns200() throws IOException {
        HttpClient.Response<String> actual = httpClient.post("http://localhost:8882/");

        assertThat(actual.getStatusCode(), is(200));
        assertThat(actual.getBody(), is(equalTo("Hello basc.io")));
    }

    @Test
    public void postWithBodyOnLocalhostReturns200() {
        Map<String, String> body = new HashMap<>();
        body.put("lol", "rofl");
        body.put("hehe", "hihi");
        HttpClient.Response<String> actual = httpClient.post("http://localhost:8882/lol", body);

        assertThat(actual.getStatusCode(), is(200));
        assertThat(actual.getBody(), is(equalTo("<lol>rofl</lol><hehe>hihi</hehe>")));
    }

    @Test
    public void getOnNonExistingSiteReturns404() {
        HttpClient.Response<String> actual = httpClient.get("http://localhost:8882/doesnotexist");

        assertThat(actual.getStatusCode(), is(404));
    }

    @Test
    public void getOnLocalhostReturns200WithBasicAuthenticationHeader() {
        Map<String, String> body = new HashMap<>();
        body.put("lol", "rofl");
        String username = "lol";
        String password = "rofl";
        String credentials = username + ":" + password;
        Header[] header = new Header[1];
        header[0] = new Header("Authorization", "Basic " + new String(Base64.encodeBase64(credentials.getBytes())));
        HttpClient.Response<String> actual = httpClient.get("http://localhost:8882/basic-auth", body, header);

        assertThat(actual.getStatusCode(), is(200));
        assertThat(actual.getBody(), is(equalTo("<auth>successfully authorized</auth>")));
    }

    @Test
    public void getOnLocalhostReturns401WithInvalidCredentialsInBasicAuthenticationHeader() {
        Map<String, String> body = new HashMap<>();
        body.put("lol", "rofl");
        String username = "l";
        String password = "r";
        String credentials = username + ":" + password;
        Header[] header = new Header[1];
        header[0] = new Header("Authorization", "Basic " + new String(Base64.encodeBase64(credentials.getBytes())));
        HttpClient.Response<String> actual = httpClient.get("http://localhost:8882/basic-auth", body, header);

        assertThat(actual.getStatusCode(), is(401));
        //assertThat(actual.getBody(), is(containsString("Unauthorized")));
    }

    @Test
    public void postOnNonExistingSiteReturns404() {
        HttpClient.Response<String> actual = httpClient.post("http://localhost:8882/doesnotexist");

        assertThat(actual.getStatusCode(), is(404));
    }

    @Test
    public void postOnLocalhostReturns303WithHeaderLocationInformation() {
        Map<String, String> body = new HashMap<>();
        body.put("lol", "rofl");
        HttpClient.Response<String> actual = httpClient.post("http://localhost:8882/redirection", body);

        assertThat(actual.getStatusCode(), is(303));
        assertThat(actual.getBody(), is(""));
        assertThat(Arrays.stream(actual.getHeaders()).filter(h -> h.getName().contains("Location")).findFirst().get().getValue(), is("http://localhost:3003"));
    }

    @Test
    public void postOnLocalhostReturns200WithBasicAuthenticationHeader() {
        Map<String, String> body = new HashMap<>();
        body.put("lol", "rofl");
        String username = "lol";
        String password = "rofl";
        String credentials = username + ":" + password;
        Header[] header = new Header[1];
        header[0] = new Header("Authorization", "Basic " + new String(Base64.encodeBase64(credentials.getBytes())));
        HttpClient.Response<String> actual = httpClient.post("http://localhost:8882/basic-auth", body, header);

        assertThat(actual.getStatusCode(), is(200));
        assertThat(actual.getBody(), is(equalTo("<auth>successfully authorized</auth>")));
    }

    @Test
    public void postOnLocalhostReturns401WithInvalidCredentialsInBasicAuthenticationHeader() {
        Map<String, String> body = new HashMap<>();
        body.put("lol", "rofl");
        String username = "l";
        String password = "r";
        String credentials = username + ":" + password;
        Header[] header = new Header[1];
        header[0] = new Header("Authorization", "Basic " + new String(Base64.encodeBase64(credentials.getBytes())));

        HttpClient.Response<String> actual = httpClient.post("http://localhost:8882/basic-auth", body, header);

        assertThat(actual.getStatusCode(), is(401));
        //assertThat(actual.getBody(), is(containsString("Unauthorized")));
    }

    @Test
    public void authLogin() {
        Map<String, String> body = new HashMap<>();
        body.put("fallback", "data");
        HttpClient.Response<String> actual = httpClient.post("http://localhost:8882/auth/login", body);

        assertThat(actual.getStatusCode(), is(200));
        assertThat(actual.getBody(), is(equalTo("<state>yes</state>")));
    }

    @Before
    public void startHttpServer() throws Exception {
        stubbyManagerFactory = new StubbyManagerFactory();

        Map<String, String> startParameter = new HashMap<>();
        startParameter.put("location", "localhost");
        startParameter.put("stubs", "8882");
        startParameter.put("disable_admin_portal", "");

        stubbyManager = stubbyManagerFactory.construct(String.valueOf(new File(getClass().getClassLoader().getResource("stubbyDataFile.yml").getPath())), startParameter);

        stubbyManager.startJetty();
    }

    @After
    public void stopHttpServer() throws Exception {
        stubbyManager.stopJetty();
    }
}
