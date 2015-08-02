package cn.xgame.a.player.captain.o;


import java.util.List;

import x.javaplus.mysql.db.Condition;
import io.netty.buffer.ByteBuf;
import cn.xgame.a.ITransformStream;
import cn.xgame.a.combat.CombatUtil;
import cn.xgame.a.combat.o.Answers;
import cn.xgame.a.combat.o.Askings;
import cn.xgame.a.combat.o.AtkAndDef;
import cn.xgame.a.player.IUObject;
import cn.xgame.a.player.captain.o.v.EquipControl;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.cequip.CEquip;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.CaptainPo;
import cn.xgame.gen.dto.MysqlGen.CaptainsDao;
import cn.xgame.gen.dto.MysqlGen.CaptainsDto;
import cn.xgame.gen.dto.MysqlGen.SqlUtil;

/**
 * 一个 舰长 信息
 * @author deng		
 * @date 2015-7-9 下午12:28:55
 */
public class CaptainInfo extends IUObject implements ITransformStream{

	private final CaptainPo templet;
	
	// 所属舰船UID
	private int shipUid 			= -1;
	
	private EquipControl equips 	= new EquipControl();
	
	public CaptainInfo(int uid, int nid) {
		super( uid, nid );
		templet = CsvGen.getCaptainPo(nid);
	}

	public CaptainInfo(CaptainsDto dto) {
		super( dto.getUid(), dto.getNid() );
		templet = CsvGen.getCaptainPo(dto.getNid());
		shipUid	= dto.getShipUid();
		equips.fromBytes( dto.getEquips() );
	}
	
	@Override
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeInt( getuId() );
		buffer.writeInt( getnId() );
		CEquip equip = equips.getEquip();
		buffer.writeInt( equip == null ? -1 : equip.getnId() );
	}

	public CaptainPo templet(){ return templet; }
	public EquipControl getEquips() { return equips; }
	public int getShipUid() { return shipUid; }
	public void setShipUid(int shipUid) { this.shipUid = shipUid; }
	
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
		dto.setNid(getnId());
		dto.setShipUid(shipUid);
		dto.setEquips(equips.toBytes());
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
		CombatUtil.putAnswer( templet.answer, answers );
		
		return equips.warpFightProperty( attacks, defends, askings, answers );
	}

	
	
}
