package com.dev.responses;
import com.dev.objects.Play;
import java.util.List;

public class ListPlayResponse extends BasicResponse{

    private List<Play> plays;

    public ListPlayResponse(boolean success, Integer errorCode, List<Play> plays) {
        super(success, errorCode);
        this.plays = plays;
    }
    public List<Play> getPlays() {
        return plays;
    }

    public void setPlays(List<Play> plays) {
        this.plays = plays;
    }
}
