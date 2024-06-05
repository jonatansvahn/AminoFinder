package aminoacid;

import java.util.*;

public class AminoModel {

  private List<AminoEntry> aminoList;

  public AminoModel(List<AminoEntry> list) {
    aminoList = new ArrayList<AminoEntry>(list);
  }
  

  public List<AminoEntry> filterAminos(int minLength, int maxLength, List<AbstractCondition> conditionList) {
    List<AminoEntry> filteredList = new ArrayList<AminoEntry>();
    

    for (AminoEntry aminoEntry : aminoList) {
      boolean filterThrough = true;
      for (AbstractCondition condition : conditionList) {
        if (!condition.checkCondition(aminoEntry)) {
          filterThrough = false;
          break;
        }
      }
      if (filterThrough && checkLength(aminoEntry.getLength(), minLength, maxLength)) {
        filteredList.add(aminoEntry);
      }
    }
    return filteredList;
  }

  private boolean checkLength(int aminoLength, int minLength, int maxLength) {
    return minLength <= aminoLength && aminoLength <= maxLength;
  }

  public List<AminoEntry> getAminoList() {
    return aminoList;
  }

  public void setAminoList(List<AminoEntry> list) {
    aminoList = new ArrayList<AminoEntry>(list);
  }
}
