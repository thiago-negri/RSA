package rsa.api;

public interface BlockOutputStream<E extends Throwable> {

    void offer(byte[] buffer) throws E;

}
