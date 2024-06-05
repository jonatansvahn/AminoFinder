package aminoacid;

import java.util.*;

public class AminoModel {

  private List<AminoEntry> aminoList;

  public AminoModel(List<AminoEntry> list) {
    aminoList = new ArrayList<AminoEntry>(list);
  }
  

  public List<AminoEntry> filterAminos(int minLength, int maxLength, List<AbstractCondition> conditionList, boolean filterByHydrophobe, int hydrophobicCount) {
    List<AminoEntry> filteredList = new ArrayList<AminoEntry>();
    if (filterByHydrophobe) {
      conditionList.add(new HydrophobeCondition(hydrophobicCount));
    }

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
    if (filterByHydrophobe) {
      Collections.sort(filteredList, (a, b) -> Integer.compare(b.getHydrophobicCount(), a.getHydrophobicCount()));
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
