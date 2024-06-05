package aminoacid;

import java.util.ArrayList;
import java.util.List;

// count "k" = 3 and count "l" > 3



public class CommandParser {

  private enum commandStates {COUNT, LETTER, SYMBOL, NUMBER, AND}

  private enum conditions {EQUAL, LESS, LESS_EQ, GREATER, GREATER_EQ, UNDEFINED}

  public List<AbstractCondition> parseCommand(String commandString) throws Exception{


    String[] commandArray = commandString.split(" ");
    List<AbstractCondition> conditionList = new ArrayList<AbstractCondition>();
    commandStates currentState = commandStates.LETTER;
    


    conditions currentCondition = conditions.UNDEFINED;
    String currentSymbol = "";
    System.out.println(commandArray.length);
    /* check if command field is empty, in that case return empty conditionlist */
    if (commandArray.length == 1 && commandArray[0].equals("")) { 
      return conditionList;
    }

    for (String commandChunk: commandArray) {
      switch (currentState) {
        case LETTER:
          if (commandChunk.length() == 1) {
            currentState = commandStates.SYMBOL;
            currentSymbol = commandChunk;
          } else {
            throw new Exception("Expected a letter, got: \"" + commandChunk + "\"");
          }
          break;
        case SYMBOL:
          if (commandChunk.equals("=")) {
            currentCondition = conditions.EQUAL;
          }
          else if (commandChunk.equals("<")) {
            currentCondition = conditions.LESS;
          } 
          else if (commandChunk.equals("=<") || commandChunk.equals("<=")) {
            currentCondition = conditions.LESS_EQ;
          }
          else if (commandChunk.equals(">")) {
            currentCondition = conditions.GREATER;
          }
          else if (commandChunk.equals(">=") || commandChunk.equals("=>")) {
            currentCondition = conditions.GREATER_EQ;
          }
          else {
            throw new Exception("Expected one of [=, <, =<, <=, >=, =>] , got: \"" + commandChunk + "\"");
          }
          currentState = commandStates.NUMBER;
          break;
        case NUMBER:
          if (isNumeric(commandChunk)) {
            int num = Integer.parseInt(commandChunk);
            AbstractCondition condition = matchCondition(currentCondition);
            condition.setSymbol(currentSymbol);
            condition.setNumber(num);
            conditionList.add(condition);
            currentState = commandStates.AND;
          } 
          else {
            throw new Exception("Expected a number, got: \"" + commandChunk + "\"");
          }
          break;
        case AND:
          if (commandChunk.toLowerCase().equals("and")) {
            currentState = commandStates.LETTER;
          }
          else {
            throw new Exception("Expected \"and\" or end of command, got: \"" + commandChunk + "\"");
          }
          break;
        default:
          break;
      }
    }
    return conditionList;
  }

  private boolean isNumeric(String strNum) {
    if (strNum == null) {
      return false;
    }
    try {
      Integer.parseInt(strNum);
    } catch (NumberFormatException nfe) {
      return false;
    }
    return true;
  }

  private AbstractCondition matchCondition(conditions condition) throws Exception{
    AbstractCondition currentCondition;
    switch (condition) {
      case EQUAL:
        currentCondition = new EqualCondition();
        break;
      case LESS:
        currentCondition = new LessCondition();
        break;
      case LESS_EQ:
        currentCondition = new LessEqualCondition();
        break;
      case GREATER:
        currentCondition = new GreaterCondition();
        break;
      case GREATER_EQ:
        currentCondition = new GreaterEqualCondition();
        break;
      case UNDEFINED:
        throw new Exception("Unexpected error in matchCondition");
      default:
        throw new Exception("Unexpected error in matchCondition");
    }
    return currentCondition;
  }
  
}
