package com.yitoa.weather.city;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.util.Log;

public class CityUtil {
	private static List<City> cities;

	public static List<City> getCities(Context context) {
		if (null == cities) {
			cities = new ArrayList<City>();
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						context.getAssets().open("city")));
				City city;
				while (br.ready()) {
					String s = br.readLine();
					Log.v("readLine", s);
					if (-1 < s.indexOf("|")) {
						String[] ss = s.split("|");
						city = new City();
						city.setPinyin(ss[0].trim());
						city.setHanzi(ss[1].trim());
						if (!ss[3].contains("null")) {
							city.setProvince(ss[3].trim());
						}
						cities.add(city);
					}
				}
				Log.v("city", cities.size() + "");
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return cities;
	}

	public static String[] CITIE_IN_PROVINCE = new String[] { "null", "null",
			"null", "null", "黑龙江", "黑龙江", "黑龙江", "黑龙江", "黑龙江", "黑龙江", "黑龙江",
			"黑龙江", "黑龙江", "黑龙江", "黑龙江", "黑龙江", "黑龙江", "吉林", "吉林", "吉林", "吉林",
			"吉林", "吉林", "吉林", "吉林", "吉林", "辽宁", "辽宁", "辽宁", "辽宁", "辽宁", "辽宁",
			"辽宁", "辽宁", "辽宁", "辽宁", "辽宁", "辽宁", "辽宁", "辽宁", "内蒙古", "内蒙古",
			"内蒙古", "内蒙古", "内蒙古", "内蒙古", "内蒙古", "内蒙古", "内蒙古", "内蒙古", "内蒙古",
			"内蒙古", "内蒙古", "内蒙古", "河北", "河北", "河北", "河北", "河北", "河北", "河北",
			"河北", "河北", "河北", "河北", "河北", "山西", "山西", "山西", "山西", "山西", "山西",
			"山西", "山西", "山西", "山西", "山西", "陕西", "陕西", "陕西", "陕西", "陕西", "陕西",
			"陕西", "陕西", "陕西", "陕西", "陕西", "陕西", "山东", "山东", "山东", "山东", "山东",
			"山东", "山东", "山东", "山东", "山东", "山东", "山东", "山东", "山东", "山东", "山东",
			"山东", "新疆", "新疆", "新疆", "新疆", "新疆", "新疆", "新疆", "新疆", "新疆", "新疆",
			"新疆", "新疆", "新疆", "新疆", "新疆", "新疆", "西藏", "西藏", "西藏", "西藏", "西藏",
			"西藏", "西藏", "青海", "青海", "青海", "青海", "青海", "青海", "青海", "青海", "甘肃",
			"甘肃", "甘肃", "甘肃", "甘肃", "甘肃", "甘肃", "甘肃", "甘肃", "甘肃", "甘肃", "甘肃",
			"甘肃", "宁夏", "宁夏", "宁夏", "宁夏", "宁夏", "河南", "河南", "河南", "河南", "河南",
			"河南", "河南", "河南", "河南", "河南", "河南", "河南", "河南", "河南", "河南", "河南",
			"河南", "河南", "江苏", "江苏", "江苏", "江苏", "江苏", "江苏", "江苏", "江苏", "江苏",
			"江苏", "江苏", "江苏", "江苏", "湖北", "湖北", "湖北", "湖北", "湖北", "湖北", "湖北",
			"湖北", "湖北", "湖北", "湖北", "湖北", "湖北", "湖北", "湖北", "湖北", "湖北", "浙江",
			"浙江", "浙江", "浙江", "浙江", "浙江", "浙江", "浙江", "浙江", "浙江", "浙江", "安徽",
			"安徽", "安徽", "安徽", "安徽", "安徽", "安徽", "安徽", "安徽", "安徽", "安徽", "安徽",
			"安徽", "安徽", "安徽", "安徽", "安徽", "福建", "福建", "福建", "福建", "福建", "福建",
			"福建", "福建", "福建", "江西", "江西", "江西", "江西", "江西", "江西", "江西", "江西",
			"江西", "江西", "江西", "湖南", "湖南", "湖南", "湖南", "湖南", "湖南", "湖南", "湖南",
			"湖南", "湖南", "湖南", "湖南", "湖南", "湖南", "湖南", "贵州", "贵州", "贵州", "贵州",
			"贵州", "贵州", "贵州", "贵州", "贵州", "四川", "四川", "四川", "四川", "四川", "四川",
			"四川", "四川", "四川", "四川", "四川", "四川", "四川", "四川", "四川", "四川", "四川",
			"四川", "四川", "四川", "四川", "广东", "广东", "广东", "广东", "广东", "广东", "广东",
			"广东", "广东", "广东", "广东", "广东", "广东", "广东", "广东", "广东", "广东", "广东",
			"广东", "广东", "广东", "广东", "云南", "云南", "云南", "云南", "云南", "云南", "云南",
			"云南", "云南", "云南", "云南", "云南", "云南", "云南", "云南", "云南", "广西", "广西",
			"广西", "广西", "广西", "广西", "广西", "广西", "广西", "广西", "广西", "广西", "广西",
			"广西", "海南", "海南", "海南", "海南", "海南", "海南", "海南", "海南", "海南", "海南",
			"海南", "海南", "海南", "海南", "海南", "海南", "海南", "海南", "海南", "海南", "海南",
			"海南", "海南", "海南", "null", "null", "台湾", "台湾", "台湾", "台湾", "台湾",
			"台湾", "台湾", "台湾", "台湾", "台湾", "台湾", "台湾", "台湾", "台湾", "台湾", "台湾",
			"台湾", "台湾", "台湾", "台湾" };
	public static String[] CITIE_IN_HANZI = new String[] { "北京", "上海", "天津",
			"重庆", "哈尔滨", "齐齐哈尔", "牡丹江", "佳木斯", "绥化", "黑河", "大兴安岭", "伊春", "大庆",
			"七台河", "鸡西", "鹤岗", "双鸭山", "长春", "吉林", "延吉", "四平", "通化", "白城", "辽源",
			"松原", "白山", "沈阳", "大连", "鞍山", "抚顺", "本溪", "丹东", "锦州", "营口", "阜新",
			"辽阳", "铁岭", "朝阳", "盘锦", "葫芦岛", "呼和浩特", "包头", "乌海", "集宁", "通辽",
			"赤峰", "鄂尔多斯", "临河", "锡林浩特", "呼伦贝尔", "海拉尔", "满洲里", "乌兰浩特", "阿拉善左旗",
			"石家庄", "保定", "张家口", "承德", "唐山", "廊坊", "沧州", "衡水", "邢台", "邯郸",
			"秦皇岛", "北戴河", "太原", "大同", "阳泉", "晋中", "长治", "晋城", "临汾", "运城", "朔州",
			"忻州", "离石", "西安", "长安", "咸阳", "三原", "延安", "榆林", "渭南", "商洛", "安康",
			"汉中", "宝鸡", "铜川", "济南", "青岛", "淄博", "德州", "烟台", "潍坊", "济宁", "泰安",
			"临沂", "菏泽", "滨州", "东营", "威海", "枣庄", "日照", "莱芜", "聊城", "乌鲁木齐",
			"克拉玛依", "石河子", "昌吉", "吐鲁番", "库尔勒", "阿拉尔", "阿克苏", "喀什", "伊宁", "塔城",
			"哈密", "和田", "阿勒泰", "阿图什", "博乐", "拉萨", "日喀则", "山南", "林芝", "昌都",
			"那曲", "阿里", "西宁", "海东", "黄南", "海南", "果洛", "玉树", "海西", "海北", "兰州",
			"定西", "平凉", "庆阳", "武威", "金昌", "张掖", "酒泉", "天水", "武都", "临夏", "合作",
			"白银", "银川", "石嘴山", "吴忠", "固原", "中卫", "郑州", "安阳", "新乡", "许昌", "平顶山",
			"信阳", "南阳", "开封", "洛阳", "商丘", "焦作", "鹤壁", "濮阳", "周口", "漯河", "驻马店",
			"三门峡", "济源", "南京", "无锡", "镇江", "苏州", "南通", "扬州", "盐城", "徐州", "淮安",
			"连云港", "常州", "泰州", "宿迁", "武汉", "襄樊", "鄂州", "孝感", "黄冈", "黄石", "咸宁",
			"荆州", "宜昌", "恩施", "十堰", "神农架", "随州", "荆门", "天门", "仙桃", "潜江", "杭州",
			"湖州", "嘉兴", "宁波", "绍兴", "台州", "温州", "丽水", "金华", "衢州", "舟山", "合肥",
			"蚌埠", "芜湖", "淮南", "马鞍山", "安庆", "宿州", "阜阳", "亳州", "黄山站", "滁州", "淮北",
			"铜陵", "宣城", "六安", "巢湖", "池州", "福州", "厦门", "宁德", "莆田", "泉州", "漳州",
			"龙岩", "三明", "南平", "南昌", "九江", "上饶", "抚州", "宜春", "吉安", "赣州", "景德镇",
			"萍乡", "新余", "鹰潭", "长沙", "湘潭", "株洲", "衡阳", "郴州", "常德", "赫山区", "娄底",
			"邵阳", "岳阳", "张家界", "怀化", "黔阳", "永州", "吉首", "贵阳", "遵义", "安顺", "都匀",
			"凯里", "铜仁", "毕节", "六盘水", "黔西", "成都", "攀枝花", "自贡", "绵阳", "南充", "达州",
			"遂宁", "广安", "巴中", "泸州", "宜宾", "内江", "资阳", "乐山", "眉山", "凉山", "雅安",
			"甘孜", "阿坝", "德阳", "广元", "广州", "韶关", "惠州", "梅州", "汕头", "深圳", "珠海",
			"佛山", "顺德", "肇庆", "湛江", "江门", "河源", "清远", "云浮", "潮州", "东莞", "中山",
			"阳江", "揭阳", "茂名", "汕尾", "昆明", "大理", "红河", "曲靖", "保山", "文山", "玉溪",
			"楚雄", "普洱", "昭通", "临沧", "怒江", "香格里拉", "丽江", "德宏", "景洪", "南宁", "崇左",
			"柳州", "来宾", "桂林", "梧州", "贺州", "贵港", "玉林", "百色", "钦州", "河池", "北海",
			"防城港", "海口", "三亚", "东方", "临高", "澄迈", "儋州", "昌江", "白沙", "琼中", "定安",
			"屯昌", "琼海", "文昌", "清兰", "保亭", "万宁", "陵水", "西沙", "珊瑚岛", "永署礁",
			"南沙岛", "乐东", "五指山", "通什", "香港", "澳门", "台北县", "台北市", "高雄", "东港",
			"大武", "恒春", "兰屿", "台南", "台中", "桃园", "新竹县", "新竹市", "公馆", "宜兰", "马公",
			"东吉屿", "嘉义", "阿里山", "玉山", "新港" };
	public static String[] CITIE_IN_PINYIN = new String[] { "bei jing",
			"shang hai", "tian jin", "zhong qing", "ha er bin", "qi qi ha er",
			"mu dan jiang", "jia mu si", "sui hua", "hei he",
			"da xing an ling", "yi chun", "da qing", "qi tai he", "ji xi",
			"he gang", "shuang ya shan", "zhang chun", "ji lin", "yan ji",
			"si ping", "tong hua", "bai cheng", "liao yuan", "song yuan",
			"bai shan", "shen yang", "da lian", "an shan", "fu shun", "ben xi",
			"dan dong", "jin zhou", "ying kou", "fu xin", "liao yang",
			"tie ling", "chao yang", "pan jin", "hu lu dao", "hu he hao te",
			"bao tou", "wu hai", "ji ning", "tong liao", "chi feng",
			"e er duo si", "lin he", "xi lin hao te", "hu lun bei er",
			"hai la er", "man zhou li", "wu lan hao te", "a la shan zuo qi",
			"shi jia zhuang", "bao ding", "zhang jia kou", "cheng de",
			"tang shan", "lang fang", "cang zhou", "heng shui", "xing tai",
			"han dan", "qin huang dao", "bei dai he", "tai yuan", "da tong",
			"yang quan", "jin zhong", "zhang zhi", "jin cheng", "lin fen",
			"yun cheng", "shuo zhou", "xin zhou", "li shi", "xi an",
			"zhang an", "xian yang", "san yuan", "yan an", "yu lin", "wei nan",
			"shang luo", "an kang", "han zhong", "bao ji", "tong chuan",
			"ji nan", "qing dao", "zi bo", "de zhou", "yan tai", "wei fang",
			"ji ning", "tai an", "lin yi", "he ze", "bin zhou", "dong ying",
			"wei hai", "zao zhuang", "ri zhao", "lai wu", "liao cheng",
			"wu lu mu qi", "ke la ma yi", "shi he zi", "chang ji", "tu lu fan",
			"ku er le", "a la er", "a ke su", "ka shen", "yi ning", "ta cheng",
			"ha mi", "he tian", "a le tai", "a tu shen", "bo le", "la sa",
			"ri ka ze", "shan nan", "lin zhi", "chang dou", "nei qu", "a li",
			"xi ning", "hai dong", "huang nan", "hai nan", "guo luo", "yu shu",
			"hai xi", "hai bei", "lan zhou", "ding xi", "ping liang",
			"qing yang", "wu wei", "jin chang", "zhang ye", "jiu quan",
			"tian shui", "wu dou", "lin xia", "he zuo", "bai yin", "yin chuan",
			"shi zui shan", "wu zhong", "gu yuan", "zhong wei", "zheng zhou",
			"an yang", "xin xiang", "xu chang", "ping ding shan", "xin yang",
			"nan yang", "kai feng", "luo yang", "shang qiu", "jiao zuo",
			"he bi", "pu yang", "zhou kou", "luo he", "zhu ma dian",
			"san men xia", "ji yuan", "nan jing", "wu xi", "zhen jiang",
			"su zhou", "nan tong", "yang zhou", "yan cheng", "xu zhou",
			"huai an", "lian yun gang", "chang zhou", "tai zhou", "su qian",
			"wu han", "xiang fan", "e zhou", "xiao gan", "huang gang",
			"huang shi", "xian ning", "jing zhou", "yi chang", "en shi",
			"shi yan", "shen nong jia", "sui zhou", "jing men", "tian men",
			"xian tao", "qian jiang", "hang zhou", "hu zhou", "jia xing",
			"ning bo", "shao xing", "tai zhou", "wen zhou", "li shui",
			"jin hua", "qu zhou", "zhou shan", "he fei", "bang bu", "wu hu",
			"huai nan", "ma an shan", "an qing", "su zhou", "fu yang",
			"bo zhou", "huang shan zhan", "chu zhou", "huai bei", "tong ling",
			"xuan cheng", "liu an", "chao hu", "chi zhou", "fu zhou",
			"sha men", "ning de", "pu tian", "quan zhou", "zhang zhou",
			"long yan", "san ming", "nan ping", "nan chang", "jiu jiang",
			"shang rao", "fu zhou", "yi chun", "ji an", "gan zhou",
			"jing de zhen", "ping xiang", "xin yu", "ying tan", "zhang sha",
			"xiang tan", "zhu zhou", "heng yang", "chen zhou", "chang de",
			"he shan qu", "lou di", "shao yang", "yue yang", "zhang jia jie",
			"huai hua", "qian yang", "yong zhou", "ji shou", "gui yang",
			"zun yi", "an shun", "dou yun", "kai li", "tong ren", "bi jie",
			"liu pan shui", "qian xi", "cheng dou", "pan zhi hua", "zi gong",
			"mian yang", "nan chong", "da zhou", "sui ning", "guang an",
			"ba zhong", "lu zhou", "yi bin", "nei jiang", "zi yang", "le shan",
			"mei shan", "liang shan", "ya an", "gan zi", "a ba", "de yang",
			"guang yuan", "guang zhou", "shao guan", "hui zhou", "mei zhou",
			"shan tou", "shen zhen", "zhu hai", "fo shan", "shun de",
			"zhao qing", "zhan jiang", "jiang men", "he yuan", "qing yuan",
			"yun fu", "chao zhou", "dong guan", "zhong shan", "yang jiang",
			"jie yang", "mao ming", "shan wei", "kun ming", "da li", "hong he",
			"qu jing", "bao shan", "wen shan", "yu xi", "chu xiong", "pu er",
			"zhao tong", "lin cang", "nu jiang", "xiang ge li la", "li jiang",
			"de hong", "jing hong", "nan ning", "chong zuo", "liu zhou",
			"lai bin", "gui lin", "wu zhou", "he zhou", "gui gang", "yu lin",
			"bai se", "qin zhou", "he chi", "bei hai", "fang cheng gang",
			"hai kou", "san ya", "dong fang", "lin gao", "cheng mai",
			"dan zhou", "chang jiang", "bai sha", "qiong zhong", "ding an",
			"tun chang", "qiong hai", "wen chang", "qing lan", "bao ting",
			"wan ning", "ling shui", "xi sha", "shan hu dao", "yong shu jiao",
			"nan sha dao", "le dong", "wu zhi shan", "tong shen", "xiang gang",
			"ao men", "tai bei xian", "tai bei shi", "gao xiong", "dong gang",
			"da wu", "heng chun", "lan yu", "tai nan", "tai zhong", "tao yuan",
			"xin zhu xian", "xin zhu shi", "gong guan", "yi lan", "ma gong",
			"dong ji yu", "jia yi", "a li shan", "yu shan", "xin gang" };
	public static String[] CITIE_HOT = new String[] { "北京", "天津", "上海", "海口",
			"三亚", "重庆", "贵阳", "六盘水", "遵义", "西宁", "石家庄", "唐山", "秦皇岛", "邯郸市",
			"邢台", "保定", "张家口", "承德", "沧州", "廊坊", "衡水", "太原", "大同", "阳泉", "长治",
			"晋城", "朔州", "呼和浩特", "内蒙包头", "内蒙乌海", "赤峰", "通辽市", "沈阳", "大连", "鞍山",
			"抚顺", "本溪", "丹东", "锦州", "营口", "阜新", "辽阳", "盘锦", "铁岭", "朝阳", "葫芦岛",
			"长春", "吉林", "四平", "辽源", "通化", "白山", "松原", "白城", "哈尔滨", "齐齐哈尔",
			"鸡西", "鹤岗", "双鸭山", "大庆", "伊春", "佳木斯", "七台河", "牡丹江", "黑河", "南京",
			"无锡", "徐州", "常州", "苏州", "南通", "连云港", "淮阴", "盐城", "扬州", "镇江", "泰州",
			"宿迁", "杭州", "宁波", "温州", "嘉兴", "湖州", "绍兴", "金华", "衢州", "舟山", "台州",
			"合肥", "芜湖", "蚌埠", "淮南", "马鞍山", "淮北", "铜陵", "安庆", "黄山", "滁州", "阜阳",
			"宿州", "巢湖", "六安", "福州", "厦门", "莆田", "三明", "泉州", "漳州", "南平", "龙岩",
			"南昌", "景德镇", "萍乡", "九江", "新余", "鹰潭", "赣州", "济南", "青岛", "淄博", "枣庄",
			"东营", "烟台", "潍坊", "济宁", "泰安", "威海", "日照", "莱芜", "临沂", "德州", "聊城",
			"郑州", "开封", "洛阳", "平顶山", "安阳", "鹤壁", "新乡", "焦作", "濮阳", "许昌", "漯河",
			"三门峡", "南阳", "商丘", "信阳", "武汉", "黄石", "十堰", "宜昌", "襄樊", "鄂州", "荆门",
			"孝感", "荆州", "黄冈", "咸宁", "长沙", "株洲", "湘潭", "衡阳", "邵阳", "岳阳", "常德",
			"张家界", "益阳", "郴州", "永州", "怀化", "娄底", "广州", "韶关", "深圳", "珠海", "汕头",
			"佛山", "江门", "湛江", "茂名", "肇庆", "惠州", "梅州", "汕尾", "河源", "阳江", "清远",
			"东莞", "中山", "潮州", "揭阳", "云浮", "南宁", "柳州", "桂林", "梧州", "北海", "防城港",
			"钦州", "贵港", "玉林", "成都", "自贡", "攀枝花", "泸州", "德阳", "绵阳", "广元", "遂宁",
			"内江", "乐山", "南充", "宜宾", "广安", "达州", "昆明", "曲靖", "玉溪", "乌鲁木齐",
			"克拉玛依", "西安", "铜川", "宝鸡", "咸阳", "渭南", "延安", "汉中", "榆林", "兰州",
			"嘉峪关", "金昌", "白银", "天水", "银川", "石嘴山", "吴忠",

	};

	private static HashMap<String, String> cityMap = null;

	public static HashMap<String, String> getHanzi4Pinyin() {
		if (null == cityMap) {
			cityMap = new HashMap<String, String>();
			for (int i = 0; i < CITIE_IN_HANZI.length; i++) {
				cityMap.put(CITIE_IN_HANZI[i], CITIE_IN_PINYIN[i]);
			}
		}
		return cityMap;
	}

	public static ArrayList<HashMap<String, Object>> getHotCity() {
		ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("ItemHotCity", "自动定位");
		lstImageItem.add(map);
		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put("ItemHotCity", "北京");
		lstImageItem.add(map1);
		HashMap<String, Object> map2 = new HashMap<String, Object>();
		map2.put("ItemHotCity", "上海");
		lstImageItem.add(map2);
		HashMap<String, Object> map3 = new HashMap<String, Object>();
		map3.put("ItemHotCity", "广州");
		lstImageItem.add(map3);
		HashMap<String, Object> map4 = new HashMap<String, Object>();
		map4.put("ItemHotCity", "深圳");
		lstImageItem.add(map4);
		HashMap<String, Object> map5 = new HashMap<String, Object>();
		map5.put("ItemHotCity", "武汉");
		lstImageItem.add(map5);
		HashMap<String, Object> map6 = new HashMap<String, Object>();
		map6.put("ItemHotCity", "南京");
		lstImageItem.add(map6);
		HashMap<String, Object> map7 = new HashMap<String, Object>();
		map7.put("ItemHotCity", "杭州");
		lstImageItem.add(map7);
		HashMap<String, Object> map8 = new HashMap<String, Object>();
		map8.put("ItemHotCity", "西安");
		lstImageItem.add(map8);
		HashMap<String, Object> map9 = new HashMap<String, Object>();
		map9.put("ItemHotCity", "郑州");
		lstImageItem.add(map9);
		HashMap<String, Object> map10 = new HashMap<String, Object>();
		map10.put("ItemHotCity", "成都");
		lstImageItem.add(map10);
		HashMap<String, Object> map11 = new HashMap<String, Object>();
		map11.put("ItemHotCity", "东莞");
		lstImageItem.add(map11);
		HashMap<String, Object> map12 = new HashMap<String, Object>();
		map12.put("ItemHotCity", "沈阳");
		lstImageItem.add(map12);
		HashMap<String, Object> map13 = new HashMap<String, Object>();
		map13.put("ItemHotCity", "天津");
		lstImageItem.add(map13);
		HashMap<String, Object> map14 = new HashMap<String, Object>();
		map14.put("ItemHotCity", "哈尔滨");
		lstImageItem.add(map14);
		HashMap<String, Object> map15 = new HashMap<String, Object>();
		map15.put("ItemHotCity", "长沙");
		lstImageItem.add(map15);
		HashMap<String, Object> map16 = new HashMap<String, Object>();
		map16.put("ItemHotCity", "福州");
		lstImageItem.add(map16);
		HashMap<String, Object> map17 = new HashMap<String, Object>();
		map17.put("ItemHotCity", "石家庄");
		lstImageItem.add(map17);
		return lstImageItem;
	}
	
	/***
	 * 根据城市拼音查找城市
	 * @param piny
	 * @return
	 */
	public String getCityPy(String piny){
		String city=null;
		int count=-1;
		//全部转成小写字母
		piny=piny.toLowerCase();
		Log.d("myDebug", "==piny=="+piny);
		for(int i=0;i<CITIE_IN_PINYIN.length;i++){
			String val=CITIE_IN_PINYIN[i];
			val=val.replace(" ", "");
			if(piny.equals(val)){
				//找到piny所在CITIE_IN_PINYIN中的位置
				count=i;
				break;
			}
		}
		
		//查找位置
		if(count==-1){//返回北京
			count=0;
		}
		return CITIE_IN_HANZI[count];
	}
}
