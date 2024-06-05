package aminoacid;

public class EqualCondition extends CountCondition{



  @Override
  protected boolean check(int count, int nbr) {
    return count == nbr;
  }
  
}
