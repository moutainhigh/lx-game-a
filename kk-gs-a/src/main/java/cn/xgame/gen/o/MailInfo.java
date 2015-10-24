package cn.xgame.gen.o;


/**
 * 邮件信息
 * @author deng		
 * @date 2015-10-24 下午8:31:28
 */
public interface MailInfo {
	
	/** 服务器ID */
	short gsid();
	
	/** 玩家唯一ID */
	String uname();
	
	/** 唯一ID */
	int uid();
	
	/** 邮件类型 */
	byte type();
		
	/** 邮件标题 */
	String title();
		
	/** 邮件内容 */
	String content();
		
	/** 货币 */
	int	money();
		
	/** 附件 */
	byte[] adjuncts();
		
	/** 发送人UID */
	String senderUID();
		
	/** 发送人名字 */
	String senderName();
		
	/** 发送时间 */
	int	sendtime();
		
	/** 已读 & 已支付 */
	byte isRead();
	
	/** 时效*/
	int	durationtime();
}
