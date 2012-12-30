package cn.ohyeah.stb.game;

import java.util.Date;

import javax.microedition.midlet.MIDlet;

import cn.ohyeah.itvgame.model.GameAttainment;
import cn.ohyeah.itvgame.model.GameRanking;
import cn.ohyeah.itvgame.model.GameRecord;
import cn.ohyeah.itvgame.model.LoginInfo;
import cn.ohyeah.itvgame.model.OwnProp;
import cn.ohyeah.itvgame.model.SubscribeProperties;
import cn.ohyeah.itvgame.service.ServiceException;
import cn.ohyeah.stb.key.KeyState;
import cn.ohyeah.stb.util.DateUtil;

public class ITVGame {
	
	private static ParamManager pm;
	private static GameService gameService;
	private static GameRecharge gameRecharge;
	private static String errorMessage;
	private static boolean loginSuccessful;
	private static String loginMessage;
	
	/*用户登录时间*/
	private static Date loginTime;	
	
	/*登录成功时的机顶盒时间*/
	private static long loginTimeMillis;		
	private static SubscribeProperties subProps;
	
	/*当前余额*/
	public static int balance;	
	
	public static KeyState ks;

	/**
	 * 用户登入
	 * @param mid
	 */
	public static void init(MIDlet mid){
		/*读取配置*/
		Configurations.loadConfigurations();	
		if (!Configurations.isLoadConfSuccess()) {
			errorMessage = "读取配置失败。"+"\n";
			errorMessage += "原因: "+Configurations.getErrorMessage()+"\n";
		}
		
		/*解析参数*/
		pm = new ParamManager(mid);
		pm.parse();		
		gameService = new GameService(pm);
		gameRecharge = new GameRecharge(pm,gameService);
		
		/*用户登入*/
		login();		
		ks = new KeyState();
	}
	
	/**
	 * 获取服务器当前时间
	 * @param pattern 格式
	 * @return
	 */
	public static String getServerTime(String pattern){
		try {
			LoginInfo info = gameService.userLogin();
			if (gameService.isServiceSuccessful()) {
				loginSuccessful = true;
				assignLoginInfo(info);
				return DateUtil.formatTimeStr(loginTime, pattern);
			}
			else {
				loginMessage = gameService.getServiceMessage();
				return null;
			}
		}
		catch (Exception e) {
			loginMessage = e.getMessage();
			System.out.println(loginMessage);
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 获取登入时间
	 * @param pattern 格式
	 * @return
	 */
	public static String getLoginTime(String pattern) {
		return DateUtil.formatTimeStr(loginTime, pattern);
	}
	
	/**
	 * 获取登入时间
	 * @return
	 */
	public static java.util.Date getLoginTime(){
		return loginTime;
	}
	
	/**
	 * 读取游戏记录
	 * @param recordId
	 * @return
	 */
	public static GameRecord readRecord(int recordId){
		if(loginSuccessful){
			return gameService.readRecord(recordId);
		}else{
			throw new ServiceException("login fail");
		}
	}
	
	/**
	 * 保存游戏记录
	 * @param record
	 */
	public static void saveRecord(GameRecord record){
		if(loginSuccessful){
			gameService.saveRecord(record);
		}else{
			throw new ServiceException("login fail");
		}
	} 
	
	/**
	 * 保存游戏成就(成就中的游戏积分用于排行)
	 * @param attainment
	 */
	public static void saveAttainment(GameAttainment attainment){
		if(loginSuccessful){
			gameService.saveAttainment(attainment);
		}else{
			throw new ServiceException("login fail");
		}
	}
	
	/**
	 * 读取游戏成就
	 * @param attainmentId
	 * @return
	 */
	public static GameAttainment readAttainment(int attainmentId){
		if(loginSuccessful){
			return gameService.readAttainment(attainmentId);
		}else{
			throw new ServiceException("login fail");
		}
	}
	
	/**
	 * 查询排行列表
	 * @param offset 起始记录
	 * @param length 记录条数
	 * @return
	 */
	public static GameRanking[] queryRankingList(int offset, int length){
		if(loginSuccessful){
			return gameService.queryRankingList(offset, length);
		}else{
			throw new ServiceException("login fail");
		}
	}
	
	/**
	 * 查询用户道具
	 * @return
	 */
	public static OwnProp[] queryOwnPropList(){
		if(loginSuccessful){
			return gameService.queryOwnPropList();
		}else{
			throw new ServiceException("login fail");
		}
	}
	
	/**
	 * 购买道具
	 * @param propId  道具id
	 * @param propCount 道具数量
	 * @param remark  描述信息
	 */
	public static void purchaseProp(int propId, int propCount, String remark){
		if(loginSuccessful){
			gameService.purchaseProp(propId, propCount, remark);
		}else{
			throw new ServiceException("login fail");
		}
	}
	
	/**
	 * 同步道具
	 * @param propIds
	 * @param counts
	 */
	public static void synProps(int[] propIds, int[] counts){
		if(loginSuccessful){
			gameService.synProps(propIds, counts);
		}else{
			throw new ServiceException("login fail");
		}
	}
	
	/**
	 * 充值
	 * @param amount 金额
	 * @param remark 描述
	 */
	public static void recharge(int amount, String remark){
		if(loginSuccessful){
			gameService.recharge(amount, remark);
		}else{
			throw new ServiceException("login fail");
		}
	}
	
	/**
	 * 带充值界面的充值
	 */
	public static void rechargeUI(){
		new Thread(gameRecharge).start();
	}
	
	/**
	 * 调用服务之后的反馈信息
	 * @return
	 */
	public static String getServiceMessage() {
		return gameService.getServiceMessage();
	}
	
	/**
	 * 判断所调用的服务是否成功
	 * @return
	 */
	public static boolean isServiceSuccessful() {
		return gameService.isServiceSuccessful();
	}
	

	private static void login(){
		try {
			LoginInfo info = gameService.userLogin();
			if (gameService.isServiceSuccessful()) {
				loginSuccessful = true;
				assignLoginInfo(info);
				System.out.println("用户登录成功:");
				printParams();
			}
			else {
				loginMessage = gameService.getServiceMessage();
			}
		}
		catch (Exception e) {
			loginMessage = e.getMessage();
			System.out.println(loginMessage);
			e.printStackTrace();
		}
	}
	
	private static void printParams() {
		System.out.println("loginTime: "+DateUtil.formatTimeStr(loginTime,DateUtil.PATTERN_DEFAULT));
		System.out.println("server: "+pm.server);
		//System.out.println("buyURL: "+pm.buyURL);
		System.out.println("accountId: "+pm.accountId);
		System.out.println("userId: "+pm.userId);
		System.out.println("accountName: "+pm.accountName);
		System.out.println("userToken: "+pm.userToken);
		System.out.println("productId: "+pm.productId);
		System.out.println("productName: "+pm.productName);
		System.out.println("appName: "+pm.appName);
		//System.out.println("checkKey: "+pm.checkKey);
		
		subProps.print();
		//auth.print();
	}

	private static void assignLoginInfo(LoginInfo info) {
		pm.accountId = info.getAccountId();
		pm.productId = info.getProductId();
		pm.productName = info.getProductName();
		loginTime = info.getSystemTime();
		loginTimeMillis = System.currentTimeMillis();
		subProps = info.getSubProps();
		balance = subProps.getBalance();
	}
	
	public static String getUserId() {
		return pm.userId;
	}
	
	public static String getAccountName() {
		return pm.accountName;
	}
	
	public static String getProductName() {
		return pm.productName;
	}
	
	public static int getBalance() {
		return balance;
	}
	
	public static String getExpendAmountUnit() {
		return subProps.getExpendAmountUnit();
	}
	
	public static int getRechargeRatio() {
		return subProps.getRechargeRatio();
	}
	
	public static String getSubscribeAmountUnit() {
		return subProps.getSubscribeAmountUnit();
	}
	
	public static int getSubscribeCashToAmountRatio() {
		return subProps.getSubscribeCashToAmountRatio();
	}
	
	public static int getExpendCashToAmountRatio() {
		return subProps.getExpendCashToAmountRatio();
	}
	
	public static boolean isSupportSubscribe() {
		return subProps.isSupportSubscribe();
	}
	
	public static java.util.Date getCurrentTime() {
		long pastMillis = System.currentTimeMillis() - loginTimeMillis;
		return new java.util.Date(loginTime.getTime()+pastMillis);
	}
	
	public static ParamManager getParamManager() {
		return pm;
	}
}
