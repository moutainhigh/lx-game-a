package cn.xgame.a.fighter;

import java.util.ArrayList;
import java.util.List;



import cn.xgame.a.fighter.o.Answers;
import cn.xgame.a.fighter.o.Askings;
import cn.xgame.a.fighter.o.Attackattr;

/**
 * 一个战斗者
 * @author deng
 * @date 2015-7-30 上午11:58:35
 */
public class Fighter {
	
	// 血量
	public int hp;
	
	// 攻击属性列表
	public List<Attackattr> attacks = new ArrayList<Attackattr>();
	
	// 防御属性列表
	public List<Attackattr> defends = new ArrayList<Attackattr>();
	
	// 应答 - 问
	public List<Askings> asking = new ArrayList<Askings>();
	
	// 应答 - 答
	public List<Answers> answer = new ArrayList<Answers>();
	
	private Attackattr getAtkattr( byte type ){
		for( Attackattr x : attacks ){
			if( x.getType() == type )
				return x;
		}
		return null;
	}
	
	private Attackattr getDefattr( byte type ){
		for( Attackattr x : defends ){
			if( x.getType() == type )
				return x;
		}
		return null;
	}
	
	/**
	 * 添加攻击属性 自动合并
	 * @param attrs
	 */
	public void addAtkattr( List<Attackattr> attrs ){
		for( Attackattr attr : attrs ){
			Attackattr temp = getAtkattr( attr.getType() );
			if( temp == null ){
				attacks.add(attr);
			}else{
				temp.addValue(attr.getValue());
			}
		}
	}
	
	/**
	 * 添加防御属性 自动合并
	 * @param attrs
	 */
	public void addDefattr( List<Attackattr> attrs ){
		for( Attackattr attr : attrs ){
			Attackattr temp = getDefattr( attr.getType() );
			if( temp == null ){
				defends.add(attr);
			}else{
				temp.addValue(attr.getValue());
			}
		}
	}
	
}
