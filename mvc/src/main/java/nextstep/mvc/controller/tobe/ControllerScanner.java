package nextstep.mvc.controller.tobe;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(final Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public List<Object> scan() {
        final Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);
        return classes.stream()
                .map(this::createInstance)
                .collect(Collectors.toList());
    }

    private Object createInstance(final Class<?> it) {
        try {
            return it.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException("인스턴스 생성에 실패했습니다.", e);
        }
    }
}
