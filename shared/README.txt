in main class you need to use @EnableAspectJAutoProxy

exaple of main class:

  import org.springframework.context.annotation.EnableAspectJAutoProxy;

  @EnableAspectJAutoProxy
  public class Main {
    public static void main(String[] args) {
    }
  }