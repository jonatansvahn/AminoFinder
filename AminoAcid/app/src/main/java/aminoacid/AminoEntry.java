package aminoacid;

public class AminoEntry {

  private int excelRow;
  private double mH;
  private int start;
  private int end;
  private String domain;
  private double hpclIndex;
  private String sequence;  
  private int length;

  public AminoEntry(int excelRow, double mH, int start, int end, String domain, double hpclIndex, String sequence) {
    this.excelRow = excelRow;
    this.mH = mH;
    this.start = start;
    this.end = end;
    this.domain = domain;
    this.hpclIndex = hpclIndex;
    this.sequence = sequence;
    length = sequence.length();
  }

  public String getSequence() {
    return sequence;
  }

  public int getLength() {
    return length;
  }

  public int getStart() {
    return start;
  }
  
  public int getEnd() {
    return end;
  }

  public double getMH() {
    return mH;
  }

  public String getDomain() {
    return domain;
  }

  public double getHPCL() {
    return hpclIndex;
  }
  public String[] getArray() {
    String[] result = {Integer.toString(excelRow), Double.toString(mH), Integer.toString(start), Integer.toString(end), domain, Double.toString(hpclIndex), sequence};
    return result;
  }
}
