package com.example.apprpg.utils;

import android.text.Html;
import android.widget.TextView;

public class StringHelper {

    public static String formatToName(String s){
        String after = s.trim().replaceAll("[^A-Za-z áàãâäéèêëíìîïóòõôöúùûüçÁÀÃÂÄÉÈÊËÍÌÎÏÓÒÕÖÔÚÙÛÜÇ']","");
        return removeDoubleSpaces(after);
    }

    public static String formatToPostTitle(String s){
        String after = s.trim().replaceAll("[^A-Za-z0-9 ?!.áàãâäéèêëíìîïóòõôöúùûüçÁÀÃÂÄÉÈÊËÍÌÎÏÓÒÕÖÔÚÙÛÜÇ']","");
        return removeDoubleSpaces(after);
    }

    public static String removeEmojis(String s){
        String after = s.replaceAll("[^\\p{L}\\p{M}\\p{N}\\p{P}\\p{Z}\\p{Cf}\\p{Cs}\\s\\+\\-\\|]","");
        return removeDoubleSpaces(after);
    }

    public static String removeDoubleSpaces(String s){
        return s.trim().replaceAll(" +"," ");
    }

    public static String onlyNumbers(String s){
        String after = s.trim().replaceAll("[^0-9]","");
        return removeDoubleSpaces(after);
    }

    public static String formatToAdapterBiography(String bio){
        if (bio == null || bio.isEmpty()){
            return "";
        }

        return removeDoubleSpaces( bio
                .replaceAll("\\*","")
                .replaceAll("_","")
                .replaceAll("#","")
                .replaceAll("\\n","")
                .trim() );
    }

    public static void formatToDescription(String nText, TextView textView){
        if (nText != null && !nText.isEmpty()) {
            if (nText.contains("*") || nText.contains("_") || nText.contains("~")) {
                nText = nText.replaceFirst("\\*", "<b>");
                nText = nText.replaceFirst("_", "<i>");
                nText = nText.replaceFirst("~", "<s>");

                formatToDescriptionClosing(nText, textView);
            } else {
                nText = nText.replaceAll("\\n", "<br />");
                textView.setText(Html.fromHtml(nText));
            }
        }
    }

    private static void formatToDescriptionClosing(String nText, TextView textView){
        nText = nText.replaceFirst("\\*", "</b>");
        nText = nText.replaceFirst("_", "</i>");
        nText = nText.replaceFirst("~", "</s>");

        formatToDescription(nText, textView);
    }

}
