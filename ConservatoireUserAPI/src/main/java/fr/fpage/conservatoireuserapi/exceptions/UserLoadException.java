package fr.fpage.conservatoireuserapi.exceptions;

public class UserLoadException extends Exception{

    private UserExceptionReasons reasons;

    public UserLoadException(UserExceptionReasons reasons) {
        this.reasons = reasons;
    }

    public UserExceptionReasons getReasons() {
        return reasons;
    }

    public enum UserExceptionReasons {
        UUID_NOT_FOUND;
    }


}
