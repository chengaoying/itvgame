package cn.ohyeah.stb.game;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.midlet.MIDlet;

import cn.ohyeah.stb.key.KeyCode;
import cn.ohyeah.stb.key.KeyState;
import cn.ohyeah.stb.res.ResourceManager;
import cn.ohyeah.stb.ui.DrawUtil;

/**
 * ��ֵ����
 * @author Administrator
 *
 */
public class GameRecharge extends GameCanvas implements Runnable{

	private static final byte STATE_SELECT_AMOUNT = 0;
	private static final byte STATE_CONFIRM = 1;
	
	private static short NUM_PICS = 0;
	private static final short PIC_ID_RECHARGE_BG = NUM_PICS++;
	private static final short PIC_ID_RECHARGE_TITLE = NUM_PICS++;
	private static final short PIC_ID_EXCHANGE_TITLE = NUM_PICS++;
	private static final short PIC_ID_CONFIRM_BG = NUM_PICS++;
	private static final short PIC_ID_CHECKED = NUM_PICS++;
	private static final short PIC_ID_UNCHECKED = NUM_PICS++;
	private static final short PIC_ID_OK0 = NUM_PICS++;
	private static final short PIC_ID_CANCEL0 = NUM_PICS++;
	private static final short PIC_ID_BACK0 = NUM_PICS++;
	private static final short PIC_ID_RECHARGE0 = NUM_PICS++;
	private static final short PIC_ID_EXCHANGE0 = NUM_PICS++;
	private static final short PIC_ID_PASSWORD_BG = NUM_PICS++;
	
	private static final String[] imagePaths = {
		"/business/recharge-bg.jpg",
		"/business/recharge-title.png",
		"/business/exchange-title.png",
		"/business/confirm-bg.jpg",
		"/business/checked.png",
		"/business/unchecked.png",
		"/business/ok0.png",
		"/business/cancel0.png",
		"/business/back0.png",
		"/business/recharge0.png",
		"/business/exchange0.png",
		"/business/password-bg.png",
	};

	protected Graphics g;
	protected ResourceManager resource;
	protected MIDlet midlet;
	//protected KeyState keyState;
	protected ParamManager pm;
	protected GameService gs;
	
	private boolean running;
	protected int screenWidth;
	protected int screenHeight;
	
	private byte groupIndex;
	private byte confirmIndex;
	private byte amountIndex;
	private byte state;
	private byte subState;
	private byte pwdGroupIndex;
	private byte pwdBtnIndex;
	private int rechargeAmount;
	private int[] amountList;
	
	public GameRecharge(ParamManager pm, GameService gs){
		super(false);
		g = getGraphics();
		this.pm = pm;
		this.gs = gs;
		amountList = pm.rechargeAmounts;
		resource = ResourceManager.createImageResourceManager(imagePaths);
		screenWidth = getWidth();
		screenHeight = getHeight();
	}
	
	public void run() {
		running = true;
		groupIndex = 1;
		try {
			while(running){
				handle();
				show(g);
				flushGraphics();
				execute();
			}
		}
		catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
		finally {
			clear();
		}
	}
	
	private void show(Graphics g) {
		switch(state) {
		case STATE_SELECT_AMOUNT: 
			showSelectAmount(g);
			break;
		case STATE_CONFIRM: 
			showConfirm(g);
			break;
		default:
			throw new RuntimeException("δ֪��״̬, state="+state);
		}
	}
	
	private void handle() {
		switch(state) {
		case STATE_SELECT_AMOUNT: 
			handleSelectAmount();
			break;
		case STATE_CONFIRM: 
			handleConfirm();
			break;
		default:
			throw new RuntimeException("δ֪��״̬, state="+state);
		}
	}
	
	private void execute() {
		switch(state) {
		case STATE_SELECT_AMOUNT: break;
		case STATE_CONFIRM: break;
		default:
			throw new RuntimeException("δ֪��״̬, state="+state);
		}
	}
	
	private void showSelectAmount(Graphics g) {
		g.setColor(43, 39, 36);
		Image bgImg = resource.loadImage(PIC_ID_RECHARGE_BG);
		g.drawImage(bgImg, 0, 0, 20);
		Font font = g.getFont();
		int amountsBgX = ((screenWidth-434)>>1)+3;
		int amountsBgY = ((screenHeight-324)>>1)+20;
		
		Image title = resource.loadImage(PIC_ID_RECHARGE_TITLE);
		
		if (title != null) {
			g.drawImage(title,amountsBgX + ((434-title.getWidth())>>1), amountsBgY + 17,20);
		}
		
		/*��ʾ֧����ʽ*/
		g.setColor(0XFFFF00);
		int sx, sy, sw, sh;
		String ss = "֧����ʽ:";
		sx = amountsBgX+35;
		sy = amountsBgY+61;
		g.setColor(242, 202, 0);
		g.drawString(ss, sx, sy, 20);
		sx += font.stringWidth(ss);
		sx += 8;
		Image checkedImg = resource.loadImage(PIC_ID_CHECKED);
		//Image uncheckedImg = resource.loadImage(PIC_ID_UNCHECKED);
		sw = checkedImg.getWidth();
		sh = checkedImg.getHeight();
		g.drawImage(checkedImg, sx, sy, 20);
		if (groupIndex == 0) {
			DrawUtil.drawRect(g, sx, sy, sw, sh, 2, 0XFFFF00);
		}
		sx += sw+2;
		ss = "�˵�֧��";
		g.setColor(242, 202, 0);
		g.drawString(ss, sx, sy, 20);
		sx += font.stringWidth(ss);
		sx += 8;
		
		/*��ʾ����б�*/
		sw = 276;
		sh = 33;
		int amountY = 0;
		String unit = "";
		int amount = 0;
		Image recharge  = resource.loadImage(PIC_ID_RECHARGE0);
		
		int textDelta = (sh-font.getHeight())>>1;
		int btnDelta = (sh-recharge.getHeight())>>1;
		
		int btnX = amountsBgX+330;
		sx = amountsBgX+33;
		sy = amountsBgY+90;
		for (int i = 0; i < amountList.length; ++i) {
			g.setColor(0X062C39);
			g.fillRect(sx, sy, sw, sh);
			g.setColor(0XA2FFFF);
            DrawUtil.drawRect(g, sx, sy, sw, sh, 2);
			amount = amountList[i]*ITVGame.getSubscribeCashToAmountRatio();
			unit = ITVGame.getSubscribeAmountUnit();
			
			g.setColor(0XFFFF00);
			g.drawString(amount+unit, sx+2, sy+textDelta, 20);
			
			ss = amountList[i]*ITVGame.getRechargeRatio()+ITVGame.getExpendAmountUnit();
			g.setColor(0XFFFF00);
			g.drawString(ss, sx+sw-font.stringWidth(ss)-2, sy+textDelta, 20);
			
			g.drawImage(recharge, btnX, sy+btnDelta, 20);
			if (groupIndex == 1 && amountIndex == i) {
				amountY = sy;
			}
			sy += sh+10;
		}
		
		
		Image back = resource.loadImage(PIC_ID_BACK0);
		sx = amountsBgX+((434-back.getWidth())>>1);
		sy = amountsBgY+284;
		g.drawImage(back, sx, sy, 20);
		
		if (groupIndex == 1) {
			DrawUtil.drawRect(g, btnX, amountY+2, recharge.getWidth(), recharge.getHeight(), 2, 0XFFFF00);
		}
		else if (groupIndex == 2){
			DrawUtil.drawRect(g, sx, sy, back.getWidth(), back.getHeight(), 2, 0XFFFF00);
		}
	}
	
	private void showConfirm(Graphics g) {
		Image bgImg = resource.loadImage(PIC_ID_CONFIRM_BG);
		int confirmX = ((screenWidth-bgImg.getWidth())>>1)+2;
		int confirmY = ((screenHeight-bgImg.getHeight())>>1)+17;
		
		g.drawImage(bgImg, confirmX, confirmY, 20);
		
		/*if (Configurations.getInstance().isServiceProviderWinside()) {
			g.setColor(0XFF0000);
			g.drawString("iTV�������ڶ�������������", confirmX+70, confirmY+138, 20);
		}*/
		
		String productName = "��ֵ"+rechargeAmount+"Ԫ";
		Font font = g.getFont();
		int textDelta = (25-font.getHeight())>>1;
		int sx = confirmX+170;
		int sy = confirmY+179+textDelta;
		g.setColor(0XFFFF00);
		g.drawString(productName, sx, sy, 20);
		
		String ss = rechargeAmount*ITVGame.getSubscribeCashToAmountRatio()+ITVGame.getSubscribeAmountUnit();
		sy = confirmY+216+textDelta;
		g.drawString(ss, sx, sy, 20);
		
		Image confirmBtn = resource.loadImage(PIC_ID_OK0);
		sx = confirmX+121;
		sy = confirmY+253;
		g.drawImage(confirmBtn, sx, sy, 20);
		if (confirmIndex == 0) {
			DrawUtil.drawRect(g, sx, sy, confirmBtn.getWidth(), confirmBtn.getHeight(), 3, 0XFF0000);
		}
		
		Image backBtn = resource.loadImage(PIC_ID_CANCEL0);
		sx = confirmX+253;
		g.drawImage(backBtn, sx, sy, 20);
		if (confirmIndex == 1) {
			DrawUtil.drawRect(g, sx, sy, confirmBtn.getWidth(), confirmBtn.getHeight(), 3, 0XFF0000);
		}
		
	}

	private void handleConfirm() {
		
		if (ITVGame.ks.containsAndRemove(KeyCode.LEFT)) {
			if (confirmIndex == 1) {
				confirmIndex = 0;
			}
		}else if (ITVGame.ks.containsAndRemove(KeyCode.RIGHT)) {
			if (confirmIndex == 0) {
				confirmIndex = 1;
			}
		}else if (ITVGame.ks.containsAndRemove(KeyCode.NUM0|KeyCode.BACK)) {
			clear();
			state=STATE_SELECT_AMOUNT;
		}else if (ITVGame.ks.containsAndRemove(KeyCode.OK)) {
			if (confirmIndex == 0) {
				String resultMsg = "";
				/*PopupText pt = UIResource.getInstance().buildDefaultPopupText();
				pt.setText("����"+engineService.getRechargeCommand()+"�����Ժ�...");
				pt.show(engine.getSGraphics());*/
				flushGraphics();
				try {
					gs.recharge(rechargeAmount,"");
					if (gs.isServiceSuccessful()) {
						resultMsg = "�ɹ�";
					}
					else {
						resultMsg = "ʧ�ܣ�ԭ��"+gs.getServiceMessage();
					}
				}
				catch (Exception e) {
					e.printStackTrace();
					resultMsg = "ʧ��, ԭ��: "+e.getMessage();
				}
				finally {
					if (gs.isServiceSuccessful()) {
						//pt.setText(resultMsg);
						//pt.popup();
						clear();
						state=STATE_SELECT_AMOUNT;
					}
					else {
							//pt.setText(resultMsg);
							//pt.popup();
							clear();
							state=STATE_SELECT_AMOUNT;
					}
				}
			}
			else {
				clear();
				state=STATE_SELECT_AMOUNT;
			}
		}
	}

	private void handleSelectAmount() {
		if (ITVGame.ks.containsAndRemove(KeyCode.UP)) {
			System.out.println("left");
			if (groupIndex == 1){
				if (amountIndex > 0) {
					--amountIndex;
				}
				else {
					groupIndex = 0;
				}
			}
			else if (groupIndex == 2){
				groupIndex = 1;
			}
		}else if (ITVGame.ks.containsAndRemove(KeyCode.DOWN)) {
			if (groupIndex == 0) {
				groupIndex = 1;
			}
			else if (groupIndex == 1) {
				if (amountIndex < amountList.length-1) {
					++amountIndex;
				}
				else {
					groupIndex = 2;
				}
			}
		}else if (ITVGame.ks.containsAndRemove(KeyCode.RIGHT)) {
			if (groupIndex == 1) {
				groupIndex = 2;
			}
		}else if (ITVGame.ks.containsAndRemove(KeyCode.LEFT)) {
			if (groupIndex == 2) {
				groupIndex = 1;
			}
		}else if (ITVGame.ks.containsAndRemove(KeyCode.NUM0|KeyCode.BACK)) {
			running = false;
		}else if (ITVGame.ks.containsAndRemove(KeyCode.OK)) {
			if (groupIndex == 1) {
				rechargeAmount = amountList[amountIndex];
				clear();
				state = STATE_CONFIRM;
			}else {
				running = false;
			}
		}
	}
	
	/*public final void keyPressed(int keyCode) {
		System.out.println("pressed keyCode:"+keyCode);
		keyState.keyPressed(keyCode);
	}
	
	public final void keyReleased(int keyCode) {
		System.out.println("releaseed keyCode:"+keyCode);
		keyState.keyReleased(keyCode);
	}
	*/
	public void clear() {
		resource.clear();
	}
}
