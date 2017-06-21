package org.shoukaiseki.maximo.anngoukafukugou;

public class ArgsUtil
{
  public static void main(String[] args)
  {
  }

  public String getvalue(String str)
  {
    if (str == null)
      return "";
    int pos = str.indexOf(61);
    if (pos > 0) {
      int pos1 = str.indexOf(32, pos);
      if (pos1 < pos)
        pos1 = str.length();

      if (pos1 > pos + 1)
        return str.substring(pos + 1, pos1).trim();
    }
    return "";
  }

  public String getValue(String[] args, String name) {
    if ((name == null) || ("".equals(name)) || (args == null))
      return null;
    int len = args.length;

    for (int i = 0; i < len; ++i) {
      String temp = args[i].toLowerCase();
      if (temp.startsWith(name.toLowerCase()))
        return getvalue(temp);
    }
    return null;
  }
}