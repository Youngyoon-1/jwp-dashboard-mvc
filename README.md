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

### DI 실습
#### Stage0
```
@Test
void stage0() {
    final var user = new User(1L, "칙촉");

    final var actual = UserService.join(user);

    assertThat(actual.getAccount()).isEqualTo("칙촉");
}
```
정적 메서드를 사용하면 변경에 취약하고 테스트 코드 또한 작성하기 어렵다. → 정적 메서드만 있어서 클래스 간에 매우 긴밀한 결합이 발생한다. 동적으로 의존성을 주입할 수 없다. 
#### Stage1
```
@Test
void stage1() {
    final var user = new User(1L, "칙촉");
    
    final var userDao = new UserDao();
    final var userService = new UserService(userDao);
    
    final var actual = userService.join(user);
    
    assertThat(actual.getAccount()).isEqualTo("칙촉");
}
```
의존하는 객체를 생성자에서 주입받는다. → 구체 클래스에 의존하고 있다.

#### Stage2
```
@Test
void stage2() {
    final var user = new User(1L, "칙촉");
    
    final UserDao userDao = new InMemoryUserDao();
    final var userService = new UserService(userDao);
    
    final var actual = userService.join(user);
    
    assertThat(actual.getAccount()).isEqualTo("칙촉");
}

```
구체 클래스가 아닌 인터페이스를 사용해서 결합도를 낮춘다 → 클라이언트에서 직접 구현 객체를 생성해서 넣어줘야 한다. OCP 위반

#### Stage3
```
@Test
void stage3() {
    final var user = new User(1L, "칙촉");
    
    final var diContainer = createDIContainer();
    final var userService = diContainer.getBean(UserService.class);
    
    final var actual = userService.join(user);
    
    assertThat(actual.getAccount()).isEqualTo("칙촉");
}

private static DIContainer createDIContainer() {
    final var classes = new HashSet<Class<?>>();
    classes.add(InMemoryUserDao.class);
    classes.add(UserService.class);
    return new DIContainer(classes);
}
```
여기서 DIContainer는 스프링의 BeanFactory, ApplicationContext에 해당한다.

객체를 생성하고 연결해주는 역할을 DIContainer 클래스에게 위임한다. → 직접 클래스를 등록해준다.

DIContainer는 애플리케이션을 구성하는 객체의 구조와 관계를 정의한 설계도 역할을 한다.

stage2 에서 직접 사용할 객체를 생성했다면, 자신이 사용할 객체를 선택하고 생성하지 않는다.

#### Stage4
```
@Test
void stage4() {
    final var user = new User(1L, "칙촉");
    
    final var diContainer = createDIContainer();
    final var userService = diContainer.getBean(UserService.class);
    
    final var actual = userService.join(user);
    
    assertThat(actual.getAccount()).isEqualTo("칙촉");
}

private static DIContainer createDIContainer() {
    final var rootPackageName = Stage4Test.class.getPackage().getName();
    return DIContainer.createContainerForPackage(rootPackageName);
}

```
Stage3 에서는 직접 빈으로 등록할 클래스를 등록해줘야 했다. -> 어노테이션 기반으로 변경해서 자동으로 빈 등록되도록 한다.

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
