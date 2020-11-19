package com.project.eniac.Trash;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.project.eniac.utils.RandomUtil;

public class GoogleRegionServiceImpl implements GoogleRegionService {

	// TODO
	// Once DB schema is defined move to table.
	private static Map<String, String> regionMap = new HashMap<String, String>();

	static {
		/**
		 * [Auto Generated]
		 * This Data is scraped from the (https://www.google.com/preferences?fg=1&safe=images)
		 * 
		 * Open the link and run the script in console.
		 * 
		 * 
			const regions = document.querySelectorAll('.DB6WRb'); let str = '';
			regions.forEach(dom => {
				const radioButton = dom.querySelector('.jfk-radiobutton')
				const region = radioButton.getAttribute('data-value');
				const regionName = radioButton.querySelector('.jfk-radiobutton-label').innerText;
				str += `regionMap.put("${region}", "${regionName}");\n`
			});
			console.log(str);
		 * 
		 */

		regionMap.put("AF", "Afghanistan");
		regionMap.put("AL", "Albania");
		regionMap.put("DZ", "Algeria");
		regionMap.put("AS", "American Samoa");
		regionMap.put("AD", "Andorra");
		regionMap.put("AO", "Angola");
		regionMap.put("AI", "Anguilla");
		regionMap.put("AG", "Antigua & Barbuda");
		regionMap.put("AR", "Argentina");
		regionMap.put("AM", "Armenia");
		regionMap.put("AU", "Australia");
		regionMap.put("AT", "Austria");
		regionMap.put("AZ", "Azerbaijan");
		regionMap.put("BS", "Bahamas");
		regionMap.put("BH", "Bahrain");
		regionMap.put("BD", "Bangladesh");
		regionMap.put("BY", "Belarus");
		regionMap.put("BE", "Belgium");
		regionMap.put("BZ", "Belize");
		regionMap.put("BJ", "Benin");
		regionMap.put("BT", "Bhutan");
		regionMap.put("BO", "Bolivia");
		regionMap.put("BA", "Bosnia & Herzegovina");
		regionMap.put("BW", "Botswana");
		regionMap.put("BR", "Brazil");
		regionMap.put("VG", "British Virgin Islands");
		regionMap.put("BN", "Brunei");
		regionMap.put("BG", "Bulgaria");
		regionMap.put("BF", "Burkina Faso");
		regionMap.put("BI", "Burundi");
		regionMap.put("KH", "Cambodia");
		regionMap.put("CM", "Cameroon");
		regionMap.put("CA", "Canada");
		regionMap.put("CV", "Cape Verde");
		regionMap.put("CF", "Central African Republic");
		regionMap.put("TD", "Chad");
		regionMap.put("CL", "Chile");
		regionMap.put("CO", "Colombia");
		regionMap.put("CG", "Congo - Brazzaville");
		regionMap.put("CD", "Congo - Kinshasa");
		regionMap.put("CK", "Cook Islands");
		regionMap.put("CR", "Costa Rica");
		regionMap.put("CI", "Côte d’Ivoire");
		regionMap.put("HR", "Croatia");
		regionMap.put("CU", "Cuba");
		regionMap.put("CY", "Cyprus");
		regionMap.put("CZ", "Czechia");
		regionMap.put("DK", "Denmark");
		regionMap.put("DJ", "Djibouti");
		regionMap.put("DM", "Dominica");
		regionMap.put("DO", "Dominican Republic");
		regionMap.put("EC", "Ecuador");
		regionMap.put("EG", "Egypt");
		regionMap.put("SV", "El Salvador");
		regionMap.put("EE", "Estonia");
		regionMap.put("ET", "Ethiopia");
		regionMap.put("FJ", "Fiji");
		regionMap.put("FI", "Finland");
		regionMap.put("FR", "France");
		regionMap.put("GA", "Gabon");
		regionMap.put("GM", "Gambia");
		regionMap.put("GE", "Georgia");
		regionMap.put("DE", "Germany");
		regionMap.put("GH", "Ghana");
		regionMap.put("GI", "Gibraltar");
		regionMap.put("GR", "Greece");
		regionMap.put("GL", "Greenland");
		regionMap.put("GT", "Guatemala");
		regionMap.put("GG", "Guernsey");
		regionMap.put("GY", "Guyana");
		regionMap.put("HT", "Haiti");
		regionMap.put("HN", "Honduras");
		regionMap.put("HK", "Hong Kong");
		regionMap.put("HU", "Hungary");
		regionMap.put("IS", "Iceland");
		regionMap.put("IN", "India");
		regionMap.put("ID", "Indonesia");
		regionMap.put("IQ", "Iraq");
		regionMap.put("IE", "Ireland");
		regionMap.put("IM", "Isle of Man");
		regionMap.put("IL", "Israel");
		regionMap.put("IT", "Italy");
		regionMap.put("JM", "Jamaica");
		regionMap.put("JP", "Japan");
		regionMap.put("JE", "Jersey");
		regionMap.put("JO", "Jordan");
		regionMap.put("KZ", "Kazakhstan");
		regionMap.put("KE", "Kenya");
		regionMap.put("KI", "Kiribati");
		regionMap.put("KW", "Kuwait");
		regionMap.put("KG", "Kyrgyzstan");
		regionMap.put("LA", "Laos");
		regionMap.put("LV", "Latvia");
		regionMap.put("LB", "Lebanon");
		regionMap.put("LS", "Lesotho");
		regionMap.put("LY", "Libya");
		regionMap.put("LI", "Liechtenstein");
		regionMap.put("LT", "Lithuania");
		regionMap.put("LU", "Luxembourg");
		regionMap.put("MG", "Madagascar");
		regionMap.put("MW", "Malawi");
		regionMap.put("MY", "Malaysia");
		regionMap.put("MV", "Maldives");
		regionMap.put("ML", "Mali");
		regionMap.put("MT", "Malta");
		regionMap.put("MU", "Mauritius");
		regionMap.put("MX", "Mexico");
		regionMap.put("FM", "Micronesia");
		regionMap.put("MD", "Moldova");
		regionMap.put("MN", "Mongolia");
		regionMap.put("ME", "Montenegro");
		regionMap.put("MS", "Montserrat");
		regionMap.put("MA", "Morocco");
		regionMap.put("MZ", "Mozambique");
		regionMap.put("MM", "Myanmar (Burma)");
		regionMap.put("NA", "Namibia");
		regionMap.put("NR", "Nauru");
		regionMap.put("NP", "Nepal");
		regionMap.put("NL", "Netherlands");
		regionMap.put("NZ", "New Zealand");
		regionMap.put("NI", "Nicaragua");
		regionMap.put("NE", "Niger");
		regionMap.put("NG", "Nigeria");
		regionMap.put("NU", "Niue");
		regionMap.put("MK", "North Macedonia");
		regionMap.put("NO", "Norway");
		regionMap.put("OM", "Oman");
		regionMap.put("PK", "Pakistan");
		regionMap.put("PS", "Palestine");
		regionMap.put("PA", "Panama");
		regionMap.put("PG", "Papua New Guinea");
		regionMap.put("PY", "Paraguay");
		regionMap.put("PE", "Peru");
		regionMap.put("PH", "Philippines");
		regionMap.put("PN", "Pitcairn Islands");
		regionMap.put("PL", "Poland");
		regionMap.put("PT", "Portugal");
		regionMap.put("PR", "Puerto Rico");
		regionMap.put("QA", "Qatar");
		regionMap.put("RO", "Romania");
		regionMap.put("RU", "Russia");
		regionMap.put("RW", "Rwanda");
		regionMap.put("WS", "Samoa");
		regionMap.put("SM", "San Marino");
		regionMap.put("ST", "São Tomé & Príncipe");
		regionMap.put("SA", "Saudi Arabia");
		regionMap.put("SN", "Senegal");
		regionMap.put("RS", "Serbia");
		regionMap.put("SC", "Seychelles");
		regionMap.put("SL", "Sierra Leone");
		regionMap.put("SG", "Singapore");
		regionMap.put("SK", "Slovakia");
		regionMap.put("SI", "Slovenia");
		regionMap.put("SB", "Solomon Islands");
		regionMap.put("SO", "Somalia");
		regionMap.put("ZA", "South Africa");
		regionMap.put("KR", "South Korea");
		regionMap.put("ES", "Spain");
		regionMap.put("LK", "Sri Lanka");
		regionMap.put("SH", "St. Helena");
		regionMap.put("VC", "St. Vincent & Grenadines");
		regionMap.put("SR", "Suriname");
		regionMap.put("SE", "Sweden");
		regionMap.put("CH", "Switzerland");
		regionMap.put("TW", "Taiwan");
		regionMap.put("TJ", "Tajikistan");
		regionMap.put("TZ", "Tanzania");
		regionMap.put("TH", "Thailand");
		regionMap.put("TL", "Timor-Leste");
		regionMap.put("TG", "Togo");
		regionMap.put("TO", "Tonga");
		regionMap.put("TT", "Trinidad & Tobago");
		regionMap.put("TN", "Tunisia");
		regionMap.put("TR", "Turkey");
		regionMap.put("TM", "Turkmenistan");
		regionMap.put("VI", "U.S. Virgin Islands");
		regionMap.put("UG", "Uganda");
		regionMap.put("UA", "Ukraine");
		regionMap.put("AE", "United Arab Emirates");
		regionMap.put("GB", "United Kingdom");
		regionMap.put("US", "United States");
		regionMap.put("UY", "Uruguay");
		regionMap.put("UZ", "Uzbekistan");
		regionMap.put("VU", "Vanuatu");
		regionMap.put("VE", "Venezuela");
		regionMap.put("VN", "Vietnam");
		regionMap.put("ZM", "Zambia");
		regionMap.put("ZW", "Zimbabwe");

		regionMap.put("ZZ", "Current Region");
	}

	@Override
	public String getDefaultRegion() {
		return "ZZ";
	}

	@Override
	public String getValidRegion(String sampleRegion) {
		String uppercasedSamplRegion = sampleRegion.toUpperCase();
		return regionMap.containsKey(uppercasedSamplRegion) ? uppercasedSamplRegion : this.getDefaultRegion();
	}

	@Override
	public String getRandomRegion() {
		// Make util to fetch the random map value !should be generic
		// Need more care. Talking more than 100ms to get random Value;
		List<String> domains = regionMap.values().stream().collect(Collectors.toList());	
		int index = RandomUtil.getRandomInt(domains.size());
		return domains.get(index);
	}

}
