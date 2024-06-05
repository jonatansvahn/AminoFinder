package aminoacid;

public class LessEqualCondition extends AbstractCondition {


  @Override
  protected boolean check(int count, int nbr) {
    return count <= nbr;
  }
  
}
