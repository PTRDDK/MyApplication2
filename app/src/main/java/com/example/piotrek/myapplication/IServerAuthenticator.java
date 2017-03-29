package com.example.piotrek.myapplication;

/**
 * Created by Piotrek on 29.03.2017.
 */

public interface IServerAuthenticator {

    public String signIn (final String email, final String password);

    public String signUp (final String email, final String username, final String password);
}
