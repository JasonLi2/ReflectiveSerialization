import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;

public class TestInspector {
  String string;
  ClassA a;
  ClassB b;
  ClassC c;
  ClassD d;
  Boolean recursive;


  @Test
  public void TestInspectorOnString() throws Exception {
    recursive = true;
    string = "Hello world";
    new Inspector().inspect(a, recursive);
  }

  @Test
  public void TestClassA() throws Exception {
    recursive = true;
    a = new ClassA();
    new Inspector().inspect(a, recursive);
  }

  @Test
  public void TestClassD() throws Exception {
    recursive = true;
    d = new ClassD();
    new Inspector().inspect(d, recursive);
  }

  @Test
  public void TestParameterClassD() throws Exception {
    recursive = true;
    d = new ClassD(29);
    new Inspector().inspect(d, recursive);
  }
}
