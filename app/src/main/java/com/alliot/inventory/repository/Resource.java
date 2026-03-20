package com.alliot.inventory.repository;

public class Resource<T> {

    public enum Status { LOADING, SUCCESS, ERROR }

    private final Status status;
    private final T data;
    private final ErrorType errorType;

    private Resource(Status status, T data, ErrorType errorType) {
        this.status = status;
        this.data = data;
        this.errorType = errorType;
    }

    public static <T> Resource<T> loading() {
        return new Resource<>(Status.LOADING, null, null);
    }

    public static <T> Resource<T> success(T data) {
        return new Resource<>(Status.SUCCESS, data, null);
    }

    public static <T> Resource<T> error(ErrorType errorType) {
        return new Resource<>(Status.ERROR, null, errorType);
    }

    public Status getStatus()       { return status; }
    public T getData()              { return data; }
    public ErrorType getErrorType() { return errorType; }
}
