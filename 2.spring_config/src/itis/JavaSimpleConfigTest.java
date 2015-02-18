package itis;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class JavaSimpleConfigTest {

  public static void main(String[] args) {
    System.out.println("Java config context configuration test");
    
    ApplicationContext context
      = new AnnotationConfigApplicationContext(ItisConfig.class);
    
    Book obj = (Book) context.getBean("book");
    System.out.println( obj );
    
    obj.setName("Spring in Action");
    obj.setIsbn("9781935182351");
    System.out.println( obj );
  }
}