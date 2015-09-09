package cn.xgame.a.player.captain.o;


import java.util.List;

import x.javaplus.mysql.db.Condition;
import io.netty.buffer.ByteBuf;
import cn.xgame.a.ITransformStream;
import cn.xgame.a.combat.CombatUtil;
import cn.xgame.a.combat.o.Answers;
import cn.xgame.a.combat.o.Askings;
import cn.xgame.a.combat.o.AtkAndDef;
import cn.xgame.a.player.captain.o.v.EquipControl;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.prop.captain.CaptainAttr;
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
	
	private EquipControl equips 	= new EquipControl();
	
	public CaptainInfo( int uid, int nid, byte quality ) {
		attr = (CaptainAttr) IProp.create( uid, nid, 1, quality );
		attr.randomAttachAttr();
	}

	public CaptainInfo(CaptainsDto dto) {
		attr = (CaptainAttr) IProp.create( dto.getUid(), dto.getNid(), 1, dto.getQuality() );
		attr.wrapAttachBytes( dto.getAttachAttr() );
		shipUid	= dto.getShipUid();
		equips.fromBytes( dto.getEquips() );
	}
	
	@Override
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeInt( attr.getUid() );
		buffer.writeInt( attr.getNid() );
		attr.buildTransformStream( buffer );
		equips.buildTransformStream( buffer );
	}

	public CaptainAttr attr(){ return attr; }
	public int getuId() { return attr.getUid(); }
	public int getnId() { return attr.getNid(); }
	public EquipControl getEquips() { return equips; }
	public int getShipUid() { return shipUid; }
	public void setShipUid(int shipUid) { this.shipUid = shipUid; }
	public int getSnid() { return snid; }
	public void setSnid(int snid) { this.snid = snid; }
	
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
		dto.setEquips( equips.toBytes() );
	}
	
	//TODO------------其他函数

	
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
		CombatUtil.putAnswer( attr.templet().answer, answers );
		
		return equips.warpFightProperty( attacks, defends, askings, answers );
	}

	


	
}
