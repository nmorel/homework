package com.github.nmorel.homework.api.services;

import com.github.nmorel.homework.api.parsers.HttpResponseParser;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpMethods;
import com.google.common.base.Optional;

/**
 * A generic service used to send request to Github api.
 * <p>
 * It can cache result of requests if asked to. With cache enabled, it uses the conditional requests described <a
 * href="http://developer.github.com/v3/#conditional-requests">here</a>. If we receive a 304, we return the cached
 * result.
 * </p>
 * 
 * @author Nicolas Morel
 */
public interface GithubService
{
    /**
     * @return a new instance of {@link GenericUrl} configured with the github url api. You can append additional paths
     * with {@link GenericUrl#appendRawPath(String)}.
     */
    GenericUrl newGithubUrl();

    /**
     * @return a new instance of {@link GenericUrl} configured with the github url api and the user token. If no token
     * exists, it returns {@link Optional#absent()}. You can append additional paths with
     * {@link GenericUrl#appendRawPath(String)}.
     */
    Optional<GenericUrl> newAuthenticatedGithubUrl();

    /**
     * Executes a request to github api. If a token is present or is associated to current user, it will be added to the
     * request.
     * 
     * @param method Http method to use. See {@link HttpMethods}.
     * @param url Complete url to the github resource. You can create an instance of it with the base api url configured
     * with {@link GithubService#newGithubUrl()}
     * @param parser A parser to handle the response.
     * @param cacheable true if the response should be cached, false otherwise
     * @return the parsed result
     */
    <T> T execute( String method, GenericUrl url, HttpResponseParser<T> parser, boolean cacheable );

}
