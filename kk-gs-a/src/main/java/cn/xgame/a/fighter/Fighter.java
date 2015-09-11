package cn.xgame.a.fighter;

import java.util.ArrayList;
import java.util.List;


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
	
	
}
