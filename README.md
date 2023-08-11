# @MVC 구현하기
### 스프링 MVC는 무엇인가?

- Model-View-Controller 또는 MVC 패턴을 처리하는 Spring 프레임워크의 모듈이다.

- Spring은 DispatcherServlet을 사용하여 MVC를 구현한다.

- 컨트롤러 역할은 DispatcherServlet, 모델은 데이터, 뷰는 다양한 템플릿 엔진이다.

- 웹 애플리케이션을 유연하고 확장 가능하게 만들어준다.

### HandlerExecution 의 역할
 - 메서드에 붙어있는 RequestMapping 을 읽어오기 위해서 메서드별로 저장해서 실행시켜야한다. 리플렉션을 이용해서 메서드를 실행하기 위해서는 해당 인스턴스도 가지고 있어야한다. 이 둘을 가지고 메서드를 실행시키는 역할을 한다.
###  HandlerAdapter 의 역할
- handler 를 실행한다.
  - 서블릿이 직접 핸들러를 실행하지 않는다. 느슨한 결합을 유지할 수 있다.
- 기존 레거시 Contoller 들이 DispatcherServlet 에서 필요로 하는 ModelAndView 타입을 반환할 수 있도록 한다.
- 추가적인 역할
  - 사용자가 Controller에 선언한 매개변수의 타입으로 변환시켜주는 역할을 한다.
  
3단계를 진행하면서 루트 경로를 처리해주는 ForwardController 를 어떻게 해야할지 고민이 있었는데
현재 스프링의 구현처럼 기존 Contoller 인터페이스와 ManualHandlerMapping 을 사용하기로 했다.


# 요구사항
1, 2단계
- [x] 컨트롤러 인터페이스 기반 MVC 프레임워크와 @MVC 프레임워크가 공존하도록 만들자.
- [x] ControllerScanner 클래스에서 @Controller 가 붙은 클래스를 찾을 수 있다.
- [x] HandlerMappingRegistry 클래스에서 HandlerMapping 을 처리하도록 구현했다.
- [x] HandlerAdapter 생성
- [x] HandlerAdapterRegistry 클래스에서 HandlerAdapter 를 처리하도록 구현했다.

3단계
- [x] JspView 클래스를 구현한다.
- [x] JsonView 클래스를 구현한다.
- [x] Legacy MVC 제거한다.
