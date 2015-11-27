package cn.xgame.a.player.mail.info;

import x.javaplus.mysql.db.Condition;
import x.javaplus.string.StringUtil;
import cn.xgame.a.player.mail.classes.IMail;
import cn.xgame.a.player.mail.classes.MailType;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.gen.dto.MysqlGen.MailInfoDao;
import cn.xgame.gen.dto.MysqlGen.MailInfoDto;
import cn.xgame.gen.dto.MysqlGen.SqlUtil;
import cn.xgame.system.LXConstants;
import cn.xgame.system.SystemCfg;

/**
 * 一个邮件
 * @author deng		
 * @date 2015-10-24 下午8:55:49
 */
public class MailInfo extends IMail{

	public MailInfo(MailInfoDto dto) {
		super(dto);
	}

	/**
	 * 玩家发送邮件
	 * @param type
	 * @param title
	 * @param content
	 * @param money
	 * @param player
	 */
	public MailInfo( byte type, String title, String content, Player player ) {
		super( MailType.fromNumber(type), title, content, player.getUID(), player.getNickname() );
	}
	
	/**
	 * 系统邮件
	 * @param type
	 * @param title
	 * @param content
	 */
	public MailInfo( MailType type, String title, String content ){
		super(type,title,content);
	}
	
	/**
	 * 添加道具
	 * @param nid
	 * @param count
	 */
	public void addProp( int nid, int count ){
		if( CsvGen.getItemPo(nid) == null )
			return;
		if( count == 0 )
			return;
		if( nid == LXConstants.CURRENCY_NID ){
			setMoney(count);
		}else{
			IProp prop = IProp.create(-1, nid, count);
			addProp(prop);
		}
	}
	
	/**
	 * 创建数据库
	 * @param root
	 */
	public void createDB( String uname ) {
		setUid( generateUID(uname) );
		MailInfoDao dao = SqlUtil.getMailInfoDao();
		MailInfoDto dto = dao.create();
		dto.setGsid( SystemCfg.ID );
		dto.setUname( uname );
		dto.setUid( getUid() );
		dto.setType( getType().toNumber() );
		dto.setTitle( StringUtil.toBytes( getTitle() ) );
		dto.setContent( StringUtil.toBytes( getContent() ) );
		dto.setMoney( getMoney() );
		dto.setAdjuncts( getAdjuncts() );
		dto.setSenderUID( getSenderUID() );
		dto.setSenderName( getSenderName() );
		dto.setSendtime( getSendtime() );
		dto.setIsRead( (byte) (isRead() ? 1 : 0) );
		dto.setDurationtime( getDurationtime() );
		dao.commit(dto);
	}
	
	/**
	 * 更新数据库
	 * @param uid
	 */
	public void updateDB( String uname ) {
		MailInfoDao dao = SqlUtil.getMailInfoDao();
		String sql = new Condition( MailInfoDto.unameChangeSql( uname ) ).
				AND( MailInfoDto.gsidChangeSql( SystemCfg.ID ) ).AND( MailInfoDto.uidChangeSql( getUid() ) ).toString();
		MailInfoDto dto = dao.updateByExact( sql );
		dto.setIsRead( (byte) (isRead() ? 1 : 0) );
		dto.setMoney( getMoney() );
		dto.setAdjuncts( getAdjuncts() );
		dao.commit(dto);
	}
	
	/**
	 * 删除数据库
	 * @param uname
	 */
	public void deleteDB( String uname ) {
		MailInfoDao dao = SqlUtil.getMailInfoDao();
		String sql = new Condition( MailInfoDto.unameChangeSql( uname ) ).
				AND( MailInfoDto.gsidChangeSql( SystemCfg.ID ) ).AND( MailInfoDto.uidChangeSql( getUid() ) ).toString();
		dao.deleteByExact( sql );
		dao.commit();
	}
	
	// 生成唯一ID
	private int generateUID( String uname ){
		String sql	= new Condition( MailInfoDto.unameChangeSql( uname ) ).AND( MailInfoDto.gsidChangeSql( SystemCfg.ID ) ).toString();
		return SqlUtil.getMaxId( SqlUtil.getClassName( MailInfoDto.class ), "uid", sql ) + 1;
	}

}
