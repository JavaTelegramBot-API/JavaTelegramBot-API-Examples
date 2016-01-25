package pro.zackpollard.telegrambot.inlinetranslationbot;

/**
 * @author Zack Pollard
 */
public enum Languages {

    ALBANIAN("sq", "Albanian"),
    ENGLISH("en", "English", "gb"),
    ARABIC("ar", "Arabic", "eg"),
    ARMENIAN("hy", "Armenian"),
    AZERBAIJAN("az", "Azerbaijan"),
    AFRIKAANS("af", "Afrikaans"),
    BASQUE("eu", "Basque"),
    BELARUSIAN("be", "Belarusian"),
    BULGARIAN("bg", "Bulgarian"),
    BOSNIAN("bs", "Bosnian"),
    WELSH("cy", "Welsh"),
    VIETNAMESE("vi", "Vietnamese"),
    HUNGARIAN("hu", "Hungarian"),
    HAITIAN("ht", "Haitian"),
    GALICIAN("gl", "Galician"),
    DUTCH("nl", "Dutch"),
    GREEK("el", "Greek"),
    GEORGIAN("ka", "Georgian"),
    DANISH("da", "Danish"),
    YIDDISH("he", "Yiddish"),
    INDONESIAN("id", "Indonesian"),
    IRISH("ga", "Irish"),
    ITALIAN("it", "Italian"),
    ICELANDIC("is", "Icelandic"),
    SPANISH("es", "Spanish"),
    KAZAKH("kk", "Kazakh"),
    CATALAN("ca", "Catalan"),
    KYRGYZ("ky", "Kyrgyz"),
    CHINESE("zh", "Chinese", "cn"),
    KOREAN("ko", "Korean"),
    LATIN("la", "Latin"),
    LATVIAN("lv", "Latvian"),
    LITHUANIAN("lt", "Lithuanian"),
    MALAGASY("mg", "Malagasy"),
    MALAY("ms", "Malay"),
    MALTESE("mt", "Maltese"),
    MACEDONIAN("mk", "Macedonian"),
    MONGOLIAN("mn", "Mongolian"),
    GERMAN("de", "German"),
    NORWEGIAN("no", "Norwegian"),
    PERSIAN("fa", "Persian", "ir"),
    POLISH("pl", "Polish"),
    PORTUGUESE("pt", "Portuguese"),
    ROMANIAN("ro", "Romanian"),
    RUSSIAN("ru", "Russian"),
    SERBIAN("sr", "Serbian"),
    SLOVAKIAN("sk", "Slovakian"),
    SLOVENIAN("sl", "Slovenian"),
    SWAHILI("sw", "Swahili"),
    TAJIK("tg", "Tajik"),
    THAI("th", "Thai"),
    TAGALOG("tl", "Tagalog"),
    TATAR("tt", "Tatar"),
    TURKISH("tr", "Turkish"),
    UZBEK("uz", "Uzbek"),
    UKRAINIAN("uk", "Ukrainian"),
    FINISH("fi", "Finish"),
    FRENCH("fr", "French"),
    CROATIAN("hr", "Croatian"),
    CZECH("cs", "Czech"),
    SWEDISH("sv", "Swedish"),
    ESTONIAN("et", "Estonian"),
    JAPANESE("ja", "Japanese", "jp"),;

    private final String langCode;
    private final String countryName;
    private final String countryCode;

    Languages(String langCode, String countryName, String countryCode) {

        this.langCode = langCode;
        this.countryName = countryName;
        this.countryCode = countryCode;
    }

    Languages(String langCode, String countryName) {

        this.langCode = langCode;
        this.countryName = countryName;
        this.countryCode = langCode;
    }

    public String getLangCode() {

        return langCode;
    }

    public String getCountryName() {

        return countryName;
    }

    public String getCountryCode() {

        return countryCode;
    }
}
