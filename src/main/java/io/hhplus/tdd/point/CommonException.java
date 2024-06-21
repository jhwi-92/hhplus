package io.hhplus.tdd.point;


//시간이 부족할 것 같아 익셉션 1개로 모든 예외처리
public class CommonException extends RuntimeException {

    public CommonException(String message) {
        super(message);
    }

}