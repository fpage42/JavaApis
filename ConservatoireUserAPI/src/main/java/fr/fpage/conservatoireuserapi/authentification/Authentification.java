package fr.fpage.conservatoireuserapi.authentification;

import fr.fpage.conservatoireuserapi.Utils;

public class Authentification {

    private static final String AUTHORIZATION_API_URL = "http://localhost:8080";

    public static AuthUser getAuthUser(UserToken userToken, String userSearchUuid)
    {
        return Utils.jsonApiCallObject(AUTHORIZATION_API_URL + "/users/other?userSearchUuid=" + userSearchUuid, userToken.getUuid().toString(), "GET", AuthUser.class);
    }

    public static AuthUser getAuthUser(UserToken userToken)
    {
        return Utils.jsonApiCallObject(AUTHORIZATION_API_URL + "/users/self", userToken.getUuid().toString(), "GET", AuthUser.class);
    }

    public static AuthUser[] getAuthUserList(UserToken userToken)
    {
        return Utils.jsonApiCallObject(AUTHORIZATION_API_URL + "/users/list", userToken.getUuid().toString(), "GET", AuthUser[].class);
    }
}