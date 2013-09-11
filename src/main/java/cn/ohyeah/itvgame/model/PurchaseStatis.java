package cn.ohyeah.itvgame.model;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * 消费排名统计
 * @author Administrator
 *
 */
public class PurchaseStatis {
	private String userId;
	private int sum;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getSum() {
		return sum;
	}
	public void setSum(int sum) {
		this.sum = sum;
	}
	
	public void readQueryResponseData(DataInputStream dis) throws IOException {
		userId = dis.readUTF();
		sum = dis.readInt();
	}
}
