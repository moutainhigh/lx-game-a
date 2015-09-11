package cn.xgame.a.player.dock.capt;


import java.util.List;

import x.javaplus.mysql.db.Condition;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import cn.xgame.a.ITransformStream;
import cn.xgame.a.ectype.combat.CombatUtil;
import cn.xgame.a.ectype.combat.o.Answers;
import cn.xgame.a.ectype.combat.o.Askings;
import cn.xgame.a.ectype.combat.o.AtkAndDef;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.prop.captain.CaptainAttr;
import cn.xgame.a.prop.cequip.CEquipAttr;
import cn.xgame.gen.dto.MysqlGen.CaptainsDao;
import cn.xgame.gen.dto.MysqlGen.CaptainsDto;
import cn.xgame.gen.dto.MysqlGen.SqlUtil;

/**
 * 一个 舰长 信息
 * @author deng		
 * @date 2015-7-9 下午12:28:55
 */
public class CaptainInfo implements ITransformStream{

	// 舰长属性
	private CaptainAttr attr;
	
	// 所在星球ID
	private int snid;
	
	// 所属舰船UID
	private int shipUid 			= -1;
	
	// 装备
	private CEquipAttr equip;
	
	public CaptainInfo( int snid, int uid, int nid, byte quality ) {
		this.snid = snid;
		attr = (CaptainAttr) IProp.create( uid, nid, 1, quality );
		attr.randomAttachAttr();
	}

	public CaptainInfo( CaptainsDto dto ) {
		attr = (CaptainAttr) IProp.create( dto.getUid(), dto.getNid(), 1, dto.getQuality() );
		attr.wrapAttachBytes( dto.getAttachAttr() );
		shipUid	= dto.getShipUid();
		equip 	= new CEquipAttr( Unpooled.copiedBuffer( dto.getEquips() ) );
	}
	
	@Override
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeInt( attr.getUid() );
		buffer.writeInt( attr.getNid() );
		attr.buildTransformStream( buffer );
		buffer.writeInt( equip == null ? -1 : equip.getUid() );
		if( equip != null ){
			equip.buildTransformStream(buffer);
		}
		buffer.writeInt( shipUid );
	}

	//TODO------------数据库相关
	public void createDB( Player root ) {
		CaptainsDao dao = SqlUtil.getCaptainsDao();
		CaptainsDto dto = dao.create();
		dto.setGsid( root.getGsid() );
		dto.setUname( root.getUID() );
		dto.setUid( getuId() );
		setDBData( dto );
		dao.commit(dto);
	}
	public void updateDB( Player player ) {
		CaptainsDao dao = SqlUtil.getCaptainsDao();
		String sql 	= new Condition( CaptainsDto.gsidChangeSql( player.getGsid() ) ).
				AND( CaptainsDto.unameChangeSql( player.getUID() ) ).AND( CaptainsDto.uidChangeSql( getuId() ) ).toString();
		CaptainsDto dto = dao.updateByExact( sql );
		setDBData(dto);
		dao.commit(dto);
	}
	private void setDBData(CaptainsDto dto) {
		dto.setNid( attr.getNid() );
		dto.setQuality( attr.getQuality().toNumber() );
		dto.setAttachAttr( attr.toAttachBytes() );
		dto.setShipUid( shipUid );
		ByteBuf buf = Unpooled.buffer();
		equip.putBuffer( buf );
		dto.setEquips( buf.array() );
	}
	public void deleteDB( Player player ){
		CaptainsDao dao = SqlUtil.getCaptainsDao();
		String sql 		= new Condition( CaptainsDto.uidChangeSql( attr.getUid() ) ).AND( CaptainsDto.gsidChangeSql( player.getGsid() ) ).
				AND( CaptainsDto.unameChangeSql( player.getUID() ) ).toString();
		dao.deleteByExact( sql );
		dao.commit();
	}
	
	public CaptainAttr attr(){ return attr; }
	public int getuId() { return attr.getUid(); }
	public int getnId() { return attr.getNid(); }
	public CEquipAttr getEquip() { return equip; }
	public void setEquip( CEquipAttr equip ) { this.equip = equip; }
	public int getShipUid() { return shipUid; }
	public void setShipUid(int shipUid) { this.shipUid = shipUid; }
	public int getSnid() { return snid; }
	public void setSnid(int snid) { this.snid = snid; }
	
	//TODO------------其他函数


	/**
	 * 结算忠诚度
	 * @param value
	 */
	public boolean changeLoyalty( int value ) {
		
		return attr.changeLoyalty( value );
	}
	
	/**
	 * 是否到发工资的时候了
	 * 7 * 24 * 60 * 60 = 604800
	 * @return
	 */
	public boolean isWantPayoff() {
		int t = (int) (System.currentTimeMillis()/1000);
		return t - attr.getWeekTime() >= 86400;
	}
	
	/**
	 * 塞入舰长 战斗数据
	 * @param attacks
	 * @param defends
	 * @param askings
	 * @param answers
	 * @return
	 */
	public int warpFightProperty(List<AtkAndDef> attacks,List<AtkAndDef> defends, 
			List<Askings> askings, List<Answers> answers) {
		
		// 答
		CombatUtil.putAnswer( attr.getAnswers(), answers );
		// 问
		CombatUtil.putAsking( attr.getAskings(), askings );
		
		return 0;
	}

	

	




	


	
}
