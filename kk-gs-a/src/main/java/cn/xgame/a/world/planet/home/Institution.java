package cn.xgame.a.world.planet.home;

public enum Institution {
	/**
	 * 共和体制
	 */
	REPUBLIC {
		@Override
		public boolean isHaveSponsorVote( Child child, int index ) {
			return false;
		}
	};

	
	/**
	 * 是否有权限发起投票
	 * @param child
	 * @param index
	 * @return
	 */
	public abstract boolean isHaveSponsorVote( Child child, int index );
	
	
}
