package com.neuroval.translationApi.model.image;


import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public final class TesseractLanguages {
    private static final Map<String, String> LANGUAGE_CODES;

    static {
        Map<String, String> tempMap = new HashMap<>();
        tempMap.put("afr", "Afrikaans");
        tempMap.put("amh", "Amharic");
        tempMap.put("ara", "Arabic");
        tempMap.put("asm", "Assamese");
        tempMap.put("aze", "Azerbaijani");
        tempMap.put("aze_cyrl", "Azerbaijani - Cyrilic");
        tempMap.put("bel", "Belarusian");
        tempMap.put("ben", "Bengali");
        tempMap.put("bod", "Tibetan");
        tempMap.put("bos", "Bosnian");
        tempMap.put("bre", "Breton");
        tempMap.put("bul", "Bulgarian");
        tempMap.put("cat", "Catalan; Valencian");
        tempMap.put("ceb", "Cebuano");
        tempMap.put("ces", "Czech");
        tempMap.put("chi_sim", "Chinese - Simplified");
        tempMap.put("chi_tra", "Chinese - Traditional");
        tempMap.put("chr", "Cherokee");
        tempMap.put("cos", "Corsican");
        tempMap.put("cym", "Welsh");
        tempMap.put("dan", "Danish");
        tempMap.put("dan_frak", "Danish - Fraktur");
        tempMap.put("deu", "German");
        tempMap.put("deu_frak", "German - Fraktur");
        tempMap.put("deu_latf", "German (Fraktur Latin)");
        tempMap.put("dzo", "Dzongkha");
        tempMap.put("ell", "Greek, Modern (1453-)");
        tempMap.put("eng", "English");
        tempMap.put("enm", "English, Middle (1100-1500)");
        tempMap.put("epo", "Esperanto");
        tempMap.put("equ", "Math / equation detection module");
        tempMap.put("est", "Estonian");
        tempMap.put("eus", "Basque");
        tempMap.put("fao", "Faroese");
        tempMap.put("fas", "Persian");
        tempMap.put("fil", "Filipino (old - Tagalog)");
        tempMap.put("fin", "Finnish");
        tempMap.put("fra", "French");
        tempMap.put("frk", "German - Fraktur (now deu_latf)");
        tempMap.put("frm", "French, Middle (ca.1400-1600)");
        tempMap.put("fry", "Western Frisian");
        tempMap.put("gla", "Scottish Gaelic");
        tempMap.put("gle", "Irish");
        tempMap.put("glg", "Galician");
        tempMap.put("grc", "Greek, Ancient (to 1453)");
        tempMap.put("guj", "Gujarati");
        tempMap.put("hat", "Haitian; Haitian Creole");
        tempMap.put("heb", "Hebrew");
        tempMap.put("hin", "Hindi");
        tempMap.put("hrv", "Croatian");
        tempMap.put("hun", "Hungarian");
        tempMap.put("hye", "Armenian");
        tempMap.put("iku", "Inuktitut");
        tempMap.put("ind", "Indonesian");
        tempMap.put("isl", "Icelandic");
        tempMap.put("ita", "Italian");
        tempMap.put("ita_old", "Italian - Old");
        tempMap.put("jav", "Javanese");
        tempMap.put("jpn", "Japanese");
        tempMap.put("kan", "Kannada");
        tempMap.put("kat", "Georgian");
        tempMap.put("kat_old", "Georgian - Old");
        tempMap.put("kaz", "Kazakh");
        tempMap.put("khm", "Central Khmer");
        tempMap.put("kir", "Kirghiz; Kyrgyz");
        tempMap.put("kmr", "Kurmanji (Kurdish - Latin Script)");
        tempMap.put("kor", "Korean");
        tempMap.put("kor_vert", "Korean (vertical)");
        tempMap.put("kur", "Kurdish (Arabic Script)");
        tempMap.put("lao", "Lao");
        tempMap.put("lat", "Latin");
        tempMap.put("lav", "Latvian");
        tempMap.put("lit", "Lithuanian");
        tempMap.put("ltz", "Luxembourgish");
        tempMap.put("mal", "Malayalam");
        tempMap.put("mar", "Marathi");
        tempMap.put("mkd", "Macedonian");
        tempMap.put("mlt", "Maltese");
        tempMap.put("mon", "Mongolian");
        tempMap.put("mri", "Maori");
        tempMap.put("msa", "Malay");
        tempMap.put("mya", "Burmese");
        tempMap.put("nep", "Nepali");
        tempMap.put("nld", "Dutch; Flemish");
        tempMap.put("nor", "Norwegian");
        tempMap.put("oci", "Occitan (post 1500)");
        tempMap.put("ori", "Oriya");
        tempMap.put("osd", "Orientation and script detection module");
        tempMap.put("pan", "Panjabi; Punjabi");
        tempMap.put("pol", "Polish");
        tempMap.put("por", "Portuguese");
        tempMap.put("pus", "Pushto; Pashto");
        tempMap.put("que", "Quechua");
        tempMap.put("ron", "Romanian; Moldavian; Moldovan");
        tempMap.put("rus", "Russian");
        tempMap.put("san", "Sanskrit");
        tempMap.put("sin", "Sinhala; Sinhalese");
        tempMap.put("slk", "Slovak");
        tempMap.put("slk_frak", "Slovak - Fraktur");
        tempMap.put("slv", "Slovenian");
        tempMap.put("snd", "Sindhi");
        tempMap.put("spa", "Spanish; Castilian");
        tempMap.put("spa_old", "Spanish; Castilian - Old");
        tempMap.put("sqi", "Albanian");
        tempMap.put("srp", "Serbian");
        tempMap.put("srp_latn", "Serbian - Latin");
        tempMap.put("sun", "Sundanese");
        tempMap.put("swa", "Swahili");
        tempMap.put("swe", "Swedish");
        tempMap.put("syr", "Syriac");
        tempMap.put("tam", "Tamil");
        tempMap.put("tat", "Tatar");

        LANGUAGE_CODES = Collections.unmodifiableMap(tempMap); // Immutable Map
    }

    public static Map<String, String> getLanguageCodes() {
        return LANGUAGE_CODES;
    }
}
