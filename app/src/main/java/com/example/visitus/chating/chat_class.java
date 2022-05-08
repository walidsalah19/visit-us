package com.example.visitus.chating;

public class chat_class {
      public static String chatbot(String ma)
      {
          String replay="";
          ma.toLowerCase();
          ma.trim();
          if (ma.equals("hi")||ma.equals("hollo"))
          {
              replay= "Hi, my friend! how I can serve you ?";
          }
         else if (ma.contains("go to"))
          {
              replay= "go to home then search about the place then click in result then click in location icon";
          }
         else if (ma.contains("ok")||ma.contains("thanks")||ma.contains("thank you "))
          {
              replay= "ok, good luck";
          }
          else if (ma.contains("dark mode"))
          {
              replay= "go to setting and turn on/off dark mode ";
          }
          else if (ma.contains("language"))
          {
              replay= "go to setting and press in language and select the language you need ";
          }
          else if (ma.contains("edit profile")||ma.contains("profile"))
          {
              replay= "go to setting and press in edit profile  ";
          }

          else {
              replay="change you'r  question";
          }
          return replay;
      }
}
