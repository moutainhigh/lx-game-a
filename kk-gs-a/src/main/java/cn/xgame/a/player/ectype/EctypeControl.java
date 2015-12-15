package cn.xgame.a.player.ectype;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.List;

import x.javaplus.collections.Lists;

import cn.xgame.a.IArrayStream;
import cn.xgame.a.player.ectype.info.ChapterInfo;
import cn.xgame.a.player.ectype.info.ChapterLevel;
import cn.xgame.a.player.ectype.info.StarGeneralEctype;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.world.WorldManager;
import cn.xgame.a.world.planet.IPlanet;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.ChapterPo;
import cn.xgame.utils.Logs;
import cn.xgame.utils.LuaUtil;

/**
 * 副本信息
 * @author deng		
 * @date 2015-7-17 上午3:23:52
 */
public class EctypeControl implements IArrayStream{

	// 临时记录 玩家每天第一次的登录时间 (这里为方便计算副本在登录时候 计时)
	private int logintime;
	
	// 临时记录 星球ID
	private List<Integer> tempSnid = Lists.newArrayList();
	
	// 所有星球的偶发副本 集合
	private List<ChapterInfo> allChances = Lists.newArrayList();
	
	
	// 常规副本难度
	private List<StarGeneralEctype> starels = Lists.newArrayList();
	
	public EctypeControl( Player player ) {
	}
	
	@Override
	public void fromBytes(byte[] data) {
		if( data == null ) return;
		ByteBuf buf = Unpooled.copiedBuffer(data);
		logintime = buf.readInt();
		int size = buf.readShort();
		for (int i = 0; i < size; i++) {
			ChapterPo templet = CsvGen.getChapterPo( buf.readInt() );
			if( templet == null )
				continue;
			ChapterInfo chapter = new ChapterInfo( templet, buf.readInt() );
			chapter.wrapBuffer(buf);
			allChances.add(chapter);
		}
		size = buf.readInt();
		for( int i = 0; i < size; i++ ){
			StarGeneralEctype x = new StarGeneralEctype(buf.readInt());
			x.wrapBuffer(buf);
			starels.add(x);
		}
	}

	@Override
	public byte[] toBytes() {
		if( allChances.isEmpty() )
			return null;
		ByteBuf buf = Unpooled.buffer( );
		buf.writeInt( logintime );
		buf.writeShort( allChances.size() );
		for( ChapterInfo chapter : allChances ){
			buf.writeInt( chapter.getId() );
			buf.writeInt( chapter.getSnid() );
			chapter.putBuffer(buf);
		}
		buf.writeInt( starels.size() );
		for( StarGeneralEctype x : starels ){
			buf.writeInt( x.getSnid() );
			x.putBuffer(buf);
		}
		return buf.array();
	}

	/**
	 * 根据星球生成 偶发副本 列表
	 * @param sid
	 * @return
	 */
	private List<ChapterInfo> generateChanceEctype( int sid ) {
		List<ChapterInfo> ret = Lists.newArrayList();
		String content = LuaUtil.getEctypeInfo().getField( "generateChanceEctype" ).call( 1, sid )[0].getString();
		if( content != null && !content.isEmpty() ){
			String[] str = content.split( ";" );
			for( String x : str ){
				ChapterPo templet = CsvGen.getChapterPo( Integer.parseInt( x ) );
				if( templet == null ) {
					Logs.error( "在生成偶发副本出错 Chapter表格找不到 " + x + ", at = EctypeControl.generateChanceEctype" );
					continue;
				}
				// 如果时间已经超过当日第一次登录的时间 那么就不生成该副本
				int curtime = (int) (System.currentTimeMillis()/1000);
				if( curtime >= logintime + templet.time && templet.time != 0 )
					continue;
				ChapterInfo chapter = new ChapterInfo( templet, sid );
				chapter.init( templet );
				chapter.setEndtime( templet.time == 0 ? 0 : (int)(logintime + templet.time) );
				chapter.generateNextEctype();
				Logs.debug( "生成副本 属性=[" + chapter.getEctypes() + "]" );
				ret.add(chapter);
			}
		}
		return ret;
	}
	
	/**
	 * 根据星球ID 和章节ID 获取对应偶发章节
	 * @param sid
	 * @param id
	 * @return
	 */
	public ChapterInfo getChapter( int sid, int id ){
		for( ChapterInfo chapter : allChances ){
			if( chapter.getSnid() == sid && chapter.getId() == id )
				return chapter;
		}
		return null;
	}
	
	/**
	 * 根据星球获取 偶发副本
	 * @param sid
	 */
	public List<ChapterInfo> getChanceEctype( int sid ){
		List<ChapterInfo> ret = Lists.newArrayList();
		for( ChapterInfo chapter : allChances ){
			if( chapter.getSnid() != sid )
				continue;
			ret.add(chapter);
		}
		if( ret.isEmpty() && tempSnid.indexOf(sid) == -1 ){
			ret.addAll( generateChanceEctype( sid ) );
			allChances.addAll( ret );
			tempSnid.add( sid );
		}
		return ret;
	}
	
	
	/**
	 * 刷新一下偶发副本 把时间到的删除掉
	 */
	public void updateChanceEctype(){
		int curtime = (int) (System.currentTimeMillis()/1000);
		List<ChapterInfo> remove = Lists.newArrayList();
		for( ChapterInfo chapter : allChances ){
			if( chapter.getEndtime() == 0 || curtime < chapter.getEndtime() )
				continue;
			remove.add(chapter);
		}
		allChances.removeAll(remove);
	}
	
	/**
	 * 清理数据
	 */
	public void clear(){
		allChances.clear();
		tempSnid.clear();
		logintime = (int) (System.currentTimeMillis()/1000);
	}
	
	/*-----------------------------常规副本----------------------------------------------*/
	
	/**
	 * 根据星球获取 常规副本
	 * @param sid
	 * @return
	 */
	public List<ChapterInfo> getGeneralEctype( int sid ){
		List<ChapterInfo> ret = Lists.newArrayList();
		try {
			WorldManager o = WorldManager.o;
			// 先放入本星球副本
			IPlanet planet = o.getPlanet(sid);
			ret.addAll( generateEctypeLevel(planet) );
			// 然后放入根据瞭望出来的星球副本信息
			List<Integer> scope = planet.getScopePlanet();
			for( int id : scope ){
				ret.addAll( generateEctypeLevel(o.getPlanet(id)) );
			}
		} catch (Exception e) { }
		return ret;
	}
	private List<ChapterInfo> generateEctypeLevel(IPlanet planet) {
		List<ChapterInfo> ret = Lists.newArrayList();
		StarGeneralEctype sgel = getSGEL(planet.getId());
		if( sgel == null ){
			sgel = new StarGeneralEctype(planet.getId());
			starels.add(sgel);
		}
		for( ChapterInfo chapter : planet.getChapters() ){
			ChapterLevel cl = sgel.getCl(chapter.getId());
			if( cl == null ){
				sgel.put( chapter.getId(), chapter.getEctypes().size(), 1 );
			}
			ret.add(chapter);
		}
		return ret;
	}
	
	/**
	 * 生成对应星球的章节 的下一个难度
	 * @param berthSnid
	 * @param chapterId
	 */
	public void generateNextGeneralEctype( int berthSnid, int chapterId ) {
		StarGeneralEctype sgel = getSGEL(berthSnid);
		if( sgel != null ){
			ChapterLevel cl = sgel.getCl(chapterId);
			if( cl != null ){
				cl.next();
			}
		}
	}
	
	public StarGeneralEctype getSGEL( int sid ){
		for( StarGeneralEctype x : starels ){
			if( x.getSnid() == sid ){
				return x;
			}
		}
		return null;
	}
}
