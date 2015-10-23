package cn.xgame.a.chat.axn;

import java.util.Iterator;
import java.util.List;

import cn.xgame.a.chat.axn.classes.ChatType;
import cn.xgame.a.chat.axn.info.AxnInfo;
import cn.xgame.gen.dto.MysqlGen.AxnInfoDao;
import cn.xgame.gen.dto.MysqlGen.AxnInfoDto;
import cn.xgame.gen.dto.MysqlGen.SqlUtil;
import cn.xgame.system.LXConstants;

import x.javaplus.collections.Lists;
import x.javaplus.mysql.db.Condition;


/**
 * 频道操作中心
 * @author deng		
 * @date 2015-8-2 下午12:49:07
 */
public class AxnControl {
	
	// 组队UID
	private int teamUID = ChatType.TEAM.toNumber()*LXConstants.CHAT_UID;
	private List<Integer> teamTeam = Lists.newArrayList();
	// 群聊UID
	private int	groupUID = ChatType.GROUP.toNumber()*LXConstants.CHAT_UID;
	private List<Integer> groupTeam = Lists.newArrayList();
	// 私聊UID
	private int privateUID = ChatType.PRIVATE.toNumber()*LXConstants.CHAT_UID;
	private List<Integer> privateTeam = Lists.newArrayList();
	
	// 频道列表
	private List<AxnInfo> axns = Lists.newArrayList();
	
	
	/**
	 * 根据唯一ID获取对应频道信息
	 * @param id
	 * @return
	 */
	public AxnInfo getAXNInfo( int id ) {
		for( AxnInfo axn : axns ){
			if( axn.getAxnId() == id )
				return axn;
		}
		return null;
	}
	
	/**
	 * 创建一个频道
	 * @param tempaxn
	 * @param p1
	 * @param p2
	 * @return 
	 * @throws Exception 
	 */
	public AxnInfo createAxn( ChatType type ) throws Exception {
		AxnInfo axn = new AxnInfo( type, generateUID( type ) );
		axns.add(axn);
		create( axn );
		return axn;
	}
	
	private int generateUID( ChatType type ) {
		if( type == ChatType.TEAM ){
			if( groupTeam.isEmpty() )
				return ++teamUID;
			else
				return teamTeam.remove(0);
		}
		if( type == ChatType.GROUP ){
			if( groupTeam.isEmpty() )
				return ++groupUID;
			else
				return groupTeam.remove(0);
		}
		if( type == ChatType.PRIVATE ){
			if( privateTeam.isEmpty() )
				return ++privateUID;
			else
				return privateTeam.remove(0);
		}
		return 0;
	}

	/**
	 * 删除一个 频道
	 * @param id
	 */
	public void removeAxn( int id ) {
		Iterator<AxnInfo> iter = axns.iterator();
		while( iter.hasNext() ){
			AxnInfo next = iter.next();
			if( next.getAxnId() == id ){
				appendUID( next.getType(), id );
				iter.remove();
				delete( next );
				return;
			}
		}
	}

	// 添加临时ID
	private void appendUID( ChatType type, int id ) {
		switch( type ){
		case TEAM:
			teamTeam.add(id);
			break;
		case GROUP:
			groupTeam.add(id);
			break;
		case PRIVATE:
			privateTeam.add(id);
			break;
		}
	}

	//===============================================================
	//TODO========================数据库相关 (暂时只保存群聊数据)==========================
	//===============================================================
	/**
	 * 创建
	 * @param axn
	 */
	private void create( AxnInfo axn ) {
		AxnInfoDao dao = SqlUtil.getAxnInfoDao();
		AxnInfoDto dto = dao.create();
		dto.setId( axn.getAxnId() );
		dto.setName( axn.getName() );
		dto.setType( axn.getType().toNumber() );
		dao.commit(dto);
	}
	
	/**
	 * 保存到数据库
	 * @param axn
	 */
	public void update( AxnInfo axn ) {
		AxnInfoDao dao = SqlUtil.getAxnInfoDao();
		AxnInfoDto dto = dao.updateByExact( new Condition( AxnInfoDto.idChangeSql(axn.getAxnId())).
				AND( AxnInfoDto.typeChangeSql( axn.getType().toNumber() )).toString() );
		dto.setName( axn.getName() );
		dto.setPlayers( axn.getAxnCrewsToBytes() );
		dao.commit(dto);
	}
	
	/**
	 * 获取所有数据
	 */
	public void init(){
		AxnInfoDao dao = SqlUtil.getAxnInfoDao();
		List<AxnInfoDto> dtos = dao.getByExact( new Condition(AxnInfoDto.typeChangeSql( ChatType.GROUP.toNumber() )).toString() );
		dao.commit();
		for( AxnInfoDto dto : dtos ){
			AxnInfo axn = new AxnInfo( ChatType.fromNumber(dto.getType()), dto.getId() );
			axn.setName( dto.getName() );
			axn.setAxnCrews( dto.getPlayers() );
			axns.add( axn );
			if( axn.getAxnId() > groupUID )
				groupUID = axn.getAxnId();
		}
		privateUID = 0;
		teamUID = 0;
		privateTeam.clear();
		teamTeam.clear();
	}
	
	// 删除
	private void delete( AxnInfo axn ) {
		AxnInfoDao dao = SqlUtil.getAxnInfoDao();
		dao.delete( axn.getAxnId() );
		dao.commit();
	}
}
