package Util;

public class DatabaseAccessException extends RuntimeException {

    public DatabaseAccessException(String messaggio,Throwable causa){
        super(messaggio,causa);
    }
}
