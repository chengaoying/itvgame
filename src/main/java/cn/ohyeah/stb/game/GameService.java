package cn.ohyeah.stb.game;

import cn.ohyeah.itvgame.model.GameAttainment;
import cn.ohyeah.itvgame.model.GameRanking;
import cn.ohyeah.itvgame.model.GameRecord;
import cn.ohyeah.itvgame.model.LoginInfo;
import cn.ohyeah.itvgame.model.OwnProp;
import cn.ohyeah.itvgame.model.Prop;
import cn.ohyeah.itvgame.model.PurchaseRecord;
import cn.ohyeah.itvgame.model.SubscribeRecord;
import cn.ohyeah.itvgame.protocol.Constant;
import cn.ohyeah.itvgame.service.AccountService;
import cn.ohyeah.itvgame.service.AttainmentService;
import cn.ohyeah.itvgame.service.PropService;
import cn.ohyeah.itvgame.service.PurchaseService;
import cn.ohyeah.itvgame.service.RecordService;
import cn.ohyeah.itvgame.service.SubscribeService;
import cn.ohyeah.itvgame.service.SystemService;

/**
 * 服务包装类，对服务类做了简单的包装，
 * 为了保证线程安全，请先创建新的对象再调用
 * @author maqian
 * @version 1.0
 */
public class GameService {
	private static String server;
	private static ParamManager paramManager;
	
	private static int result;
	private static String message;
	
	public GameService(ParamManager pm){
		paramManager = pm;
		server = paramManager.server;
	}
	
	public LoginInfo userLogin() {
		try {
			AccountService accountService = new AccountService(server);
			LoginInfo info = accountService.userLogin(paramManager.buyURL, 
					paramManager.userId, paramManager.accountName, paramManager.userToken, paramManager.appName, paramManager.checkKey);
			result = accountService.getResult();
			if (result != 0) {
				message = accountService.getMessage();
			}
			return info;
		}
		catch (Exception e) {
			e.printStackTrace();
			result = -1;
			message = e.getMessage();
			return null;
		}
	}
	
	public GameRanking[] queryRankingList(int offset, int length) {
		try {
			AttainmentService attainmentService = new AttainmentService(server);
			GameRanking[] rankList = attainmentService.queryRankingList(paramManager.productId, 
					"desc", null, null, offset, length);
			result = attainmentService.getResult();
			if (result != 0) {
				message = attainmentService.getMessage();
			}
			return rankList;
		}
		catch (Exception e) {
			e.printStackTrace();
			result = -1;
			message = e.getMessage();
			return null;
		}
	}
	
	public GameAttainment readAttainment(int attainmentId) {
		try {
			AttainmentService attainmentService = new AttainmentService(server);
			GameAttainment ga = attainmentService.read(paramManager.accountId, paramManager.productId, 
					attainmentId, "desc", null, null);
			result = attainmentService.getResult();
			if (result != 0) {
				message = attainmentService.getMessage();
			}
			return ga;
		}
		catch (Exception e) {
			e.printStackTrace();
			result = -1;
			message = e.getMessage();
			return null;
		}
	}
	
	public void saveAttainment(GameAttainment attainment) {
		try {
			AttainmentService attainmentService = new AttainmentService(server);
			attainmentService.save(paramManager.accountId, paramManager.userId, paramManager.productId, attainment);
			result = attainmentService.getResult();
			if (result != 0) {
				message = attainmentService.getMessage();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			result = -1;
			message = e.getMessage();
		}
	}
	
	public OwnProp[] queryOwnPropList() {
		try {
			PropService propService = new PropService(server);
			OwnProp[] ownProps = propService.queryOwnPropList(paramManager.accountId, paramManager.productId);
			result = propService.getResult();
			if (result != 0) {
				message = propService.getMessage();
			}
			return ownProps;
		}
		catch (Exception e) {
			e.printStackTrace();
			result = -1;
			message = e.getMessage();
			return null;
		}
	}
	
	public Prop[] queryGamePropList() {
		try {
			PropService propService = new PropService(server);
			Prop[] props = propService.queryPropList(paramManager.productId);
			result = propService.getResult();
			if (result != 0) {
				message = propService.getMessage();
			}
			return props;
		}
		catch (Exception e) {
			e.printStackTrace();
			result = -1;
			message = e.getMessage();
			return null;
		}
	}
	
	public void synProps(int[] propIds, int[] counts) {
		try {
			PropService propService = new PropService(server);
			propService.synProps(paramManager.accountId, paramManager.productId, propIds, counts);
			result = propService.getResult();
			if (result != 0) {
				message = propService.getMessage();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			result = -1;
			message = e.getMessage();
		}
	}
	
	public void useProps(int propId, int num) {
		useProps(new int[]{propId}, new int[]{num});
	}
	
	public void useProps(int[] propIds, int[] nums) {
		try {
			PropService propService = new PropService(server);
			propService.useProps(paramManager.accountId, paramManager.productId, propIds, nums);
			result = propService.getResult();
			if (result != 0) {
				message = propService.getMessage();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			result = -1;
			message = e.getMessage();
		}
	}
	
	public void purchaseProp(int propId, int propCount, String remark) {
		try {
			PurchaseService purchaseService = new PurchaseService(server);
			int	b = purchaseService.purchaseProp(paramManager.buyURL, paramManager.accountId, paramManager.accountName, 
						paramManager.userToken, paramManager.productId, propId, propCount, remark, paramManager.checkKey);
			result = purchaseService.getResult();
			if (result == 0) {
				if (b >= 0) {
					ITVGame.balance -= b;
				}
				else {
					ITVGame.balance += b;
				}
			}
			else {
				message = purchaseService.getMessage();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			result = -1;
			message = e.getMessage();
		}
	}
	
	public PurchaseRecord[] queryPurchaseRecord(int offset, int length) {
		try {
			PurchaseService purchaseService = new PurchaseService(server);
			PurchaseRecord[] pr = purchaseService.queryPurchasePropRecord(paramManager.accountId, 
					paramManager.productId, offset, length);
			result = purchaseService.getResult();
			if (result != 0) {
				message = purchaseService.getMessage();
			}
			return pr;
		}
		catch (Exception e) {
			e.printStackTrace();
			result = -1;
			message = e.getMessage();
			return null;
		}
	}
	
	public GameRecord readRecord(int recordId) {
		try {
			RecordService recordService = new RecordService(server);
			GameRecord gr = recordService.read(paramManager.accountId, paramManager.productId, recordId);
			result = recordService.getResult();
			if (result != 0) {
				message = recordService.getMessage();
			}
			if (result == Constant.EC_RECORD_NOT_EXIST) {
				result = 0;
			}
			return gr;
		}
		catch (Exception e) {
			e.printStackTrace();
			result = -1;
			message = e.getMessage();
			return null;
		}
	}
	
	public void saveRecord(GameRecord record) {
		try {
			RecordService recordService = new RecordService(server);
			recordService.save(paramManager.accountId, paramManager.productId, record);
			result = recordService.getResult();
			if (result != 0) {
				message = recordService.getMessage();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			result = -1;
			message = e.getMessage();
		}
	}
	
	public void queryBalance() {
		try {
			SubscribeService subscribeService = new SubscribeService(server);
			int b = subscribeService.queryBalance(paramManager.buyURL, paramManager.accountId, paramManager.accountName, paramManager.productId);
			result = subscribeService.getResult();
			if (result == 0) {
				ITVGame.balance = b;
			}
			else {
				message = subscribeService.getMessage();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			result = -1;
			message = e.getMessage();
		}
	}
	
	public SubscribeRecord[] querySubscribeRecord(int offset, int length) {
		try {
			SubscribeService subscribeService = new SubscribeService(server);
			SubscribeRecord[] sr = subscribeService.querySubscribeRecord(paramManager.userId, paramManager.productId, offset, length);
			result = subscribeService.getResult();
			if (result != 0) {
				message = subscribeService.getMessage();
			}
			return sr;
		}
		catch (Exception e) {
			e.printStackTrace();
			result = -1;
			message = e.getMessage();
			return null;
		}
	}
	
	public void recharge(int amount, String remark) {
		try {
			SubscribeService subscribeService = new SubscribeService(server);
			int b = 0;
			
			b = subscribeService.recharge(paramManager.buyURL, paramManager.accountId, paramManager.accountName, 
					paramManager.userToken, paramManager.productId, amount,	ITVGame.getRechargeRatio(), 
					remark, paramManager.checkKey, paramManager.spid, null);
			
			result = subscribeService.getResult();
			if (result == 0) {
				if (b > 0) {
					ITVGame.balance += b;
				}
				else {
					ITVGame.balance = 0;
				}
			}
			else {
				message = subscribeService.getMessage();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			result = -1;
			message = e.getMessage();
		}
	}
	
	public void subscribe(int purchaseId, String remark) {
		try {
			SubscribeService subscribeService = new SubscribeService(server);
			subscribeService.subscribe(paramManager.buyURL, paramManager.accountId, paramManager.accountName, 
					paramManager.userToken, paramManager.productId, purchaseId, remark, paramManager.checkKey);
			result = subscribeService.getResult();
			if (result != 0) {
				message = subscribeService.getMessage();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			result = -1;
			message = e.getMessage();
		}
	}
	
	public void subscribeProduct(String subscribeType, String remark) {
		try {
			SubscribeService subscribeService = new SubscribeService(server);
			
			subscribeService.subscribeProduct(paramManager.buyURL, paramManager.accountId, paramManager.accountName, 
					paramManager.userToken, paramManager.productId, subscribeType, remark, paramManager.checkKey);
		
			result = subscribeService.getResult();
			if (result != 0) {
				message = subscribeService.getMessage();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			result = -1;
			message = e.getMessage();
		}
	}
	
	public java.util.Date querySystemTime() {
		try {
			SystemService systemService = new SystemService(server);
			java.util.Date t = systemService.getSystemTime();
			result = systemService.getResult();
			if (result != 0) {
				message = systemService.getMessage();
			}
			return t;
		}
		catch (Exception e) {
			e.printStackTrace();
			result = -1;
			message = e.getMessage();
			return null;
		}
	}
	
	public int getServiceResult() {
		return result;
	}
	
	public String getServiceMessage() {
		return message;
	}
	
	public boolean isServiceSuccessful() {
		return result == 0;
	}
}
