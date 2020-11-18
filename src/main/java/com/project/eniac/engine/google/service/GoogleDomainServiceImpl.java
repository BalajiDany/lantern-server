package com.project.eniac.engine.google.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import com.project.eniac.utils.RandomGenerator;

public class GoogleDomainServiceImpl implements GoogleDomainService {
	
	// TODO
	// Once DB schema is defined move to table.
	private static Map<String, String> domainMap = new HashMap<String, String>();
	
	private static String DEFAULT_DOMAIN = "google.com";

	static {

		
		/**
		 * [Auto Generated]
		 * 
		 * This Data is scraped from the (https://www.google.com/supported_domains)
		 * 
		 * Open the Link and run the script in console.
		 * 
			const domains = document.body.innerText.split(' ');
			let str = '';
			domains.sort().forEach(domain => {
				const splited = domain.split('.');
				const region = splited[splited.length - 1] || '';
			
				if (region && region !== '')
					str += `domainMap.put("${region.toUpperCase()}", "${domain}");\n`
			});
			console.log(str);
		 * 
		 * 
		 * NOTE: it will create like ".google.ad" (find and replace .google => google )
		 * 
		 */
		domainMap.put("AD", "google.ad");
		domainMap.put("AE", "google.ae");
		domainMap.put("AL", "google.al");
		domainMap.put("AM", "google.am");
		domainMap.put("AS", "google.as");
		domainMap.put("AT", "google.at");
		domainMap.put("AZ", "google.az");
		domainMap.put("BA", "google.ba");
		domainMap.put("BE", "google.be");
		domainMap.put("BF", "google.bf");
		domainMap.put("BG", "google.bg");
		domainMap.put("BI", "google.bi");
		domainMap.put("BJ", "google.bj");
		domainMap.put("BS", "google.bs");
		domainMap.put("BT", "google.bt");
		domainMap.put("BY", "google.by");
		domainMap.put("CA", "google.ca");
		domainMap.put("CAT", "google.cat");
		domainMap.put("CD", "google.cd");
		domainMap.put("CF", "google.cf");
		domainMap.put("CG", "google.cg");
		domainMap.put("CH", "google.ch");
		domainMap.put("CI", "google.ci");
		domainMap.put("CL", "google.cl");
		domainMap.put("CM", "google.cm");
		domainMap.put("CN", "google.cn");
		domainMap.put("AO", "google.co.ao");
		domainMap.put("BW", "google.co.bw");
		domainMap.put("CK", "google.co.ck");
		domainMap.put("CR", "google.co.cr");
		domainMap.put("ID", "google.co.id");
		domainMap.put("IL", "google.co.il");
		domainMap.put("IN", "google.co.in");
		domainMap.put("JP", "google.co.jp");
		domainMap.put("KE", "google.co.ke");
		domainMap.put("KR", "google.co.kr");
		domainMap.put("LS", "google.co.ls");
		domainMap.put("MA", "google.co.ma");
		domainMap.put("MZ", "google.co.mz");
		domainMap.put("NZ", "google.co.nz");
		domainMap.put("TH", "google.co.th");
		domainMap.put("TZ", "google.co.tz");
		domainMap.put("UG", "google.co.ug");
		domainMap.put("UK", "google.co.uk");
		domainMap.put("UZ", "google.co.uz");
		domainMap.put("VE", "google.co.ve");
		domainMap.put("VI", "google.co.vi");
		domainMap.put("ZA", "google.co.za");
		domainMap.put("ZM", "google.co.zm");
		domainMap.put("ZW", "google.co.zw");
		domainMap.put("COM", "google.com");
		domainMap.put("AF", "google.com.af");
		domainMap.put("AG", "google.com.ag");
		domainMap.put("AI", "google.com.ai");
		domainMap.put("AR", "google.com.ar");
		domainMap.put("AU", "google.com.au");
		domainMap.put("BD", "google.com.bd");
		domainMap.put("BH", "google.com.bh");
		domainMap.put("BN", "google.com.bn");
		domainMap.put("BO", "google.com.bo");
		domainMap.put("BR", "google.com.br");
		domainMap.put("BZ", "google.com.bz");
		domainMap.put("CO", "google.com.co");
		domainMap.put("CU", "google.com.cu");
		domainMap.put("CY", "google.com.cy");
		domainMap.put("DO", "google.com.do");
		domainMap.put("EC", "google.com.ec");
		domainMap.put("EG", "google.com.eg");
		domainMap.put("ET", "google.com.et");
		domainMap.put("FJ", "google.com.fj");
		domainMap.put("GH", "google.com.gh");
		domainMap.put("GI", "google.com.gi");
		domainMap.put("GT", "google.com.gt");
		domainMap.put("HK", "google.com.hk");
		domainMap.put("JM", "google.com.jm");
		domainMap.put("KH", "google.com.kh");
		domainMap.put("KW", "google.com.kw");
		domainMap.put("LB", "google.com.lb");
		domainMap.put("LY", "google.com.ly");
		domainMap.put("MM", "google.com.mm");
		domainMap.put("MT", "google.com.mt");
		domainMap.put("MX", "google.com.mx");
		domainMap.put("MY", "google.com.my");
		domainMap.put("NA", "google.com.na");
		domainMap.put("NG", "google.com.ng");
		domainMap.put("NI", "google.com.ni");
		domainMap.put("NP", "google.com.np");
		domainMap.put("OM", "google.com.om");
		domainMap.put("PA", "google.com.pa");
		domainMap.put("PE", "google.com.pe");
		domainMap.put("PG", "google.com.pg");
		domainMap.put("PH", "google.com.ph");
		domainMap.put("PK", "google.com.pk");
		domainMap.put("PR", "google.com.pr");
		domainMap.put("PY", "google.com.py");
		domainMap.put("QA", "google.com.qa");
		domainMap.put("SA", "google.com.sa");
		domainMap.put("SB", "google.com.sb");
		domainMap.put("SG", "google.com.sg");
		domainMap.put("SL", "google.com.sl");
		domainMap.put("SV", "google.com.sv");
		domainMap.put("TJ", "google.com.tj");
		domainMap.put("TR", "google.com.tr");
		domainMap.put("TW", "google.com.tw");
		domainMap.put("UA", "google.com.ua");
		domainMap.put("UY", "google.com.uy");
		domainMap.put("VC", "google.com.vc");
		domainMap.put("VN", "google.com.vn");
		domainMap.put("CV", "google.cv");
		domainMap.put("CZ", "google.cz");
		domainMap.put("DE", "google.de");
		domainMap.put("DJ", "google.dj");
		domainMap.put("DK", "google.dk");
		domainMap.put("DM", "google.dm");
		domainMap.put("DZ", "google.dz");
		domainMap.put("EE", "google.ee");
		domainMap.put("ES", "google.es");
		domainMap.put("FI", "google.fi");
		domainMap.put("FM", "google.fm");
		domainMap.put("FR", "google.fr");
		domainMap.put("GA", "google.ga");
		domainMap.put("GE", "google.ge");
		domainMap.put("GG", "google.gg");
		domainMap.put("GL", "google.gl");
		domainMap.put("GM", "google.gm");
		domainMap.put("GR", "google.gr");
		domainMap.put("GY", "google.gy");
		domainMap.put("HN", "google.hn");
		domainMap.put("HR", "google.hr");
		domainMap.put("HT", "google.ht");
		domainMap.put("HU", "google.hu");
		domainMap.put("IE", "google.ie");
		domainMap.put("IM", "google.im");
		domainMap.put("IQ", "google.iq");
		domainMap.put("IS", "google.is");
		domainMap.put("IT", "google.it");
		domainMap.put("JE", "google.je");
		domainMap.put("JO", "google.jo");
		domainMap.put("KG", "google.kg");
		domainMap.put("KI", "google.ki");
		domainMap.put("KZ", "google.kz");
		domainMap.put("LA", "google.la");
		domainMap.put("LI", "google.li");
		domainMap.put("LK", "google.lk");
		domainMap.put("LT", "google.lt");
		domainMap.put("LU", "google.lu");
		domainMap.put("LV", "google.lv");
		domainMap.put("MD", "google.md");
		domainMap.put("ME", "google.me");
		domainMap.put("MG", "google.mg");
		domainMap.put("MK", "google.mk");
		domainMap.put("ML", "google.ml");
		domainMap.put("MN", "google.mn");
		domainMap.put("MS", "google.ms");
		domainMap.put("MU", "google.mu");
		domainMap.put("MV", "google.mv");
		domainMap.put("MW", "google.mw");
		domainMap.put("NE", "google.ne");
		domainMap.put("NL", "google.nl");
		domainMap.put("NO", "google.no");
		domainMap.put("NR", "google.nr");
		domainMap.put("NU", "google.nu");
		domainMap.put("PL", "google.pl");
		domainMap.put("PN", "google.pn");
		domainMap.put("PS", "google.ps");
		domainMap.put("PT", "google.pt");
		domainMap.put("RO", "google.ro");
		domainMap.put("RS", "google.rs");
		domainMap.put("RU", "google.ru");
		domainMap.put("RW", "google.rw");
		domainMap.put("SC", "google.sc");
		domainMap.put("SE", "google.se");
		domainMap.put("SH", "google.sh");
		domainMap.put("SI", "google.si");
		domainMap.put("SK", "google.sk");
		domainMap.put("SM", "google.sm");
		domainMap.put("SN", "google.sn");
		domainMap.put("SO", "google.so");
		domainMap.put("SR", "google.sr");
		domainMap.put("ST", "google.st");
		domainMap.put("TD", "google.td");
		domainMap.put("TG", "google.tg");
		domainMap.put("TL", "google.tl");
		domainMap.put("TM", "google.tm");
		domainMap.put("TN", "google.tn");
		domainMap.put("TO", "google.to");
		domainMap.put("TT", "google.tt");
		domainMap.put("VG", "google.vg");
		domainMap.put("VU", "google.vu");
		domainMap.put("WS", "google.ws");
	}

	@Override
	public String getDefaultDomain() {
		return DEFAULT_DOMAIN;
	}

	@Override
	public String getDomainByLocation(String location) {
		return domainMap.getOrDefault(location, this.getDefaultDomain());
	}

	@Override
	public String getRandomDomain() {
		// Make util to fetch the random map value !should be generic
		// Need more care. Talking more than 100ms to get random Value;
		List<String> domains = domainMap.values().stream().collect(Collectors.toList());	
		int index = RandomGenerator.getRandomInt(domains.size());
		return domains.get(index);
	}
}
