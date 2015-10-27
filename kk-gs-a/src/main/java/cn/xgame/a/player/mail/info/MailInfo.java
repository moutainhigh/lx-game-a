package cn.xgame.a.player.mail.info;

import x.javaplus.mysql.db.Condition;
import x.javaplus.string.StringUtil;
import cn.xgame.a.player.mail.classes.IMail;
import cn.xgame.a.player.mail.classes.MailType;
import cn.xgame.a.player.u.Player;
import cn.xgame.gen.dto.MysqlGen.MailInfoDao;
import cn.xgame.gen.dto.MysqlGen.MailInfoDto;
import cn.xgame.gen.dto.MysqlGen.SqlUtil;
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

	public MailInfo( byte type, String title, String content, int money, Player player ) {
		super( MailType.fromNumber(type), title, content, money, player.getUID(), player.getNickname() );
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
