package cn.xgame.a.world.planet.home.o;

public enum Institution {
	/**
	 * 独裁体制
	 */
	AUTARCHY {
		@Override
		public boolean isSenator( int i, int privilege ) {
			//独裁体质：一个人的话语权>50%，独裁体质形成
			return i == 0;
		}
	},
	
	/**
	 * 元老体制
	 */
	SENATOR {
		@Override
		public boolean isSenator( int i, int privilege ) {
			//b)元老体质：12个话语权最高的人，话语总和权大于50%
			return i <= 11;
		}
	},
	
	/**
	 * 共和体制
	 */
	REPUBLIC {
		@Override
		public boolean isSenator( int i, int privilege ) {
			//共和体质：12个话语权最高的人，话语权总和小于50（共和体质允许发起投票的人数在13~18之间，当前18个之前就超过50%按超过50%的最小人数算，反之取前18人）
			return privilege <= 5000 && i < 18;
		}
	};

	/**
	 * 是否元老
	 * @param i 排名
	 * @param privilege 当前总话语权
	 * @return
	 */
	public abstract boolean isSenator( int i, int privilege );
}
