package com.dev.responses;

import com.dev.objects.Play;

import java.util.List;

public class BasicResponse {
    private boolean success;
    private Integer errorCode;
    private List<Play> playList;

    public BasicResponse(boolean success, Integer errorCode) {
        this.success = success;
        this.errorCode = errorCode;
    }

    public BasicResponse(boolean success, Integer errorCode, List<Play> playList) {
        this.success = success;
        this.errorCode = errorCode;
        this.playList = playList;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public List<Play> getPlayList() {
        return playList;
    }

    public void setPlayList(List<Play> playList) {
        this.playList = playList;
    }
}
