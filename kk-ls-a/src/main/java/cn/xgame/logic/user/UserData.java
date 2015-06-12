package cn.xgame.logic.user;

import io.netty.channel.ChannelHandlerContext;
import cn.xgame.utils.PackageCheck;

public class UserData {

	// 用户唯一ID
	private String UID;
	
	// 账号
	private String account;
	
	// 密码
	private String password;
	
	// uin
	private String uin;

	
	private ChannelHandlerContext ctx;
	private PackageCheck pc;
	
	public String getUID() {
		return UID;
	}
	public void setUID(String uID) {
		UID = uID;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUin() {
		return uin;
	}
	public void setUin(String uin) {
		this.uin = uin;
	}
	public ChannelHandlerContext getCtx() {
		return ctx;
	}
	public void setCtx(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}
	public PackageCheck getPc() {
		return pc;
	}
	public void setPc(PackageCheck pc) {
		this.pc = pc;
	}
	

}
