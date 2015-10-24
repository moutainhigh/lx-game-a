package cn.xgame.a.player.mail;

import java.util.Iterator;
import java.util.List;

import x.javaplus.collections.Lists;
import x.javaplus.mysql.db.Condition;

import cn.xgame.a.IFromDB;
import cn.xgame.a.player.mail.info.MailInfo;
import cn.xgame.a.player.u.Player;
import cn.xgame.gen.dto.MysqlGen.MailInfoDao;
import cn.xgame.gen.dto.MysqlGen.MailInfoDto;
import cn.xgame.gen.dto.MysqlGen.SqlUtil;
import cn.xgame.system.SystemCfg;

/**
 * 邮件操作中心
 * @author deng		
 * @date 2015-10-14 下午4:55:20
 */
public class MailControl implements IFromDB{
	
	// 一页最多个数
	private static final int MAX_NUM = 20;
	
	//
	private Player root;
	
	// 所有邮件列表
	private List<MailInfo> mails = Lists.newArrayList();
	
	
	public MailControl(Player player) {
		root = player;
	}

	@Override
	public void fromDB() {
		mails.clear();
		MailInfoDao dao = SqlUtil.getMailInfoDao();
		String sql = new Condition( MailInfoDto.gsidChangeSql( SystemCfg.ID ) ).AND( MailInfoDto.unameChangeSql( root.getUID() ) ).toString();
		List<MailInfoDto> dtos = dao.getByExact(sql);
		dao.commit();
		for( MailInfoDto dto : dtos ){
			MailInfo mail = new MailInfo( dto );
			mails.add(mail);
		}
	}
	
	/**
	 * 根据唯一ID 获取邮件 
	 * @param uid
	 * @return
	 */
	public MailInfo getMail( int uid ) {
		for( MailInfo mail : mails ){
			if( mail.getUid() == uid )
				return mail;
		}
		return null;
	}
	
	/**
	 * 添加一个邮件
	 * @param mail
	 */
	public void addMail( MailInfo mail ) {
		mails.add( mail );
	}
	
	/**
	 * 删除一个邮件
	 * @param uid
	 */
	public void remove( int uid ) {
		Iterator<MailInfo> iter = mails.iterator();
		while( iter.hasNext() ){
			MailInfo mail = iter.next();
			if( mail.getUid() == uid ){
				iter.remove();
				mail.deleteDB( root.getUID() );
				break;
			}
		}
	}
	
	/**
	 * 根据页数获取邮件列表
	 * @param page
	 * @return
	 */
	public List<MailInfo> getMailsByPage( byte page ) {
		List<MailInfo> ret = Lists.newArrayList();
		for( int i = (page-1)*MAX_NUM; i < MAX_NUM && i < mails.size(); i++ ){
			ret.add( mails.get(i) );
		}
		return ret;
	}


}
