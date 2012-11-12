package cn.ohyeah.stb.game;

import javax.microedition.midlet.MIDlet;

/**
 * ���������࣬����У��jad����
 * @author maqian
 * @version 1.0
 */
final class ParamManager {
	
	public static final String ENTRANCE_OHYEAH = "ohyeah";
	
	int[] rechargeAmounts;	/*��ֵ����б�*/
	
	String spid;			/*��Ӧ��ID�����κ��������ֵʱ��Ҫ*/
	String gameid;			/*��ϷID�����κ��������ֵʱ��Ҫ*/
	String buyURL;			/*��ѯԪ���Ϳ۳�Ԫ����������ַ*/
	String checkKey;		/*MD5�����ַ���*/
	
	String server;			/*��Ϸ��������ַ*/
	int accountId;			/*�û��˺�*/
	String userId;			/*������ID*/
	String accountName;		/*�û��ǳ�*/
	String userToken;		
	int productId;			/*��ƷID*/
	String productName;		/*��Ʒ��������*/
	String appName;			/*��ƷӢ������*/
	
	private String errorMessage;
	private boolean parseSuccessful;
	private MIDlet midlet;
	
	public ParamManager(MIDlet mid){
		this.midlet = mid;
	}
	
	public String getAppProperty(String param){
		return midlet.getAppProperty(param);
	}
	
	public boolean parse() {
		try {
			parseSuccessful = true;
			errorMessage = "";
			parseParam();
			if (!parseSuccessful) {
				System.out.println(errorMessage);
			}
		}
		catch (Exception e) {
			parseSuccessful = false;
			errorMessage += e.getMessage()+"\n";
		}
		return parseSuccessful;
	}
	
	private void parseParam() {
		Configurations conf = Configurations.getInstance();
		System.out.println("telcomOperators:"+conf.getTelcomOperators());
		System.out.println("serviceProvider:"+conf.getServiceProvider());
		if (conf.isServiceProviderOhyeah()) {
			parseOhyeahPlatParam();
		}
		else {
			parseSuccessful = false;
			errorMessage += "[����] ==> "+"δ֪����ڲ���"+conf.getServiceProvider()+"\n";
		}
		
		String amounts = conf.getPrice();
		if (amounts == null || "".equals(amounts)) {
			amounts = getStringParam("price");
		}
		if (amounts != null && !"".equals(amounts)) {
			parseAmounts(amounts);
		}
	}
	
	private void parseAmounts(String amounts) {
		try {
			int prevPos = 0;
			int scanPos = 0;
			int amountCount = 1;
			if (!amounts.startsWith("/") && !amounts.endsWith("/") && amounts.indexOf("//")<0) {
				while (scanPos < amounts.length()) {
					if (amounts.charAt(scanPos) == '/') {
						++amountCount;
					}
					++scanPos;
				}
				rechargeAmounts = new int[amountCount];
				
				scanPos = 0;
				amountCount = 0;
				while (scanPos < amounts.length()) {
					if (amounts.charAt(scanPos) == '/') {
						rechargeAmounts[amountCount] = Integer.parseInt(amounts.substring(prevPos, scanPos));
						++amountCount;
						prevPos = scanPos+1;
					}
					++scanPos;
				}
				rechargeAmounts[amountCount] = Integer.parseInt(amounts.substring(prevPos));
			}
			else {
				parseSuccessful = false;
				errorMessage += "[����] ==> "+"����"+"\""+"price"+"\""+"��ʽ����"+"\n";
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			parseSuccessful = false;
			errorMessage += "[����] ==> "+"����"+"\""+"price"+"\""+"��ʽ����"+"\n";
		}
	}
	
	private void parseOhyeahPlatParam() {
		server = getStringParam("server");
		userId = getStringParam("userId");
		accountName = getStringParam("accountName");
		userToken = getStringParam("userToken");
		appName = getStringParam("appName");
		buyURL = "";
		gameid = "";
		spid = "";
		checkKey = "";
	}
	
	private String getStringParam(String paramName) {
		String paramValue = null;
		try {
			paramValue = getAppProperty(paramName).trim();
			if ("".equals(paramValue)) {
				parseSuccessful = false;
				errorMessage += "[��Ϣ] ==> "+"��ȡ����"+"\""+paramName+"\""+"ʧ��"+"\n";
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			parseSuccessful = false;
			errorMessage += "[��Ϣ] ==> "+"��ȡ����"+"\""+paramName+"\""+"ʧ��"+"\n";
		}
		return paramValue;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public boolean isParseSuccess() {
		return parseSuccessful;
	}
}
