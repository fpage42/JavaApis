package fr.fpage.timemanagementapi.exceptions;

public class BddLoadException extends Exception {

    private BddLoadExceptionReasons reasons;

    public BddLoadException(BddLoadExceptionReasons reasons) {
        this.reasons = reasons;
    }

    public BddLoadExceptionReasons getReasons() {
        return reasons;
    }

    public enum BddLoadExceptionReasons {
        UUID_NOT_FOUND;
    }

}
