package cn.ohyeah.stb.game;

import cn.ohyeah.stb.util.Properties;

/**
 * 游戏配置类
 * @author maqian
 * @version 1.0
 */
public class Configurations {
	private static String DEFAULT_CONF_PATH = "/conf/game.conf";
	public static final String TAG_RECHARGE = "recharge";
	public static final String TAG_EXCHANGE = "exchange";
	public static final String TAG_COMMON = "common";
	public static final String TAG_OK = "ok";
	public static final String TAG_BACK = "back";
	public static final String TAG_CANCEL = "cancel";
	public static final String TAG_NONSUPPORT = "nonsupport";
	
	public static final String TAG_TELCOMSH = "telcomsh";			/*上海电信*/
	public static final String TAG_OHYEAH = "ohyeah";				/*欧耶平台*/
	
	private static Configurations instance = new Configurations();
	private static boolean success;
	private static String errorMessage;
	private String telcomOperators;
	private String serviceProvider;
	private String rechargeWay;
	private String subscribeFocus;
	private String rechargeCmd;
	private String price;
    private String appName;
	
	public static Configurations loadConfigurations() {
		return loadConfigurations(DEFAULT_CONF_PATH);
	}
	
	public static Configurations loadConfigurations(String path) {
		try {
			Properties props = new Properties();
			props.parseFile(path, "GB2312");
			instance.setProperties(props);
			success = true;
		}
		catch (Exception e) {
			success = false;
			errorMessage = e.getMessage();
		}
		return instance;
	}
	
	private void setProperties(Properties props) {
		telcomOperators = props.get("telcomOperators");
		serviceProvider = props.get("serviceProvider");
		rechargeWay = props.get("rechargeWay");
		subscribeFocus = props.get("subscribeFocus");
		rechargeCmd = props.get("rechargeCmd");
		price = props.get("price");
        appName = props.get("appName");
	}
	
	public static Configurations getInstance() {
		return instance;
	}
	
	public static boolean isLoadConfSuccess() {
		return success;
	}
	
	public static String getErrorMessage() {
		return errorMessage;
	}
	
	public String getTelcomOperators() {
		return telcomOperators;
	}
	
	public String getServiceProvider() {
		return serviceProvider;
	}
	
	public boolean isTelcomOperatorsTelcomsh() {
		return TAG_TELCOMSH.equals(telcomOperators);
	}
	
	public boolean isServiceProviderOhyeah() {
		return TAG_OHYEAH.equals(serviceProvider);
	}
	
	public String getSubscribeFocus() {
		return subscribeFocus;
	}
	
	public boolean isSubscribeFocusOk() {
		return TAG_OK.equals(subscribeFocus);
	}
	
	public String getRechargeWay() {
		return rechargeWay;
	}
	
	public boolean isRechargeWayCommon() {
		return TAG_COMMON.equals(rechargeWay);
	}
	
	public boolean isRechargeCmdExchange() {
		return TAG_EXCHANGE.equals(rechargeCmd);
	}
	
	public boolean isRechargeCmdRecharge() {
		return TAG_RECHARGE.equals(rechargeCmd);
	}
	
	public String getRechargeCmd() {
		String cmd = "充值";
		if (isRechargeCmdExchange()) {
			cmd = "兑换";
		}
		else {
			cmd = "充值";
		}
		return cmd;
	}
	
	public String getPrice() {
		return price;
	}

    public String getAppName() {
        return appName;
    }
	
	public String toString() {
		return telcomOperators+";"+serviceProvider
				+";"+rechargeWay+";"+subscribeFocus+";"+rechargeCmd+";"+price;
	}
}
