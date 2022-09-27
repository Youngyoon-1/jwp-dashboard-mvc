package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.controller.tobe.HandlerExecution;
import org.junit.jupiter.api.Test;

class HandlerMappingRegistryTest {

    @Test
    void getHandler() {
        final var request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        final HandlerMappingRegistry registry = new HandlerMappingRegistry();
        registry.addHandlerMapping(new AnnotationHandlerMapping("samples"));

        final Object handler = registry.getHandler(request);

        assertThat(handler).isInstanceOf(HandlerExecution.class);
    }
}