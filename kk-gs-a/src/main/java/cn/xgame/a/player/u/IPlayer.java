package cn.xgame.a.player.u;

import cn.xgame.a.player.manor.ManorControl;
import cn.xgame.gen.dto.MysqlGen.PlayerDataDto;

/**
 * 玩家 基础 数据结构 （数据库相关）
 * @author deng		
 * @date 2015-6-17 下午2:49:58
 */
public abstract class IPlayer {

	// 唯一ID
	private String UID;
	// 服务器ID
	private short gsid;
	
	// 玩家昵称
	private String nickname;
	// 头像图标ID
	private int headIco;
	// 副官ID
	private int adjutantId;
	
	// 区域 - 属于星球ID
	private int countryId;
	
	// 游戏币
	private int currency;
	// 充值币
	private int gold;
	
	
	// 创建时间 (单位：毫秒)
	private long createTime;
	
	// 上次下线时间 (单位：毫秒)
	private long lastLogoutTime;
	
	
	/////////////////////////////////////
	// 玩家领地
	private ManorControl manors;
	
	
	
	public void wrap( PlayerDataDto dto ){
		
		this.UID 		= dto.getUid();
		this.gsid		= dto.getGsid();
		this.nickname 	= dto.getNickname();
		this.headIco	= dto.getHeadIco();
		this.adjutantId = dto.getAdjutantId();
		this.countryId	= dto.getCountryId();
		this.currency	= dto.getCurrency();
		this.gold		= dto.getGold();
		this.createTime	= dto.getCreateTime();
		this.lastLogoutTime = dto.getLastLogoutTime();
		this.manors		= new ManorControl( );
		this.manors.fromBytes( dto.getManors() );
	}
	
	public void update( PlayerDataDto dto ) {
		dto.setUid(UID);
		dto.setGsid(gsid);
		dto.setNickname(nickname);
		dto.setHeadIco(headIco);
		dto.setAdjutantId(adjutantId);
		dto.setCountryId(countryId);
		dto.setCurrency(currency);
		dto.setGold(gold);
		dto.setCreateTime(createTime);
		dto.setLastLogoutTime(lastLogoutTime);
		dto.setManors(manors.toBytes());
	}
	
	/**
	 * 改变货币
	 * @param value 添加用正号  减少用负号
	 */
	public int changeCurrency( int value ) {
		if( currency + value < 0  )
			return -1;
		currency += value;
		return currency;
	}
	
	public String getUID() {
		return UID;
	}
	public void setUID(String uID) {
		UID = uID;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public int getHeadIco() {
		return headIco;
	}
	public void setHeadIco(int headIco) {
		this.headIco = headIco;
	}
	public int getCountryId() {
		return countryId;
	}
	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}
	public int getCurrency() {
		return currency;
	}
	public void setCurrency(int currency) {
		this.currency = currency;
	}
	public int getGold() {
		return gold;
	}
	public void setGold(int gold) {
		this.gold = gold;
	}
	public long getLastLogoutTime() {
		return lastLogoutTime;
	}
	public void setLastLogoutTime(long lastLogoutTime) {
		this.lastLogoutTime = lastLogoutTime;
	}
	public short getGsid() {
		return gsid;
	}
	public void setGsid(short gsid) {
		this.gsid = gsid;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public ManorControl getManors() {
		return manors;
	}
	public void setManors(ManorControl manor) {
		this.manors = manor;
	}
	public int getAdjutantId() {
		return adjutantId;
	}
	public void setAdjutantId(int adjutantId) {
		this.adjutantId = adjutantId;
	}
	
	
	
	
}
