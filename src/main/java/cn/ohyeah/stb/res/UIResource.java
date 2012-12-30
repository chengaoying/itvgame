package cn.ohyeah.stb.res;

import javax.microedition.lcdui.Image;

import cn.ohyeah.stb.game.GameRecharge;
import cn.ohyeah.stb.ui.PopupText;

/**
 * 通用UI资源
 * @author maqian
 * @version 1.0
 */
public class UIResource {
	
	private static final short X_POPUP_TEXT = 18;
	private static final short Y_POPUP_TEXT = 35;
	private static final short W_POPUP_TEXT = 245;
	private static final short H_POPUP_TEXT = 96;
	private static final short W_CONFIRM_BTN = 60;
	private static final short H_CONFIRM_BTN = 27;
	private static final short BORDER_CONFIRM_BTN = 5;
	
	//private static final String[] STR_CONFIRM_BTN_TEXT = {"确定", "取消"};
	
	private static final short PIC_ID_POPUP_BG = 0;
	private static final short PIC_ID_POPUP_BTN = 1;
	
	private static final String[] imagePaths = {
		"/common/popup-bg.png",
		"/common/popup-btn.png",
	};
	
	private static UIResource instance;
	
	private PopupText defaultPt;
	private ResourceManager resource;
	private static GameRecharge gameRecharge;
	
	private UIResource() {
		resource = ResourceManager.createImageResourceManager(imagePaths);
		registerDefaultPopup();
	}
	
	public static void registerEngine(GameRecharge gr) {
		gameRecharge = gr;
		instance = new UIResource();
	}
	
	public static UIResource getInstance() {
		return instance;
	}
	
	public PopupText buildDefaultPopupTextWithoutBtn() {
		PopupText pt = buildDefaultPopupText();
		pt.setButtonBg(null);
		return pt;
	}
	
	public void clearDefaultPopupTextWithoutBtn() {
		clearDefaultPopupText();
	}
	
	public PopupText buildDefaultPopupText() {
		defaultPt.setText(null);
		defaultPt.setWaitMillisSeconds(0);
		registerTextBg(defaultPt);
		registerButtonBg(defaultPt);
		return defaultPt;
	}

	private void registerButtonBg(PopupText pt) {
		pt.setButtonBg(resource.loadImage(PIC_ID_POPUP_BTN), 
				W_CONFIRM_BTN, (short)0, W_CONFIRM_BTN, H_CONFIRM_BTN);
		pt.setButtonBorder(BORDER_CONFIRM_BTN);
		pt.setButtonText("确定");
		pt.setButtonTextColor(0XFFFFFF);
	}

	private void registerTextBg(PopupText pt) {
		Image textBg = resource.loadImage(PIC_ID_POPUP_BG);
		pt.setTextBg(textBg);
		pt.setTextBgPos(((gameRecharge.screenWidth-textBg.getWidth())>>1), 
				((gameRecharge.screenHeight-textBg.getHeight())>>1));
		defaultPt.setTextRegion(X_POPUP_TEXT, Y_POPUP_TEXT, W_POPUP_TEXT, H_POPUP_TEXT);
		pt.setTextColor(0XFFFFFF);
	}
	
	public void clearDefaultPopupText() {
		resource.freeImage(PIC_ID_POPUP_BG);
		resource.freeImage(PIC_ID_POPUP_BTN);
		defaultPt.clear();
	}
	
	
	public void clearDefaultPopup() {
		clearDefaultPopupText();
	}
	
	private void registerDefaultPopup() {
		registerDefaultPopupText();
	}
	
	private void registerDefaultPopupText() {
		defaultPt = new PopupText(gameRecharge);
		registerTextBg(defaultPt);
		registerButtonBg(defaultPt);
		defaultPt.setWaitMillisSeconds(0);
	}

}
