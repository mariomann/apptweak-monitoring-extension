package com.appdynamics.extensions.apptweak;


import lombok.Data;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by mariomann on 23/08/16.
 */
public class HttpClient {

    org.apache.commons.httpclient.HttpClient httpClient = new org.apache.commons.httpclient.HttpClient();

    public Response<String> get(String url) {
        return get(url, Collections.emptyMap(), new Header[0]);
    }

    public Response<String> get(String url, Map<String, String> query) {
        return get(url, query, new Header[0]);
    }

    public Response<String> get(String url, Map<String, String> query, Header[] headers) {
        String parameter = query.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("&"));
        GetMethod getMethod = new GetMethod(String.format("%s?%s", url, parameter));

        Arrays.stream(headers).forEach(getMethod::addRequestHeader);

        return execute(getMethod);
    }

    public Response<String> post(String url) {
        return post(url, Collections.emptyMap(), new Header[0] );
    }

    public Response<String> post(String url, Map<String, String> body) {
        return post(url, body, new Header[0] );
    }

    public Response<String> post(String url, Map<String, String> body, Header[] headers) {
        PostMethod postMethod = new PostMethod(url);

        Arrays.stream(headers).forEach(postMethod::addRequestHeader);

        body.entrySet().stream().forEach(System.out::println);

        NameValuePair[] pairs = body.entrySet().stream()
                .map(e -> new NameValuePair(e.getKey(), e.getValue()))
                .toArray(NameValuePair[]::new);
        postMethod.setRequestBody(pairs);

        return execute(postMethod);
    }

    private Response<String> execute(HttpMethod method) {
        try {
            int status = httpClient.executeMethod(method);
            String result = IOUtils.toString(method.getResponseBodyAsStream());
            Header[] headers = method.getResponseHeaders();

            return new Response<>(status, result, headers);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Data
    public static class Response<T> {
        public final int statusCode;
        public final T body;
        public final Header[] headers;
    }
}
