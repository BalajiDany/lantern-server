package com.project.eniac.Trash;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.project.eniac.utils.RandomUtil;

public class GoogleLanguageServiceImpl implements GoogleLanguageService {

	// TODO
	// Once DB schema is defined move to table.
	private static Map<String, String> regionMap = new HashMap<String, String>();

	static {
		/**
		 * [Auto Generated]
		 * This Data is scraped from the (https://www.google.com/preferences?fg=1&safe=images)
		 * 
		 * Open the link and choose language tab 
		 * run the script in console.
		 * 
		 * 
			const languages = document.querySelectorAll('.jfk-radiobutton'); let str = '';
			languages.forEach(radioButton => {
				const region = radioButton.getAttribute('data-value');
				const regionName = radioButton.querySelector('.jfk-radiobutton-label').innerText;
				str += `regionMap.put("${region}", "${regionName}");\n`
			});
			console.log(str);
		 * 
		 * NOTE: It comes with region too,
		 * so make sure to remove region entries
		 */

		regionMap.put("en", "English");
		regionMap.put("es", "español");
		regionMap.put("es-419", "español (Latinoamérica)");
		regionMap.put("fr", "français");
		regionMap.put("hr", "hrvatski");
		regionMap.put("it", "italiano");
		regionMap.put("nl", "Nederlands");
		regionMap.put("pl", "polski");
		regionMap.put("pt-BR", "português (Brasil)");
		regionMap.put("pt-PT", "português (Portugal)");
		regionMap.put("vi", "Tiếng Việt");
		regionMap.put("tr", "Türkçe");
		regionMap.put("ru", "русский");
		regionMap.put("ar", "العربية");
		regionMap.put("th", "ไทย");
		regionMap.put("ko", "한국어");
		regionMap.put("zh-CN", "中文 (简体)");
		regionMap.put("zh-TW", "中文 (繁體)");
		regionMap.put("ja", "日本語");
		regionMap.put("ach", "Acoli");
		regionMap.put("af", "Afrikaans");
		regionMap.put("ak", "Akan");
		regionMap.put("ig", "Asụsụ Igbo");
		regionMap.put("az", "azərbaycan");
		regionMap.put("ban", "Balinese");
		regionMap.put("su", "Basa Sunda");
		regionMap.put("ceb", "Binisaya");
		regionMap.put("xx-bork", "Bork, bork, bork!");
		regionMap.put("bs", "bosanski");
		regionMap.put("br", "brezhoneg");
		regionMap.put("ca", "català");
		regionMap.put("cs", "čeština");
		regionMap.put("sn", "chiShona");
		regionMap.put("co", "Corsican");
		regionMap.put("cy", "Cymraeg");
		GeneralSearchResultEntity;
		regionMap.put("ht", "Haitian Creole");
		regionMap.put("ha", "Hausa");
		regionMap.put("haw", "ʻŌlelo Hawaiʻi");
		regionMap.put("bem", "Ichibemba");
		regionMap.put("rn", "Ikirundi");
		regionMap.put("id", "Indonesia");
		regionMap.put("ia", "interlingua");
		regionMap.put("xh", "isiXhosa");
		regionMap.put("zu", "isiZulu");
		regionMap.put("is", "íslenska");
		regionMap.put("jw", "Jawa");
		regionMap.put("rw", "Kinyarwanda");
		regionMap.put("sw", "Kiswahili");
		regionMap.put("tlh", "Klingon");
		regionMap.put("kg", "Kongo");
		regionMap.put("mfe", "kreol morisien");
		regionMap.put("kri", "Krio (Sierra Leone)");
		regionMap.put("la", "Latin");
		regionMap.put("lv", "latviešu");
		regionMap.put("to", "lea fakatonga");
		regionMap.put("lt", "lietuvių");
		regionMap.put("ln", "lingála");
		regionMap.put("loz", "Lozi");
		regionMap.put("lua", "Luba-Lulua");
		regionMap.put("lg", "Luganda");
		regionMap.put("hu", "magyar");
		regionMap.put("mg", "Malagasy");
		regionMap.put("mt", "Malti");
		regionMap.put("mi", "Māori");
		regionMap.put("ms", "Melayu");
		regionMap.put("pcm", "Nigerian Pidgin");
		regionMap.put("no", "norsk");
		regionMap.put("nso", "Northern Sotho");
		regionMap.put("ny", "Nyanja");
		regionMap.put("nn", "nynorsk");
		regionMap.put("uz", "o‘zbek");
		regionMap.put("oc", "Occitan");
		regionMap.put("om", "Oromoo");
		regionMap.put("xx-pirate", "Pirate");
		regionMap.put("ro", "română");
		regionMap.put("rm", "rumantsch");
		regionMap.put("qu", "Runasimi");
		regionMap.put("nyn", "Runyankore");
		regionMap.put("crs", "Seychellois Creole");
		regionMap.put("sq", "shqip");
		regionMap.put("sk", "slovenčina");
		regionMap.put("sl", "slovenščina");
		regionMap.put("so", "Soomaali");
		regionMap.put("st", "Southern Sotho");
		regionMap.put("sr-ME", "srpski (Crna Gora)");
		regionMap.put("sr-Latn", "srpski (latinica)");
		regionMap.put("fi", "suomi");
		regionMap.put("sv", "svenska");
		regionMap.put("tn", "Tswana");
		regionMap.put("tum", "Tumbuka");
		regionMap.put("tk", "türkmen dili");
		regionMap.put("tw", "Twi");
		regionMap.put("wo", "Wolof");
		regionMap.put("el", "Ελληνικά");
		regionMap.put("be", "беларуская");
		regionMap.put("bg", "български");
		regionMap.put("ky", "кыргызча");
		regionMap.put("kk", "қазақ тілі");
		regionMap.put("mk", "македонски");
		regionMap.put("mn", "монгол");
		regionMap.put("sr", "српски");
		regionMap.put("tt", "татар");
		regionMap.put("tg", "тоҷикӣ");
		regionMap.put("uk", "українська");
		regionMap.put("ka", "ქართული");
		regionMap.put("hy", "հայերեն");
		regionMap.put("yi", "ייִדיש");
		regionMap.put("iw", "עברית");
		regionMap.put("ug", "ئۇيغۇرچە");
		regionMap.put("ur", "اردو");
		regionMap.put("ps", "پښتو");
		regionMap.put("sd", "سنڌي");
		regionMap.put("fa", "فارسی");
		regionMap.put("ckb", "کوردیی ناوەندی");
		regionMap.put("ti", "ትግርኛ");
		regionMap.put("am", "አማርኛ");
		regionMap.put("ne", "नेपाली");
		regionMap.put("mr", "मराठी");
		regionMap.put("hi", "हिन्दी");
		regionMap.put("bn", "বাংলা");
		regionMap.put("pa", "ਪੰਜਾਬੀ");
		regionMap.put("gu", "ગુજરાતી");
		regionMap.put("or", "ଓଡ଼ିଆ");
		regionMap.put("ta", "தமிழ்");
		regionMap.put("te", "తెలుగు");
		regionMap.put("kn", "ಕನ್ನಡ");
		regionMap.put("ml", "മലയാളം");
		regionMap.put("si", "සිංහල");
		regionMap.put("lo", "ລາວ");
		regionMap.put("my", "မြန်မာ");
		regionMap.put("km", "ខ្មែរ");
		regionMap.put("chr", "ᏣᎳᎩ");
	}

	@Override
	public String getDefaultLanguage() {
		return "en";
	}

	@Override
	public String getValidLanguage(String sampleLanguage) {
		String lowercasedSampleLanguage = sampleLanguage.toLowerCase();
		return regionMap.containsKey(lowercasedSampleLanguage) ? lowercasedSampleLanguage : this.getDefaultLanguage();
	}

	@Override
	public String getRandomLanguage() {
		// Make util to fetch the random map value !should be generic
		// Need more care. Talking more than 100ms to get random Value;
		List<String> domains = regionMap.values().stream().collect(Collectors.toList());	
		int index = RandomUtil.getRandomInt(domains.size());
		return domains.get(index);
	}
}
