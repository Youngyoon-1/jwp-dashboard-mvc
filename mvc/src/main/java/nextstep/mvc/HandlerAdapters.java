package nextstep.mvc;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapters {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapters() {
        this.handlerAdapters = new ArrayList<>();
    }

    public void add(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getAdapter(Object handler) {
        return handlerAdapters.stream()
            .filter(handlerAdapter -> handlerAdapter.supports(handler))
            .findFirst()
            .orElseThrow();
    }
}