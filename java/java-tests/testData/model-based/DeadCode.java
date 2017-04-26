public class DeadCode
{
  private int a = 2;

  public void foo() {
    a = 2;
    <caret>
  }
}
