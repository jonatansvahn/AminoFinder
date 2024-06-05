package aminoacid;

public class LessEqualCondition extends CountCondition {


  @Override
  protected boolean check(int count, int nbr) {
    return count <= nbr;
  }
  
}
