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
	
	/*�û���¼ʱ��*/
	private static Date loginTime;	
	
	/*��¼�ɹ�ʱ�Ļ�����ʱ��*/
	private static long loginTimeMillis;		
	private static SubscribeProperties subProps;
	
	/*��ǰ���*/
	public static int balance;	
	
	public static KeyState ks;

	/**
	 * �û�����
	 * @param mid
	 */
	public static void init(MIDlet mid){
		/*��ȡ����*/
		Configurations.loadConfigurations();	
		if (!Configurations.isLoadConfSuccess()) {
			errorMessage = "��ȡ����ʧ�ܡ�"+"\n";
			errorMessage += "ԭ��: "+Configurations.getErrorMessage()+"\n";
		}
		
		/*��������*/
		pm = new ParamManager(mid);
		pm.parse();		
		
		gameService = new GameService(pm);
		gameRecharge = new GameRecharge(pm,gameService);
		
		/*�û�����*/
		login();		
	}
	
	/**
	 * ��ȡ��������ǰʱ��
	 * @param pattern ��ʽ
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
	 * ��ȡ����ʱ��
	 * @param pattern ��ʽ
	 * @return
	 */
	public static String getLoginTime(String pattern) {
		return DateUtil.formatTimeStr(loginTime, pattern);
	}
	
	/**
	 * ��ȡ����ʱ��
	 * @return
	 */
	public static java.util.Date getLoginTime(){
		return loginTime;
	}
	
	/**
	 * ��ȡ��Ϸ��¼
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
	 * ������Ϸ��¼
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
	 * ������Ϸ�ɾ�(�ɾ��е���Ϸ������������)
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
	 * ��ȡ��Ϸ�ɾ�
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
	 * ��ѯ�����б�
	 * @param offset ��ʼ��¼
	 * @param length ��¼����
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
	 * ��ѯ�û�����
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
	 * �������
	 * @param propId  ����id
	 * @param propCount ��������
	 * @param remark  ������Ϣ
	 */
	public static void purchaseProp(int propId, int propCount, String remark){
		if(loginSuccessful){
			gameService.purchaseProp(propId, propCount, remark);
		}else{
			throw new ServiceException("login fail");
		}
	}
	
	/**
	 * ͬ������
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
	 * ��ֵ
	 * @param amount ���
	 * @param remark ����
	 */
	public static void recharge(int amount, String remark){
		if(loginSuccessful){
			gameService.recharge(amount, remark);
		}else{
			throw new ServiceException("login fail");
		}
	}
	
	/**
	 * ����ֵ����ĳ�ֵ
	 */
	public static void rechargeUI(KeyState keystate){
		ks = keystate;
		new Thread(gameRecharge).start();
	}
	
	/**
	 * ���÷���֮��ķ�����Ϣ
	 * @return
	 */
	public static String getServiceMessage() {
		return gameService.getServiceMessage();
	}
	
	/**
	 * �ж������õķ����Ƿ�ɹ�
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
				System.out.println("�û���¼�ɹ�:");
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
